<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParam.queryCond" :inline="true" label-width="80px">
      <el-form-item label="规则编码" prop="ruleCode">
        <el-input v-model="queryParam.queryCond.ruleCode" placeholder="请输入规则编码" clearable size="small"
                  @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="规则名称" prop="ruleName">
        <el-input v-model="queryParam.queryCond.ruleName" placeholder="请输入规则名称" clearable size="small"
                  @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="规则状态" prop="ruleStatus">
        <el-select v-model="queryParam.queryCond.ruleStatus" placeholder="规则状态" clearable size="small">
          <el-option v-for="item in AgileSwitchStatus.getList()" :key="item.value"
                     :label="item.label" :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-hasPerm="['system:rule:add']" type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPerm="['system:rule:edit']" type="success" icon="el-icon-edit" size="mini" :disabled="single"
                   @click="handleUpdate"
        >
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPerm="['system:rule:delete']" type="danger" icon="el-icon-delete" size="mini" :disabled="single"
                   @click="handleDelete"
        >
          删除
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getDroolsRuleList"/>
    </el-row>

    <el-table v-loading="loading" :data="ruleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="规则编码" align="center" prop="ruleCode"/>
      <el-table-column label="规则名称" align="center" prop="ruleName"/>
      <el-table-column label="规则包名" align="center" prop="rulePackage"/>
      <el-table-column label="规则类型" align="center" prop="ruleType">
        <template slot-scope="scope">
          {{ DroolsRuleType.getLabel(scope.row.ruleType) }}
        </template>
      </el-table-column>
      <el-table-column label="规则状态" align="center" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.ruleStatus" :active-value="AgileSwitchStatus.ENABLE"
                     :inactive-value="AgileSwitchStatus.DISABLE"
                     @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-hasPerm="['system:rule:designer']" size="mini" type="text" icon="el-icon-s-custom"
                     @click="handleDesigner(scope.row)"
          >
            规则
          </el-button>
          <el-button v-hasPerm="['system:rule:edit']" size="mini" type="text" icon="el-icon-edit"
                     @click="handleUpdate(scope.row)"
          >
            修改
          </el-button>
          <el-button v-hasPerm="['system:rule:delete']" size="mini" type="text" icon="el-icon-delete"
                     @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getDroolsRuleList"
    />

    <!-- 添加或修改规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="form.ruleName" placeholder="请输入规则名称"/>
        </el-form-item>
        <el-form-item label="规则编码" prop="ruleCode">
          <el-input v-model="form.ruleCode" placeholder="请输入编码名称"/>
        </el-form-item>
        <el-form-item label="规则包名" prop="ruleCode">
          <el-input v-model="form.rulePackage" placeholder="请输入规则包名"/>
        </el-form-item>
        <el-form-item label="规则类型" prop="ruleType">
          <el-select v-model="form.ruleType" placeholder="规则类型" clearable size="small">
            <el-option v-for="item in DroolsRuleType.getList()" :key="item.value"
                       :label="item.label" :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="规则对象" prop="modelIdList">
          <el-select v-model="form.modelIdList" multiple placeholder="请选择">
            <el-option v-for="item in modelList.filter(item=>item.modelStatus===AgileSwitchStatus.ENABLE)"
                       :key="item.id"
                       :label="item.modelLabel" :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="规则描述" prop="ruleDesc">
          <el-input v-model="form.ruleDesc" type="textarea" placeholder="请输入内容"/>
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
  import { selectDroolsRulePage, detailDroolsRule, deleteDroolsRule, addDroolsRule, updateDroolsRule, changeDroolsRuleStatus } from '@/api/drools/rule'
  import { selectDroolsModelList } from '@/api/drools/model'

  export default {
    name: 'Rule',
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
        // 规则对象列表
        modelList: [],
        // 规则表格数据
        ruleList: [],
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
            ruleCode: undefined,
            ruleName: undefined,
            ruleStatus: undefined
          }
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          ruleName: [
            { required: true, message: '规则名称不能为空', trigger: 'blur' }
          ],
          ruleCode: [
            { required: true, message: '规则编码不能为空', trigger: 'blur' }
          ],
          rulePackage: [
            { required: true, message: '规则包名不能为空', trigger: 'blur' },
            { pattern: /[a-zA-Z][\-_.0-9_a-zA-Z$]*/, message: '请以字母开头' }
          ],
          ruleType: [
            { required: true, message: '请选择规则类型', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getDroolsModelList()
      this.getDroolsRuleList()
    },
    methods: {
      /** 查询规则列表 */
      getDroolsRuleList() {
        this.loading = true
        selectDroolsRulePage(this.queryParam).then(response => {
          this.ruleList = (response.data && response.data.records) || []
          this.queryParam.pageTotal = (response.data && response.data.pageTotal) || 0
          this.loading = false
        })
      },
      getDroolsModelList() {
        selectDroolsModelList().then(response => {
            this.modelList = response.data
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
          ruleCode: undefined,
          ruleName: undefined,
          rulePackage: undefined,
          ruleType: this.DroolsRuleType.DRL_FILE,
          modelIdList: [],
          ruleDesc: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getDroolsRuleList()
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
        this.title = '添加规则'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailDroolsRule(row.id).then(response => {
          this.form = response.data
          this.open = true
          this.title = '修改规则'
        })
      },
      // 规则状态修改
      handleStatusChange(row) {
        let text = this.AgileSwitchStatus.getLabel(row.ruleStatus)
        this.$confirm('确认要' + text + '"' + row.ruleName + '"规则吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const data = { ruleId: row.id, ruleStatus: row.ruleStatus }
          return changeDroolsRuleStatus(data)
        }).then(() => {
          this.messageSuccess(text + '成功')
        }).catch(function () {
          row.ruleStatus = row.ruleStatus === this.AgileSwitchStatus.DISABLE ? this.AgileSwitchStatus.ENABLE : this.AgileSwitchStatus.DISABLE
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateDroolsRule(this.form).then(response => {
                this.messageSuccess('修改成功')
                this.open = false
                this.getDroolsRuleList()
              })
            } else {
              addDroolsRule(this.form).then(response => {
                this.messageSuccess('新增成功')
                this.open = false
                this.getDroolsRuleList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除规则编号为"' + row.ruleName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteDroolsRule(row.id)
        }).then(() => {
          this.getDroolsRuleList()
          this.messageSuccess('删除成功')
        })
      },
      handleDesigner(row) {
        this.$router.push('/drools/rule/designer/' + row.id).catch(() => {})
      }
    }
  }
</script>
