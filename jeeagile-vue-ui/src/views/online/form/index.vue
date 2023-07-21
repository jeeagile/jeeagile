<template>
  <div class="app-container">
    <div v-if="!openForm">
      <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="表单编码" prop="formCode">
          <el-input v-model="queryParam.queryCond.formCode" placeholder="请输入表单编码" clearable size="small"
                    @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item label="表单名称" prop="formName">
          <el-input v-model="queryParam.queryCond.formName" placeholder="请输入表单名称" clearable size="small"
                    @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item label="表单类型" prop="formType">
          <el-select v-model="queryParam.queryCond.formType" placeholder="表单类型" clearable size="small">
            <el-option v-for="option in OnlineFormType.getList()" :key="option.key" :label="option.label"
                       :value="option.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="发布状态" prop="publishStatus">
          <el-select v-model="queryParam.queryCond.publishStatus" placeholder="发布状态" clearable size="small">
            <el-option v-for="option in SysPublishStatus.getList()" :key="option.key" :label="option.label"
                       :value="option.value"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddForm"
                     v-hasPerm="['online:form:add']">
            新增
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdateForm"
                     v-hasPerm="['online:form:update']">
            修改
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleDeleteForm"
                     v-hasPerm="['online:form:delete']">
            删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExportForm"
                     v-hasPerm="['online:form:export']">
            导出
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getOnlineFormList"></right-toolbar>
      </el-row>
      <el-table v-loading="loading" :data="onlineFormList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="表单编码" align="center" prop="formCode"/>
        <el-table-column label="表单名称" align="center" prop="formName" :show-overflow-tooltip="true"/>
        <el-table-column label="表单类型" align="center" prop="formType">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getOnlineFormTypeTag(scope.row.formType)">
              {{OnlineFormType.getLabel(scope.row.formType)}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="表单状态" align="center" prop="formStatus">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getOnlineFormStatusTag(scope.row.formStatus)">
              {{OnlineFormStatus.getLabel(scope.row.formStatus)}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发布状态" align="center" prop="publishStatus">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.publishStatus" active-value="01" inactive-value="02"
                       @change="handlePublishStatus(scope.row)"/>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdateForm(scope.row)"
                       v-hasPerm="['online:form:update']">
              修改
            </el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteForm(scope.row)"
                       v-hasPerm="['online:form:delete']">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                  :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                  @pagination="getOnlineFormList"/>
    </div>
    <div v-else>
      <el-header style="height: 50px">
        <el-row>
          <el-col :span="18">
            <el-steps :active="activeStep" finish-status="success" simple>
              <el-step title="基础信息"></el-step>
              <el-step title="数据模型"></el-step>
              <el-step title="页面设计"></el-step>
            </el-steps>
          </el-col>
          <el-col v-if="columnVisible==false && designerVisible==false" :span="6"
                  style="margin-top: 8px;text-align: right">
            <el-button size="small" @click="handlePrevStep" :disabled="activeStep === 0">上一步</el-button>
            <el-button size="small" @click="handleNextStep" :disabled="activeStep === 2">下一步</el-button>
            <el-button size="small" type="primary" @click="handleExitForm">退出</el-button>
          </el-col>
        </el-row>
      </el-header>
      <div class="online-form">
        <el-row type="flex" justify="center" align="middle" style="height: 100%;">
          <el-col v-if="activeStep === 0" class="online-form-el-col">
            <el-form ref="onlineForm" :model="onlineForm" :rules="onlineFormRules" label-width="100px">
              <el-form-item label="表单编码" prop="formCode">
                <el-input v-model="onlineForm.formCode" placeholder="请输入表单编码"/>
              </el-form-item>
              <el-form-item label="表单名称" prop="formName">
                <el-input v-model="onlineForm.formName" placeholder="请输入表单名称"/>
              </el-form-item>
              <el-form-item label="表单类型" prop="formType">
                <el-select v-model="onlineForm.formType" placeholder="表单类型" clearable style="width: 100%">
                  <el-option
                    v-for="formTypeOption in OnlineFormType.getList()"
                    :key="formTypeOption.key"
                    :label="formTypeOption.label"
                    :value="formTypeOption.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="备注:" prop="remark">
                <el-input v-model="onlineForm.remark" type="textarea" placeholder="请输入内容"></el-input>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
  import {
    selectFormPage,
    detailForm,
    deleteForm,
    addForm,
    updateForm,
    exportForm,
    changePublishStatus
  } from '@/api/online/form'
  import { SysPublishStatus } from '../../../components/AgileDict/system'
  import { OnlineFormStatus } from '../../../components/AgileDict/online'

  export default {
    name: 'Form',
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
        // 在线表单表格数据
        onlineFormList: [],
        // 查询在线表单
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            formCode: undefined,
            formName: undefined,
            formType: undefined,
            publishType: undefined,
            excelName: '在线表单'
          }
        },
        // 打开表单页面标识
        openForm: false,
        // 表单执行步骤
        activeStep: 0,
        // 字段管理
        columnVisible: false,
        // 页面设计
        designerVisible: false,
        // 在线表单参数
        onlineForm: {},
        // 表单校验
        onlineFormRules: {
          formCode: [
            { required: true, message: '表单编码不能为空', trigger: 'blur' }
          ],
          formName: [
            { required: true, message: '参数名称不能为空', trigger: 'blur' }
          ],
          formType: [
            { required: true, message: '表单类型不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getOnlineFormList()
    },
    methods: {
      /** 查询参数列表 */
      getOnlineFormList() {
        this.loading = true
        selectFormPage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.onlineFormList = response.data.records
            this.loading = false
          }
        )
      },
      // 取消按钮
      cancel() {
        this.openDialog = false
        this.reset()
      },
      // 表单重置
      resetOnlineForm() {
        this.onlineForm = {
          id: undefined,
          formCode: undefined,
          formName: undefined,
          formType: undefined,
          formStatus: OnlineFormStatus.FORM_BASIC,
          publishStatus: SysPublishStatus.UNPUBLISHED,
          remark: undefined
        }
        this.activeStep = 0
        this.columnVisible = false
        this.designerVisible = false
        this.resetForm('onlineForm')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getOnlineFormList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 设置表单类型标签 */
      getOnlineFormTypeTag(formType) {
        switch (formType) {
        case this.OnlineFormType.BUSINESS:
          return 'success'
        case this.OnlineDictType.FLOW:
          return 'info'
        default:
          return 'info'
        }
      },
      /** 设置表单状态标签 */
      getOnlineFormStatusTag(formStatus) {
        switch (formStatus) {
        case this.OnlineFormStatus.FORM_BASIC:
          return 'warning'
        case this.OnlineFormStatus.DATA_MODEL:
          return 'primary'
        case this.OnlineFormStatus.PAGE_DESIGN:
          return 'success'
        }
      },
      /** 修改发布状态 */
      handlePublishStatus(row) {
        if (row.publishStatus === SysPublishStatus.PUBLISHED && row.formStatus !== OnlineFormStatus.PAGE_DESIGN) {
          this.messageWarning('表单还处于' + OnlineFormStatus.getLabel(row.formStatus) + '不能进行发布！')
          row.publishStatus = row.publishStatus === SysPublishStatus.PUBLISHED ? SysPublishStatus.UNPUBLISHED : SysPublishStatus.PUBLISHED
          return
        }
        let text = row.publishStatus === SysPublishStatus.PUBLISHED ? '发布' : '停用'
        this.$confirm('确认要' + text + '' + row.formName + '表单吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const data = { id: row.id, publishStatus: row.publishStatus }
          return changePublishStatus(data)
        }).then(() => {
          this.messageSuccess(text + '成功')
        }).catch(() => {
          row.publishStatus = row.publishStatus === SysPublishStatus.PUBLISHED ? SysPublishStatus.UNPUBLISHED : SysPublishStatus.PUBLISHED
        })
      },
      /** 新增表单按钮操作 */
      handleAddForm() {
        this.resetOnlineForm()
        this.openForm = true
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 修改按钮操作 */
      handleUpdateForm(row) {
        this.resetOnlineForm()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailForm(row.id).then(response => {
          this.onlineForm = response.data
          this.openForm = true
        })
      },
      /** 上一步 */
      handlePrevStep() {
        this.activeStep = this.activeStep - 1
        if (this.activeStep == 1) {
          this.columnVisible = false
        }
      },
      /** 下一步 */
      handleNextStep() {
        if (this.activeStep == 0) {
          this.columnVisible = false
          this.submitOnlineForm()
        }
        this.activeStep = this.activeStep + 1
        if (this.activeStep == 2) {
          this.designerVisible = false
        }
      },
      handleExitForm() {
        this.openForm = false
        this.getOnlineFormList()
      },
      /** 提交按钮 */
      submitOnlineForm: function () {
        this.$refs.onlineForm.validate(valid => {
          if (valid) {
            if (this.onlineForm.id != undefined) {
              updateForm(this.onlineForm).then(() => {
                this.messageSuccess('表单基础信息修改成功')
              })
            } else {
              addForm(this.onlineForm).then(() => {
                this.messageSuccess('新增表单基础信息成功')
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDeleteForm(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除参数名称为"' + row.formName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteForm(row.id)
        }).then(() => {
          this.getOnlineFormList()
          this.messageSuccess('删除成功')
        })
      },
      /** 导出按钮操作 */
      handleExportForm() {
        this.$confirm('请确认是否导出字段类型数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return exportForm(this.queryParam.queryCond)
        })
      }
    }
  }
</script>
<style lang='scss'>
  .online-form {
    background: #EBEEF5;
    padding: 10px;
    height: calc(100vh - 165px);
  }

  .online-form-el-col {
    width: 60%;
    background: white;
    padding: 20px;
    position: absolute;
  }
</style>
