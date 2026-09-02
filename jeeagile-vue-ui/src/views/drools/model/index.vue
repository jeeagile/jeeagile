<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="对象名称" prop="modelName">
        <el-input v-model="queryParam.queryCond.modelName" placeholder="请输入对象名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="对象标签" prop="modelLabel">
        <el-input v-model="queryParam.queryCond.modelLabel" placeholder="请输入对象标签" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerm="['drools:model:add']">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPerm="['drools:model:update']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete"
                   v-hasPerm="['drools:model:delete']">
          删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getModelList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="modelList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="对象名称" align="center" prop="modelName">
        <template slot-scope="scope">
          <router-link :to="'/drools/model/field/' + scope.row.id" class="link-type">
            <span>{{ scope.row.modelName }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="对象标签" align="center" prop="modelLabel"/>
      <el-table-column label="对象包名" align="center" prop="modelPackage"/>
      <el-table-column label="父级对象" align="center" prop="superModel"/>
      <el-table-column label="对象状态" align="center" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.modelStatus" :active-value="AgileSwitchStatus.ENABLE"
                     :inactive-value="AgileSwitchStatus.DISABLE"
                     @change="handleStatusChange(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-paddingEnum fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPerm="['drools:model:update']">
            修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPerm="['drools:model:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getModelList"/>

    <!-- 添加或修改规则引擎 数据对象对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="对象名称" prop="modelName">
          <el-input v-model="form.modelName" placeholder="请输入对象名称"/>
        </el-form-item>
        <el-form-item label="对象标签" prop="modelLabel">
          <el-input v-model="form.modelLabel" placeholder="请输入对象标签"/>
        </el-form-item>
        <el-form-item label="对象包名" prop="modelPackage">
          <el-input v-model="form.modelPackage" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="父级对象" prop="superModel">
          <el-input v-model="form.superModel" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="出入参标识" prop="input_output">
          <el-checkbox v-model="form.inputFlag" :true-label="AgileYesNo.YES" :false-label="AgileYesNo.NO">入参
          </el-checkbox>
          <el-checkbox v-model="form.outputFlag" :true-label="AgileYesNo.YES" :false-label="AgileYesNo.NO">出参
          </el-checkbox>
        </el-form-item>
        <el-form-item label="对象描述" prop="modelDesc">
          <el-input v-model="form.modelDesc" type="textarea" placeholder="请输入内容"/>
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
    selectDroolsModelPage,
    detailDroolsModel,
    addDroolsModel,
    updateDroolsModel,
    deleteDroolsModel,
    changeDroolsModelStatus
  } from '@/api/drools/model'

  export default {
    name: 'Model',
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
        // 规则引擎 数据对象表格数据
        modelList: [],
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        openDialog: false,
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            modelName: undefined,
            modelLabel: undefined
          }
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          modelName: [
            { required: true, message: '对象名称不能为空', trigger: 'blur' }
          ],
          modelLabel: [
            { required: true, message: '对象标签不能为空', trigger: 'blur' }
          ],
          modelPackage: [
            { required: true, message: '对象包名不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getModelList()
    },
    methods: {
      /** 查询规则引擎 数据对象列表 */
      getModelList() {
        this.loading = true
        selectDroolsModelPage(this.queryParam).then(response => {
          this.queryParam.pageTotal = (response.data && response.data.pageTotal) || 0
          this.modelList = (response.data && response.data.records) || []
          this.loading = false
        })
      },
      /** 取消按钮 */
      cancel() {
        this.openDialog = false
        this.reset()
      },
      /** 表单重置 */
      reset() {
        this.form = {
          id: undefined,
          modelName: undefined,
          modelLabel: undefined,
          modelType: this.DroolsModelType.DECLARE,
          modelPackage: undefined,
          superModel: undefined,
          inputFlag: this.AgileYesNo.NO,
          outputFlag: this.AgileYesNo.NO,
          modelDesc: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getModelList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 多选框选中数据 */
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.openDialog = true
        this.dialogTitle = '添加规则引擎 数据对象'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailDroolsModel(row.id).then(response => {
          this.form = response.data
          this.openDialog = true
          this.dialogTitle = '修改规则引擎 数据对象'
        })
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateDroolsModel(this.form).then(response => {
                this.messageSuccess('修改成功')
                this.openDialog = false
                this.getModelList()
              })
            } else {
              addDroolsModel(this.form).then(response => {
                this.messageSuccess('新增成功')
                this.openDialog = false
                this.getModelList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('请确认是否删除对象名称为《' + row.modelName + '》的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteDroolsModel(row.id)
        }).then(() => {
          this.getModelList()
          this.messageSuccess('删除成功')
        })
      },
      /** 对象状态修改 */
      handleStatusChange(row) {
        let text = this.AgileSwitchStatus.getLabel(row.modelStatus)
        this.$confirm('确认要' + text + '"' + row.modelName + '"对象吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const data = { modelId: row.id, modelStatus: row.modelStatus }
          return changeDroolsModelStatus(data)
        }).then(() => {
          this.messageSuccess(text + '成功')
        }).catch(function () {
          row.modelStatus = row.modelStatus === this.AgileSwitchStatus.DISABLE ? this.AgileSwitchStatus.ENABLE : this.AgileSwitchStatus.DISABLE
        })
      }
    }
  }
</script>
