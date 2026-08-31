<template>
  <div class="app-container">
    <el-form :model="queryParam.queryCond" ref="queryForm" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="场景编码" prop="sceneCode">
        <el-input v-model="queryParam.queryCond.sceneCode" placeholder="请输入场景编码" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="场景名称" prop="sceneName">
        <el-input v-model="queryParam.queryCond.sceneName" placeholder="请输入场景名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="场景状态" prop="sceneStatus">
        <el-select v-model="queryParam.queryCond.sceneStatus" placeholder="场景状态" clearable size="small">
          <el-option v-for="item in AgileSwitchStatus.getList()" :key="item.value"
                     :label="item.label" :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerm="['drools:scene:add']">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPerm="['drools:scene:edit']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete"
                   v-hasPerm="['drools:scene:delete']">
          删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getDroolsSceneList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sceneList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="场景编码" align="center" prop="sceneCode"/>
      <el-table-column label="场景名称" align="center" prop="sceneName"/>
      <el-table-column label="场景状态" align="center" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.sceneStatus" :active-value="AgileSwitchStatus.ENABLE"
                     :inactive-value="AgileSwitchStatus.DISABLE"
                     @change="handleStatusChange(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-caret-right" @click="handleExecute(scope.row)"
                     v-hasPerm="['drools:scene:execute']">
            执行
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPerm="['drools:scene:edit']">
            修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPerm="['drools:scene:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getDroolsSceneList"
    />

    <!-- 添加或修改场景对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="场景名称" prop="sceneName">
          <el-input v-model="form.sceneName" placeholder="请输入场景名称"/>
        </el-form-item>
        <el-form-item label="场景编码" prop="sceneCode">
          <el-input v-model="form.sceneCode" placeholder="请输入编码名称"/>
        </el-form-item>
        <el-form-item label="绑定规则" prop="modelIdList">
          <el-select v-model="form.ruleIdList" multiple placeholder="请选择">
            <el-option v-for="item in ruleList.filter(item=>item.ruleStatus===AgileSwitchStatus.ENABLE)" :key="item.id"
                       :label="item.ruleName" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="场景描述" prop="sceneDesc">
          <el-input v-model="form.sceneDesc" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    selectDroolsScenePage,
    detailDroolsScene,
    deleteDroolsScene,
    addDroolsScene,
    updateDroolsScene,
    changeDroolsSceneStatus
  } from '@/api/drools/scene'
  import { selectDroolsRuleList } from '@/api/drools/rule'

  export default {
    name: 'Scene',
    data() {
      return {
        // 遮罩层
        loading: true,
        // 已选择的列表
        selectRowList: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 规则列表
        ruleList: [],
        // 场景表格数据
        sceneList: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            sceneCode: undefined,
            sceneName: undefined,
            sceneStatus: undefined
          }
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          sceneName: [
            { required: true, message: '场景名称不能为空', trigger: 'blur' }
          ],
          sceneCode: [
            { required: true, message: '场景编码不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getDroolsRuleList()
      this.getDroolsSceneList()
    },
    methods: {
      /** 查询场景列表 */
      getDroolsSceneList() {
        this.loading = true
        selectDroolsScenePage(this.queryParam).then(response => {
          this.sceneList = response.data.records
          this.queryParam.pageTotal = response.data.pageTotal
          this.loading = false
        })
      },
      getDroolsRuleList() {
        selectDroolsRuleList().then(response => {
            this.ruleList = response.data
          }
        )
      },
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          sceneCode: undefined,
          sceneName: undefined,
          ruleIdList: [],
          sceneDesc: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getDroolsSceneList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.open = true
        this.title = '添加场景'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailDroolsScene(row.id).then(response => {
          this.form = response.data
          this.open = true
          this.title = '修改场景'
        })
      },
      // 角色状态修改
      handleStatusChange(row) {
        let text = this.AgileSwitchStatus.getLabel(row.sceneStatus)
        this.$confirm('确认要' + text + '"' + row.sceneName + '"场景吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const data = { sceneId: row.id, sceneStatus: row.sceneStatus }
          return changeDroolsSceneStatus(data)
        }).then(() => {
          this.messageSuccess(text + '成功')
        }).catch(function () {
          row.sceneStatus = row.sceneStatus === this.AgileSwitchStatus.DISABLE ? this.AgileSwitchStatus.ENABLE : this.AgileSwitchStatus.DISABLE
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateDroolsScene(this.form).then(response => {
                this.messageSuccess('修改成功')
                this.open = false
                this.getDroolsSceneList()
              })
            } else {
              addDroolsScene(this.form).then(response => {
                this.messageSuccess('新增成功')
                this.open = false
                this.getDroolsSceneList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除场景编号为"' + row.sceneName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteDroolsScene(row.id)
        }).then(() => {
          this.getDroolsSceneList()
          this.messageSuccess('删除成功')
        })
      },
      handleExecute(row) {
        this.$router.push('/drools/scene/execute/' + row.id)
      }
    }
  }
</script>
