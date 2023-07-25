<template>
  <div class="app-container">
    <div v-if="!openOnlineForm">
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
      <el-table v-loading="formLoading" :data="onlineFormList" @selection-change="handleSelectionChange">
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
        <el-row type="flex" justify="center" style="height: 100%;">
          <el-col v-if="activeStep === 0" class="form-basic">
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
          <el-col v-if="activeStep === 1" class="data-model">
            <template v-if="columnVisible==false">
              <el-table v-loading="formLoading" :data="onlineTableList"
                        header-cell-class-name="table-header-gray" key="formTable">
                <el-table-column label="数据表名" prop="tableName"/>
                <el-table-column label="数据表描述" prop="tableLabel" :show-overflow-tooltip="true"/>
                <el-table-column label="数据表类型" prop="relationType">
                  <template slot-scope="scope">
                    <el-tag size="mini" :type="getOnlineTableTypeTag(scope.row.tableType)">
                      {{OnlineTableType.getLabel(scope.row.tableType)|| '未知类型'}}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="主表关联字段" prop="masterColumnName"/>
                <el-table-column label="从表关联字段" prop="slaveColumnName"/>
                <el-table-column label="操作" width="200px">
                  <template slot-scope="scope">
                    <el-button size="mini" type="text" @click="editOnlineTable(scope.row)">
                      编辑
                    </el-button>
                    <el-button size="mini" type="text" @click="editOnlineTableColumn(scope.row)">
                      字段管理
                    </el-button>
                    <el-button size="mini" type="text" @click="deleteOnlineTable(scope.row)">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-button style="width: 100%;margin-top: 10px" icon="el-icon-plus" @click="addOnlineTable">新增数据表
              </el-button>
              <el-dialog :title="onlineTableTitle" :visible.sync="onlineTableDialog" width="700px" append-to-body>
                <el-form ref="onlineTableForm" :model="onlineTable" :rules="onlineTableRules"
                         label-width="120px">
                </el-form>
                <div slot="footer" class="dialog-footer">
                  <el-button type="primary" @click="submitOnlineTable">确 定</el-button>
                  <el-button @click="onlineTableDialog=false">取 消</el-button>
                </div>
              </el-dialog>
            </template>
            <template v-if="columnVisible">
              <table-column :table-id="editColumnTableId" :table-name="editColumnTableName"
                            @close="handleTableColumnClose"></table-column>
            </template>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
  import {
    selectOnlineFormPage,
    detailOnlineForm,
    deleteOnlineForm,
    addOnlineForm,
    updateOnlineForm,
    exportOnlineForm,
    publishOnlineForm
  } from '@/api/online/form'
  import {
    selectOnlineTableList,
    detailOnlineTable,
    deleteOnlineTable,
    addOnlineTable,
    updateOnlineTable
  } from '@/api/online/table'
  import { SysPublishStatus } from '@/components/AgileDict/system'
  import { OnlineFormStatus } from '@/components/AgileDict/online'
  import { OnlineTableType } from '../../../components/AgileDict/online'

  export default {
    name: 'Form',
    data() {
      return {
        // 表单列表遮罩层
        formLoading: true,
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
        openOnlineForm: false,
        // 表单执行步骤
        activeStep: 0,
        // 字段管理
        columnVisible: false,
        // 页面设计
        designerVisible: false,
        // 在线表单参数
        onlineForm: {},
        // 在线表单参数字符串
        onlineFormStr: undefined,
        // 表单校验
        onlineFormRules: {
          formCode: [
            { required: true, message: '表单编码不能为空', trigger: 'blur' }
          ],
          formName: [
            { required: true, message: '表单不能为空', trigger: 'blur' }
          ],
          formType: [
            { required: true, message: '表单类型不能为空', trigger: 'blur' }
          ]
        },
        // 数据模型信息
        onlineTable: {},
        // 数据表列表
        onlineTableList: [],
        // 数据表标题
        onlineTableTitle: '新增数据表',
        // 打开数据表模型编辑标识
        onlineTableDialog: false,
        // 数据模型表单校验
        onlineTableRules: {
          tableName: [
            { required: true, message: '表单编码不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getOnlineFormList()
    },
    methods: {
      /** 查询表单列表 */
      getOnlineFormList() {
        this.formLoading = true
        selectOnlineFormPage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.onlineFormList = response.data.records
            this.formLoading = false
          }
        )
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
        default:
          return 'warning'
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
          return publishOnlineForm(data)
        }).then(() => {
          this.messageSuccess(text + '成功')
        }).catch(() => {
          row.publishStatus = row.publishStatus === SysPublishStatus.PUBLISHED ? SysPublishStatus.UNPUBLISHED : SysPublishStatus.PUBLISHED
        })
      },
      /** 新增表单按钮操作 */
      handleAddForm() {
        this.resetOnlineForm()
        this.openOnlineForm = true
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
        detailOnlineForm(row.id).then(response => {
          this.onlineForm = response.data
          this.onlineFormStr = JSON.stringify(this.onlineForm)
          this.openOnlineForm = true
        })
      },
      /** 上一步 */
      handlePrevStep() {
        this.activeStep = this.activeStep - 1
        if (this.activeStep == 1) {
          this.columnVisible = false
          this.getOnlineTableList()
        }
      },
      /** 下一步 */
      handleNextStep() {
        if (this.activeStep == 0) {
          this.columnVisible = false
          // 判断是否修改过表单基础信息
          if (JSON.stringify(this.onlineForm) !== this.onlineFormStr) {
            this.onlineFormStr = JSON.stringify(this.onlineForm)
            this.submitOnlineForm()
          }
        }
        this.activeStep = this.activeStep + 1
        if (this.activeStep == 1) {
          this.columnVisible = false
          this.getOnlineTableList()
        }
        if (this.activeStep == 2) {
          this.designerVisible = false
        }
      },
      /** 退出表单编辑 */
      handleExitForm() {
        this.openOnlineForm = false
        this.getOnlineFormList()
      },
      /** 提交按钮 */
      submitOnlineForm: function () {
        this.$refs.onlineForm.validate(valid => {
          if (valid) {
            if (this.onlineForm.id != undefined) {
              updateOnlineForm(this.onlineForm).then(() => {
                this.messageSuccess('表单基础信息修改成功')
              })
            } else {
              addOnlineForm(this.onlineForm).then(() => {
                this.messageSuccess('新增表单基础信息成功')
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDeleteForm(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除表单名称为"' + row.formName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteOnlineForm(row.id)
        }).then(() => {
          this.getOnlineFormList()
          this.messageSuccess('删除成功')
        })
      },
      /** 查询表单列表 */
      getOnlineTableList() {
        this.formLoading = true
        this.resetOnlineTable()
        selectOnlineTableList(this.onlineTable).then(response => {
            this.onlineTableList = response.data
            this.formLoading = false
          }
        )
      },
      /** 设置表单类型标签 */
      getOnlineTableTypeTag(tableType) {
        switch (tableType) {
        case this.OnlineTableType.MASTER:
          return 'success'
        case this.OnlineTableType.ONE_TO_ONE:
          return 'primary'
        case this.OnlineTableType.ONE_TO_MANY:
          return 'warning'
        default:
          return 'error'
        }
      },
      // 表单重置
      resetOnlineTable() {
        this.onlineTable = {
          id: undefined,
          formId: this.onlineForm.id,
          tableName: undefined,
          tableType: undefined,
          tableLabel: undefined,
          masterColumnId: undefined,
          masterColumnName: undefined,
          slaveColumnId: undefined,
          slaveColumnName: undefined
        }
        this.resetForm('onlineTable')
      },
      /** 新增数据表 */
      addOnlineTable() {
        this.resetOnlineTable()
        if (!this.onlineTableList?.length > 0) {
          this.onlineTable.tableType = OnlineTableType.MASTER
        }
        this.onlineTableDialog = true
        this.onlineTableTitle = '新增数据表'
      },
      /** 编辑数据表 */
      editOnlineTable(row) {
        this.resetOnlineTable()
        detailOnlineTable(row.id).then(response => {
          this.onlineTable = response.data
          this.onlineTableDialog = true
          this.onlineTableTitle = '编辑数据表'
        })
      },
      /** 删除数据表 */
      deleteOnlineTable(row) {
        this.$confirm('是否确认删除数据表为"' + row.tableName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteOnlineTable(row.id)
        }).then(() => {
          this.getOnlineFormList()
          this.messageSuccess('删除成功')
        })
      },
      /** 提交数据表 */
      submitOnlineTable() {
        this.$refs.onlineTable.validate(valid => {
          if (valid) {
            if (this.onlineTable.id != undefined) {
              updateOnlineTable(this.onlineForm).then(() => {
                this.messageSuccess('数据表信息修改成功')
              })
            } else {
              addOnlineTable(this.onlineForm).then(() => {
                this.messageSuccess('新增数据表信息成功')
              })
            }
          }
        })
      },
      /** 编辑数据表字段 */
      editOnlineTableColumn() {
      },
      /** 导出按钮操作 */
      handleExportForm() {
        this.$confirm('请确认是否导出字段类型数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return exportOnlineForm(this.queryParam.queryCond)
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

    .form-basic {
      width: 60%;
      background: white;
      padding: 20px;
    }

    .data-model {
      width: 99%;
      background: white;
    }
  }


</style>
