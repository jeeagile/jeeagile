<template>
  <div v-if="!openOnlineForm" class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParam" :inline="true" label-width="68px">
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
        <el-button type="primary" icon="el-icon-plus" size="mini" v-hasPerm="['online:form:add']"
                   @click="handleAddForm">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" v-hasPerm="['online:form:update']"
                   @click="handleUpdateForm">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" v-hasPerm="['online:form:delete']"
                   @click="handleDeleteForm">
          删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" v-hasPerm="['online:form:export']"
                   @click="handleExportForm">
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
          <el-tag size="mini" :type="OnlineFormType.getTag(scope.row.formType)">
            {{OnlineFormType.getLabel(scope.row.formType)}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="表单状态" align="center" prop="formStatus">
        <template slot-scope="scope">
          <el-tag size="mini" :type="OnlineFormStatus.getTag(scope.row.formStatus)">
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
  <div v-else style="padding: 5px">
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
            <el-form-item label="备注" prop="remark">
              <el-input v-model="onlineForm.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col v-if="activeStep === 1" class="data-model">
          <template v-if="columnVisible==false">
            <el-table v-loading="formLoading" :data="onlineTableList"
                      header-cell-class-name="table-header-gray" key="formTable">
              <el-table-column label="数据表名" prop="tableName" align="center" width="200px"
                               :show-overflow-tooltip="true"/>
              <el-table-column label="数据表描述" prop="tableLabel" align="center" :show-overflow-tooltip="true"/>
              <el-table-column label="数据表类型" prop="relationType" align="center">
                <template slot-scope="scope">
                  <el-tag size="mini" :type="OnlineTableType.getTag(scope.row.tableType)">
                    {{OnlineTableType.getLabel(scope.row.tableType)|| '未知类型'}}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="主表关联字段" prop="masterColumnName" align="center"/>
              <el-table-column label="从表关联字段" prop="slaveColumnName" align="center"/>
              <el-table-column label="操作" width="200px" align="center">
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
              <el-form ref="onlineTableForm" :model="onlineTable" :rules="onlineTableRules" label-width="120px">
                <template v-if="onlineTable.tableType==OnlineTableType.MASTER">
                  <el-form-item label="数据主表" prop="tableName">
                    <el-select v-model="onlineTable.tableName" placeholder="数据主表" clearable style="width: 100%"
                               @change="handleOnlineTable">
                      <el-option
                        v-for="tableOption in jdbcTableList"
                        :key="tableOption.tableName"
                        :label="`${tableOption.tableComment}|${tableOption.tableName}`"
                        :value="tableOption.tableName"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="数据表描述" prop="tableLabel">
                    <el-input v-model="onlineTable.tableLabel" placeholder="请输入数据表描述"/>
                  </el-form-item>
                  <el-form-item label="数据模型标识" prop="modelName">
                    <el-input v-model="onlineTable.modelName" placeholder="请输入数据模型标识"/>
                  </el-form-item>
                </template>
                <template v-else>
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="数据从表" prop="tableName">
                        <el-select v-model="onlineTable.tableName" placeholder="数据从表" clearable style="width: 100%"
                                   @change="handleOnlineTable">
                          <el-option
                            v-for="tableOption in jdbcTableList"
                            :key="tableOption.tableName"
                            :label="`${tableOption.tableComment}|${tableOption.tableName}`"
                            :value="tableOption.tableName"
                          />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="从表类型" prop="tableType">
                        <el-select v-model="onlineTable.tableType" size="small" style="width: 100%">
                          <el-option
                            v-for="tableTypeOption in OnlineTableType.getList().filter(item => item.value!==OnlineTableType.MASTER)"
                            :key="tableTypeOption.value"
                            :label="tableTypeOption.label"
                            :value="tableTypeOption.value"
                          />
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="数据表描述" prop="tableLabel">
                        <el-input v-model="onlineTable.tableLabel" placeholder="请输入数据表描述"/>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="数据模型标识" prop="modelName">
                        <el-input v-model="onlineTable.modelName" placeholder="请输入数据模型标识"/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="主表关联字段" prop="masterColumnId">
                        <el-select v-model="onlineTable.masterColumnId" size="small" style="width: 100%">
                          <el-option
                            v-for="tableColumnOption in masterTableColumnList"
                            :key="tableColumnOption.id"
                            :label="`${tableColumnOption.columnComment}|${tableColumnOption.columnName}`"
                            :value="tableColumnOption.id"
                          />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="从表关联字段" prop="slaveColumnName">
                        <el-select v-model="onlineTable.slaveColumnName" size="small" style="width: 100%">
                          <el-option
                            v-for="tableColumnOption in slaveTableColumnList"
                            :key="tableColumnOption.columnName"
                            :label="`${tableColumnOption.columnComment}|${tableColumnOption.columnName}`"
                            :value="tableColumnOption.columnName"
                          />
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="是否级联删除" prop="cascadeDelete">
                        <el-switch
                          v-model="onlineTable.cascadeDelete"
                          active-value="1"
                          inactive-value="0"
                        ></el-switch>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="是否左关联" prop="leftJoin">
                        <el-switch
                          v-model="onlineTable.leftJoin"
                          active-value="1"
                          inactive-value="0"
                        ></el-switch>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </template>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitOnlineTable">确 定</el-button>
                <el-button @click="onlineTableDialog=false">取 消</el-button>
              </div>
            </el-dialog>
          </template>
          <template v-if="columnVisible">
            <table-column :form-id="onlineFormId" :table-id="onlineTableId" :table-name="onlineTableName"
                          :table-label="onlineTableLabel"
                          @close="columnVisible=false"/>
          </template>
        </el-col>
        <el-col v-if="activeStep === 2" class="page-designer">
          <template v-if="designerVisible==false">
            <el-table v-loading="formLoading" :data="onlinePageList" header-cell-class-name="table-header-gray"
                      key="onlinePage">
              <el-table-column label="页面编码" prop="pageCode" :show-overflow-tooltip="true" align="center"/>
              <el-table-column label="页面名称" prop="pageName" :show-overflow-tooltip="true" align="center"/>
              <el-table-column label="页面分类" prop="pageCategory" align="center">
                <template slot-scope="scope">
                  <el-tag size="mini" :type="OnlinePageKind.getTag(scope.row.pageKind)">
                    {{OnlinePageKind.getLabel(scope.row.pageKind)|| '未知类型'}}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="页面类型" prop="pageType" align="center">
                <template slot-scope="scope">
                  <el-tag size="mini" :type="OnlinePageType.getTag(scope.row.pageType)">
                    {{OnlinePageType.getLabel(scope.row.pageType)|| '未知类型'}}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="220px" align="center">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" @click="designerOnlinePage(scope.row)">
                    页面设计
                  </el-button>
                  <el-button size="mini" type="text" @click="editOnlinePage(scope.row)">
                    编辑页面
                  </el-button>
                  <el-button size="mini" type="text" @click="deleteOnlinePage(scope.row)">
                    删除页面
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-button style="width: 100%;margin-top: 10px" icon="el-icon-plus" @click="addOnlinePage">添加表单页面
            </el-button>
            <el-dialog :title="onlinePageTitle" :visible.sync="onlinePageDialog" width="500px" append-to-body>
              <el-form ref="onlinePageForm" :model="onlinePage" :rules="onlinePageRules" label-width="100px">
                <el-form-item label="页面编码" prop="pageCode">
                  <el-input v-model="onlinePage.pageCode" placeholder="请输入页面编码"/>
                </el-form-item>
                <el-form-item label="页面名称" prop="pageName">
                  <el-input v-model="onlinePage.pageName" placeholder="请输入页面编码"/>
                </el-form-item>
                <el-form-item label="页面类别" prop="pageKind">
                  <el-select v-model="onlinePage.pageKind" placeholder="请选择页面类别" size="small" style="width: 100%"
                             :disabled="this.onlineForm.formType===OnlineFormType.FLOW">
                    <el-option
                      v-for="item in OnlinePageKind.getList()"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="页面类型" prop="pageType">
                  <el-select v-model="onlinePage.pageType" placeholder="请选择页面类型" size="small" style="width: 100%">
                    <el-option
                      v-for="item in filterPageTypeList"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="页面数据" prop="tableId">
                  <el-select class="input-item" v-model="onlinePage.tableId" :clearable="true" placeholder="页面数据"
                             style="width: 100%">
                    <el-option v-for="item in filterPageTableList" :key="item.id"
                               :value="item.id" :label="item.tableName">
                      <el-row type="flex" justify="space-between" align="middle">
                        <span>{{item.tableName}}</span>
                        <el-tag size="mini" :type="OnlineTableType.getTag(item.tableType)" effect="dark"
                                style="margin-left: 30px;">
                          {{OnlineTableType.getLabel(item.tableType)}}
                        </el-tag>
                      </el-row>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitOnlinePage">确 定</el-button>
                <el-button @click="onlinePageDialog=false">取 消</el-button>
              </div>
            </el-dialog>
          </template>
          <template v-if="designerVisible">
            <page-designer :online-form="onlineForm" :online-page="onlinePage" @close="designerVisible=false"/>
          </template>
        </el-col>
      </el-row>
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
    publishOnlineForm,
    changeFormStatus
  } from '@/api/online/form'
  import {
    selectOnlineTableList,
    detailOnlineTable,
    deleteOnlineTable,
    addOnlineTable,
    updateOnlineTable
  } from '@/api/online/table'
  import { selectJdbcTableList, selectTableColumnList } from '@/api/system/jdbc'
  import { selectOnlineColumnList } from '@/api/online/column'
  import {
    selectOnlinePageList,
    detailOnlinePage,
    deleteOnlinePage,
    addOnlinePage,
    updateOnlinePage
  } from '@/api/online/page'
  import { DefaultPageConfig, DefaultWidgetAttributes } from './config'
  import TableColumn from './column'
  import PageDesigner from './designer'

  export default {
    name: 'Form',
    components: { TableColumn, PageDesigner },
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
            { required: true, message: '表单名称不能为空', trigger: 'blur' }
          ],
          formType: [
            { required: true, message: '表单类型不能为空', trigger: 'blur' }
          ]
        },
        jdbcTableList: [],
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
            { required: true, message: '请选择数据表！', trigger: 'blur' }
          ],
          tableLabel: [
            { required: true, message: '数据表描述不能为空！', trigger: 'blur' }
          ],
          tableType: [
            { required: true, message: '从表类型不能为空！', trigger: 'blur' }
          ],
          modelName: [
            { required: true, message: '数据模型标识不能为空！', trigger: 'blur' }
          ],
          masterColumnId: [
            { required: true, message: '主表关联字段不能为空！', trigger: 'blur' }
          ],
          slaveColumnName: [
            { required: true, message: '从表关联字段不能为空！', trigger: 'blur' }
          ]
        },
        // 主表信息
        masterOnlineTable: undefined,
        // 主表字段信息
        masterTableColumnList: [],
        // 从表字段信息
        slaveTableColumnList: [],
        // 编辑字段 表单ID
        onlineFormId: undefined,
        // 编辑字段 表ID
        onlineTableId: undefined,
        // 编辑字段 表名称
        onlineTableName: undefined,
        // 编辑字段 表描述
        onlineTableLabel: undefined,
        onlinePageList: [],
        onlinePageTitle: '新增表单页面',
        onlinePageDialog: false,
        onlinePage: {},
        onlinePageRules: {
          pageCode: [
            { required: true, message: '表单页面编码不能为空', trigger: 'blur' }
          ],
          pageName: [
            { required: true, message: '表单页面名称不能为空', trigger: 'blur' }
          ],
          pageType: [
            { required: true, message: '表单页面类型不能为空', trigger: 'blur' }
          ],
          pageKind: [
            { required: true, message: '表单页面分类不能为空', trigger: 'blur' }
          ],
          tableId: [
            { required: true, message: '数据表不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getOnlineFormList()
    },
    computed: {
      /** 过滤表单页面类型 */
      filterPageTypeList() {
        return this.OnlinePageType.getList().filter(item => {
          if (item.value === this.OnlinePageType.FLOW) {
            return this.onlineForm.formType === this.OnlineFormType.FLOW
          } else if (item.value === this.OnlinePageType.QUERY) {
            return this.onlineForm.formType !== this.OnlineFormType.FLOW
          } else if (item.value === this.OnlinePageType.ORDER) {
            return this.onlineForm.formType === this.OnlineFormType.FLOW
          } else {
            return true
          }
        })
      },
      /** 过滤表单页面可选数据表 */
      filterPageTableList() {
        return this.onlineTableList.filter(item => {
          switch (this.onlinePage.pageType) {
            // 工单列表页面和工作流流程页面，只能选择主表
          case this.OnlinePageType.FLOW:
          case this.OnlinePageType.ORDER:
            return item.tableType === this.OnlineTableType.MASTER
            // 流程编辑页面只支持一对多从表，普通编辑页面只支持主表和一对多从表
          case this.OnlinePageType.EDIT:
            return this.onlineForm.formType === this.OnlineFormType.FLOW ? item.tableType === this.OnlineTableType.ONE_TO_MANY : (item.tableType === this.OnlineTableType.MASTER || item.tableType === this.OnlineTableType.ONE_TO_MANY)
            // 查询页面可以选择主表或者一对多从表
          case this.OnlinePageType.QUERY:
            return item.tableType === this.OnlineTableType.MASTER || item.tableType === this.OnlineTableType.ONE_TO_MANY
          default:
            return false
          }
        })
      }
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
          formStatus: this.OnlineFormStatus.FORM_BASIC,
          publishStatus: this.SysPublishStatus.UNPUBLISHED,
          remark: undefined
        }
        this.activeStep = 0
        this.columnVisible = false
        this.designerVisible = false
        this.onlineTableList = []
        this.masterOnlineTable = {}
        this.masterTableColumnList = []
        this.slaveTableColumnList = []
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
      /** 修改发布状态 */
      handlePublishStatus(row) {
        if (row.publishStatus === this.SysPublishStatus.PUBLISHED && row.formStatus !== this.OnlineFormStatus.PAGE_DESIGN) {
          this.messageWarning('表单还处于' + this.OnlineFormStatus.getLabel(row.formStatus) + '不能进行发布！')
          row.publishStatus = row.publishStatus === this.SysPublishStatus.PUBLISHED ? this.SysPublishStatus.UNPUBLISHED : this.SysPublishStatus.PUBLISHED
          return
        }
        let text = row.publishStatus === this.SysPublishStatus.PUBLISHED ? '发布' : '停用'
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
          row.publishStatus = row.publishStatus === this.SysPublishStatus.PUBLISHED ? this.SysPublishStatus.UNPUBLISHED : this.SysPublishStatus.PUBLISHED
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
        this.single = selection.length !== 1
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
        if (this.activeStep === 1) {
          this.columnVisible = false
          this.getOnlineTableList()
        }
      },
      /** 下一步 */
      handleNextStep() {
        if (this.activeStep === 0) {
          this.columnVisible = false
          // 判断是否修改过表单基础信息
          if (JSON.stringify(this.onlineForm) !== this.onlineFormStr) {
            this.onlineFormStr = JSON.stringify(this.onlineForm)
            this.submitOnlineForm()
          } else {
            this.getOnlineTableList()
          }
        }
        if (this.activeStep === 1 && !this.masterOnlineTable) {
          this.messageWarning('请添加数据主表！')
          return
        }
        this.activeStep = this.activeStep + 1
        if (this.activeStep === 2) {
          this.designerVisible = false
          if (this.onlineForm.formStatus !== this.OnlineFormStatus.PAGE_DESIGN) {
            changeFormStatus({ id: this.onlineForm.id, formStatus: this.OnlineFormStatus.PAGE_DESIGN })
          }
          this.getOnlinePageList()
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
            if (this.onlineForm.id !== undefined) {
              updateOnlineForm(this.onlineForm).then(() => {
                this.getOnlineFormList()
                this.getOnlineTableList()
                this.messageSuccess('表单基础信息修改成功')
              })
            } else {
              addOnlineForm(this.onlineForm).then(response => {
                this.onlineForm = response.data
                this.onlineFormStr = JSON.stringify(this.onlineForm)
                this.getOnlineFormList()
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
      /** 查询表单数据模型列表 */
      getOnlineTableList() {
        this.formLoading = true
        this.resetOnlineTable()
        selectOnlineTableList(this.onlineTable).then(response => {
            this.onlineTableList = response.data
            this.masterOnlineTable = this.onlineTableList.find(item => item.tableType === this.OnlineTableType.MASTER)
            this.formLoading = false
          }
        )
      },
      /** 查询数据库表列表 */
      getJdbcTableList() {
        selectJdbcTableList().then(response => {
            this.jdbcTableList = response.data
          }
        )
      },
      /** 查询表单页面列表 */
      getOnlinePageList() {
        this.formLoading = true
        this.resetOnlinePage()
        selectOnlinePageList({ formId: this.onlineForm.id }).then(response => {
            this.onlinePageList = response.data
            this.formLoading = false
          }
        )
      },
      /** 查询主表字段列表 */
      getMasterTableColumnList(tableId) {
        selectOnlineColumnList({ formId: this.onlineTable.formId, tableId: tableId }).then(response => {
            this.masterTableColumnList = response.data
          }
        )
      },
      /** 查询从表字段列表 */
      getSlaveTableColumnList(tableName) {
        selectTableColumnList(tableName).then(response => {
          this.slaveTableColumnList = response.data
        })
      },
      // 表单重置
      resetOnlineTable() {
        this.onlineTable = {
          id: undefined,
          formId: this.onlineForm.id,
          tableName: undefined,
          tableType: undefined,
          tableLabel: undefined,
          modelName: undefined,
          masterTableId: undefined,
          masterColumnId: undefined,
          masterColumnName: undefined,
          slaveColumnId: undefined,
          slaveColumnName: undefined
        }
        this.slaveTableColumnList = []
        this.resetForm('onlineTable')
      },
      /** 新增数据表 */
      addOnlineTable() {
        this.resetOnlineTable()
        if (!this.onlineTableList || this.onlineTableList.length === 0) {
          this.onlineTable.tableType = this.OnlineTableType.MASTER
          this.onlineTableTitle = '新增数据主表'
        } else {
          this.onlineTable.tableType = undefined
          this.onlineTableTitle = '新增数据从表'
          this.onlineTable.masterTableId = this.masterOnlineTable.id
          if (this.masterTableColumnList) {
            this.getMasterTableColumnList(this.onlineTable.masterTableId)
          }
        }
        if (this.jdbcTableList) {
          this.getJdbcTableList()
        }
        this.onlineTableDialog = true
      },
      /** 编辑数据表 */
      editOnlineTable(row) {
        this.resetOnlineTable()
        detailOnlineTable(row.id).then(response => {
          this.onlineTable = response.data
          if (this.onlineTable.tableType === this.OnlineTableType.MASTER) {
            this.onlineTableTitle = '编辑数据主表'
          } else {
            this.onlineTableTitle = '编辑数据从表'
            if (this.masterTableColumnList) {
              this.getMasterTableColumnList(this.onlineTable.masterTableId)
            }
            this.getSlaveTableColumnList(this.onlineTable.tableName)
          }
          if (this.jdbcTableList) {
            this.getJdbcTableList()
          }
          this.onlineTableDialog = true
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
          this.getOnlineTableList()
          this.messageSuccess('删除成功')
        })
      },
      /** 提交数据表 */
      submitOnlineTable() {
        this.$refs.onlineTableForm.validate(valid => {
          if (valid) {
            if (this.onlineTable.tableType !== this.OnlineTableType.MASTER) {
              this.onlineTable.masterColumnName = this.masterTableColumnList.find(item => item.id === this.onlineTable.masterColumnId)?.columnName
            }
            if (this.onlineTable.id !== undefined) {
              updateOnlineTable(this.onlineTable).then(() => {
                this.onlineTableDialog = false
                this.messageSuccess('数据表信息修改成功')
                this.getOnlineTableList()
              })
            } else {
              addOnlineTable(this.onlineTable).then(() => {
                this.onlineTableDialog = false
                this.messageSuccess('新增数据表信息成功')
                this.getOnlineTableList()
              })
            }
          }
        })
      },
      /** 编辑数据表字段 */
      editOnlineTableColumn(row) {
        this.onlineFormId = row.formId
        this.onlineTableId = row.id
        this.onlineTableName = row.tableName
        this.onlineTableLabel = row.tableLabel
        this.columnVisible = true
      },
      handleOnlineTable() {
        this.onlineTable.modelName = this.toCamelCase(this.onlineTable.tableName)
        this.onlineTable.tableLabel = this.jdbcTableList.find(item => item.tableName === this.onlineTable.tableName)?.tableComment
        if (this.onlineTable.tableType !== this.OnlineTableType.MASTER) {
          this.getSlaveTableColumnList(this.onlineTable.tableName)
          this.onlineTable.slaveColumnName = undefined
        }
      },
      // 表单重置
      resetOnlinePage() {
        this.onlinePage = {
          id: undefined,
          formId: this.onlineForm.id,
          tableId: undefined,
          pageCode: undefined,
          pageName: undefined,
          pageType: this.onlineForm.formTag === this.OnlineFormType.FLOW ? this.OnlinePageType.FLOW : this.OnlinePageType.QUERY,
          pageKind: this.OnlinePageKind.JUMP
        }
        this.onlinePageList = []
        this.resetForm('onlinePage')
      },
      /** 新增表单页面 */
      addOnlinePage() {
        this.resetOnlinePage()
        this.onlinePageDialog = true
      },
      /** 编辑表单页面 */
      editOnlinePage(row) {
        detailOnlinePage(row.id).then(response => {
          this.onlinePage = response.data
          this.onlinePageDialog = true
        })
      },
      /** 删除表单页面 */
      deleteOnlinePage(row) {
        this.$confirm('是否确认删除表单页面名称为"' + row.pageName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteOnlinePage(row.id)
        }).then(() => {
          this.getOnlinePageList()
          this.messageSuccess('删除成功')
        })
      },
      /** 表单页面设计 */
      designerOnlinePage(row) {
        detailOnlinePage(row.id).then(response => {
          this.onlinePage = response.data
          this.designerVisible = true
        })
      },
      /** 提交表单页面 */
      submitOnlinePage() {
        let pageConfig = {
          ...DefaultPageConfig,
          widgetList: [],
          paramList: []
        }
        // 如果是查询页面，则添加默认的主表
        if (this.onlinePage.pageType === this.OnlinePageType.QUERY || this.onlinePage.pageType === this.OnlinePageType.ORDER) {
          pageConfig.tableWidget = {
            ...DefaultWidgetAttributes.Table,
            tableId: this.onlinePage.tableId,
            tableColumnList: [],
            variableName: this.onlinePage.pageCode,
            showName: this.onlinePage.pageName,
            operationList: this.onlinePage.pageType === this.OnlinePageType.QUERY ? [...DefaultWidgetAttributes.Table.operationList] : [],
          }
        }
        this.onlinePage.widgetJson = JSON.stringify({
          pageConfig: pageConfig,
          widgetList: []
        })
        this.onlinePage.paramsJson = JSON.stringify([])
        this.$refs.onlinePageForm.validate(valid => {
          if (valid) {
            if (this.onlinePage.id !== undefined) {
              updateOnlinePage(this.onlinePage).then(() => {
                this.onlinePageDialog = false
                this.messageSuccess('表单页面信息修改成功')
                this.getOnlinePageList()
              })
            } else {
              addOnlinePage(this.onlinePage).then(() => {
                this.onlinePageDialog = false
                this.messageSuccess('新增数据表信息成功')
                this.getOnlinePageList()
              })
            }
          }
        })
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
    height: calc(100vh - 150px);

    .form-basic {
      width: 60%;
      background: white;
      padding: 20px;
    }

    .data-model {
      width: 99%;
      background: white;
    }

    .page-designer {
      width: 99%;
      background: white;
    }
  }
</style>
