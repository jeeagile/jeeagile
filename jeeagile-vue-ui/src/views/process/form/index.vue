<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="表单编码" prop="formCode">
        <el-input v-model="queryParam.queryCond.formCode" placeholder="请输入表单编码" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="表单名称" prop="formName">
        <el-input v-model="queryParam.queryCond.formName" placeholder="请输入表单名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="formStatus">
        <el-select v-model="queryParam.queryCond.formStatus" placeholder="表单状态" clearable size="small">
          <el-option v-for="formStatusOption in formStatusOptionList" :key="formStatusOption.dictValue"
                     :label="formStatusOption.dictLabel" :value="formStatusOption.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerm="['process:form:add']">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPerm="['process:form:update']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete"
                   v-hasPerm="['process:form:delete']">
          删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getProcessFormList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="formList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="表单编码" align="center" prop="formCode"/>
      <el-table-column label="表单名称" align="center" prop="formName"/>
      <el-table-column label="状态" align="center" prop="formStatus" :formatter="formStatusFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300px">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPerm="['process:form:update']">
            修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleFormView(scope.row)"
                     v-hasPerm="['process:form:view']">
            预览
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-s-custom" @click="handleFormDesigner(scope.row)"
                     v-hasPerm="['process:form:designer']">
            设计
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPerm="['process:form:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getProcessFormList"/>

    <!-- 添加或修改表单对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="表单编码" prop="formCode">
          <el-input v-model="form.formCode" placeholder="请输入编码名称"/>
        </el-form-item>
        <el-form-item label="表单名称" prop="formName">
          <el-input v-model="form.formName" placeholder="请输入表单名称"/>
        </el-form-item>
        <el-form-item label="表单状态" prop="formStatus">
          <el-radio-group v-model="form.formStatus">
            <el-radio v-for="formStatusOption in formStatusOptionList" :key="formStatusOption.dictValue"
                      :label="formStatusOption.dictValue">
              {{ formStatusOption.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="表单预览" :visible.sync="parserOpen" width="50%" append-to-body>
      <div class="test-form">
        <form-parser :key="new Date().getTime()" :form-conf="parserForm" @submit="submitFormData"/>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    selectProcessFormPage,
    detailProcessForm,
    deleteProcessForm,
    addProcessForm,
    updateProcessForm
  } from '@/api/process/form'
  import FormParser from '@/components/FormDesigner/parser/Parser'

  export default {
    name: 'ProcessForm',
    components: { FormParser },
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
        // 表单表格数据
        formList: [],
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        openDialog: false,
        // 状态数据字典
        formStatusOptionList: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            formCode: undefined,
            formName: undefined,
            formStatus: undefined
          }
        },
        // 表单参数
        form: {},
        parserOpen: false,
        parserForm: {
          fields: []
        },
        // 表单校验
        rules: {
          formName: [
            { required: true, message: '表单名称不能为空', trigger: 'blur' }
          ],
          formCode: [
            { required: true, message: '表单编码不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getProcessFormList()
      this.getSysDictDataList('sys_normal_disable').then(response => {
        this.formStatusOptionList = response.data
      })
    },
    methods: {
      /** 查询表单列表 */
      getProcessFormList() {
        this.loading = true
        selectProcessFormPage(this.queryParam).then(response => {
          this.queryParam.pageTotal = response.data.pageTotal
          this.formList = response.data.recordList
          this.loading = false
        })
      },
      /** 表单状态字典翻译 */
      formStatusFormat(row, column) {
        return this.handleDictLabel(this.formStatusOptionList, row.formStatus)
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
          formCode: undefined,
          formName: undefined,
          formStatus: '0',
          remark: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getProcessFormList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 多选框选中数据 */
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.openDialog = true
        this.dialogTitle = '添加表单'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailProcessForm(row.id).then(response => {
          this.form = response.data
          this.openDialog = true
          this.dialogTitle = '修改表单'
        })
      },
      handleFormView(row) {
        detailProcessForm(row.id).then(response => {
          this.$nextTick(() => {
            if (response.data.formConf && response.data.formFields) {
              this.parserForm = {
                fields: JSON.parse(response.data.formFields),
                ...JSON.parse(response.data.formConf)
              }
            }
            this.parserOpen = true
          })
        })
      },
      /** 流程设计操作 */
      handleFormDesigner(row) {
        this.$router.push('/process/form/designer/' + row.id)
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateProcessForm(this.form).then(() => {
                this.messageSuccess('修改成功')
                this.openDialog = false
                this.getProcessFormList()
              })
            } else {
              addProcessForm(this.form).then(() => {
                this.messageSuccess('新增成功')
                this.openDialog = false
                this.getProcessFormList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除表单名称为"' + row.formName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteProcessForm(row.id)
        }).then(() => {
          this.getProcessFormList()
          this.messageSuccess('删除成功')
        })
      },
      submitFormData(data) {
        alert(JSON.stringify(data))
      }
    }
  }
</script>
