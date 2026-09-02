<template>
  <div style="position: relative;">
    <el-form ref="queryForm" label-width="80px" size="mini" :inline="true" v-show="showSearch">
      <drag-widget-filter>
        <el-form-item label="流程状态">
          <el-select class="filter-item" v-model="processOrderStatus" :clearable="true" placeholder="流程状态"
                     style="width: 250px">
            <el-option v-for="item in ProcessOrderStatus.getList()" :key="item.value" :label="item.label"
                       :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="创建日期">
          <el-date-picker class="filter-item" v-model="createTime" style="width: 250px" :clearable="true"
                          :allowTypes="['day']" align="left"
                          format="yyyy-MM-dd" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                          range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"/>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" :plain="true"
                     :disabled="processDefinitionId == null" @click="onSearch()">搜索
          </el-button>
          <el-button icon="el-icon-refresh" size="mini" :plain="true" :disabled="processDefinitionId == null">重置
          </el-button>
        </el-form-item>
      </drag-widget-filter>
    </el-form>


    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" :disabled="processDefinitionId == null"
                   @click="createOrder()">新建
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="onSearch"></right-toolbar>
    </el-row>

    <el-row>
      <el-col :span="24" v-if="pageConfig.pageQueryTable">
        <custom-table-widget :ref="pageConfig.pageQueryTable.variableName"
                             :widgetConfig="pageConfig.pageQueryTable"
                             :pageType="pageConfig.pageType"
                             :tableQueryParam="getTableQueryParam"
                             :loadTableDataFunc="loadProcessOrderList"
                             @viewProcessOrder="onView"
                             @handlerProcessOrder="onHandler"
                             @cancelProcessOrder="onCancelProcessOrder"
                             @handlerRemind="onRemindClick"
        />
      </el-col>
    </el-row>


    <el-dialog :title="orderDialogTitle" :visible.sync="openOrderDialog" width="650px" append-to-body>
      <online-flow-page v-if="openOrderDialog" :key="new Date().getTime()" ref="onlineOrder"
                        :page-id="processDefinition.pageId"/>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitOrder">提 交</el-button>
        <el-button @click="openOrderDialog = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="流程办理" :visible.sync="handleProcess.openProcess" width="750px" append-to-body>
      <el-tabs v-model="handleProcess.activeName" @tab-click="handleClick">
        <el-tab-pane label="表单信息" name="formInfo">
          <div v-if="handleProcess.fromParser">
            <process-form-parser :key="new Date().getTime()" :form-conf="handleProcess.parserForm"
                                 :form-data="handleProcess.processOrder.formData"
                                 v-if="handleProcess.processOrder.formType === ProcessFormType.PROCESS_FORM && handleProcess.fromParser"
            <online-flow-page ref="onlineForm" :key="new Date().getTime()"
                              :page-id="handleProcess.processOrder.pageId"
                              :process-id="handleProcess.processOrder.processId" :read-only="true"
                              :page-type="OnlinePageType.FLOW"
                              :page-data="handleProcess.processOrder.pageData"
                              v-if="handleProcess.processOrder.formType === ProcessFormType.ONLINE_FORM && handleProcess.fromParser"
                              style="height: 260px" v-once/>

          </div>
        </el-tab-pane>
        <el-tab-pane label="流程视图" name="processView">
          <process-view key="designer" v-model="handleProcess.processOrder.processXml"
                        :high-line-data="handleProcess.processOrder.highLineData" style="height: 260px"
                        v-if="handleProcess.openProcessView"/>
        </el-tab-pane>
        <el-tab-pane label="流转信息" name="flowInfo">
          <div style="height: 260px;">
            <el-table v-loading="handleProcess.loading" :data="handleProcess.flowInfoList">
              <el-table-column label="执行环节" align="center" prop="activityName" :show-overflow-tooltip="true"/>
              <el-table-column label="执行人" align="center" prop="assigneeName" :show-overflow-tooltip="true"/>
              <el-table-column label="开始时间" width="150" align="center" prop="startTime"/>
              <el-table-column label="结束时间" width="150" align="center" prop="endTime"/>
              <el-table-column label="办理状态" align="center" prop="status"/>
              <el-table-column label="审批意见" align="center" prop="message" :show-overflow-tooltip="true"/>
              <el-table-column label="任务历时" align="center" prop="durationTime"/>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
      <el-divider/>
      <el-form ref="taskForm" :model="taskForm" :rules="taskRules" label-width="80px">
        <el-form-item label="审批意见" prop="approveMessage">
          <el-input v-model="taskForm.approveMessage" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleApproveProcessTask">同 意</el-button>
        <el-button type="danger" @click="handleRefuseProcessTask">拒 绝</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import { OnlinePageMixins } from './onlinePageMixins.js'
  import CustomTableWidget from '../designer/customTableWidget'
  import DragWidgetFilter from '../designer/dragWidgetFilter'
  import OnlineFlowPage from './onlineFlowPage'
  import { selectMainProcessDefinition } from '@/api/process/definition'
  import {
    selectOnlineOrderList,
    startProcess,
    detailProcessOrder,
    cancelProcessOrder,
    detailProcessHistory
  } from '@/api/process/order'
  import { approveProcessTask, refuseProcessTask } from '@/api/process/task'
  import ProcessFormParser from '@/components/FormDesigner/parser/Parser'

  export default {
    name: 'OnlineOrderPage',
    props: {
      pageId: {
        type: String,
        required: true
      },
      processId: {
        type: String,
        required: true
      },
      isPreview: {
        type: Boolean,
        default: false
      }
    },
    mixins: [OnlinePageMixins],
    components: {
      CustomTableWidget, DragWidgetFilter, OnlineFlowPage, ProcessFormParser
    },
    data() {
      return {
        processDefinitionId: undefined,
        processDefinition: {},
        processName: undefined,
        createTime: [],
        processOrderStatus: undefined,
        showSearch: true,
        orderDialogTitle: undefined,
        openOrderDialog: false,
        handleProcess: {
          fromParser: true,
          loading: true,
          openProcess: false,
          processOrder: {},
          processTask: undefined,
          activeName: 'formInfo',
          formView: false,
          openProcessView: false,
          parserForm: undefined,
          formData: undefined,
          flowInfoList: undefined
        },
        taskForm: {
          approveMessage: undefined
        },
        taskRules: {
          approveMessage: [
            { required: true, message: '审批意见不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      getTableQueryParam(widget) {

      },

      loadProcessOrderList(params) {
        if (this.isPreview || this.processDefinitionId == null) return Promise.reject()
        return new Promise((resolve, reject) => {
          params.queryCond = {
            ...params.queryCond,
            processId: this.processId,
            orderPageId: this.pageId,
            instanceStatus: this.processOrderStatus,
            createTimeStart: Array.isArray(this.createTime) ? this.createTime[0] : undefined,
            createTimeEnd: Array.isArray(this.createTime) ? this.createTime[1] : undefined
          }
          selectOnlineOrderList(params).then(response => {
            resolve({
              dataList: (response.data && response.data.records) || [],
              pageTotal: (response.data && response.data.pageTotal) || 0
            })
          })
        })
      },
      onSearch() {
        this.formPageData.pageFilterCopy = {
          ...this.formPageData.pageFilter
        }
        this.$refs[this.pageConfig.pageQueryTable.variableName].refresh()
      },
      onResume() {
        let key = this.$route.fullPath
        let cachePageData = this.onlinePageCache[key]
        if (cachePageData) {
          this.$nextTick(() => {
            if (Array.isArray(this.dropdownWidgetList)) {
              this.dropdownWidgetList.forEach(dropdownWidget => {
                let dropdownWidgetImpl = this.$refs[dropdownWidget.variableName][0]
                if (dropdownWidgetImpl) {
                  dropdownWidgetImpl.onVisibleChange()
                }
              })
            }
            this.formPageData.formFilter = cachePageData.pageFilter
            this.formPageData.formFilterCopy = cachePageData.pageFilterCopy
            this.$refs[this.pageConfig.pageQueryTable.variableName].setTableWidget(cachePageData.tableImpl)
            this.deleteOnlinePageCache(key)
          })
        }
      },
      /** 创建工单 **/
      createOrder() {
        if (this.isPreview || this.processDefinitionId === null || !this.processDefinition) return
        this.orderDialogTitle = this.processDefinition.processName
        this.openOrderDialog = true
      },
      submitOrder() {
        this.$refs.onlineOrder.getFormPageData().then(orderData => {
          if (!this.processDefinition) return
          startProcess({ processDefinitionId: this.processDefinition.id, orderData: orderData }).then(response => {
              this.messageSuccess('流程工单发起成功')
              this.onSearch()
              this.openOrderDialog = false
            }
          )
        })
      },
      onView(row) {
        this.$router.push({ path: '/process/order/detail/' + row.orderId }).catch(() => {})
      },
      onHandler(row) {
        // 先关闭对话框并清除旧数据
        this.handleProcess.openProcess = false
        this.handleProcess.processOrder = {}
        this.handleProcess.processTask = undefined
        this.handleProcess.parserForm = undefined
        this.handleProcess.formData = undefined
        this.handleProcess.flowInfoList = undefined
        this.handleProcess.activeName = 'formInfo'
        this.handleProcess.fromParser = true
        this.handleProcess.openProcessView = false
        this.taskForm.approveMessage = undefined

        detailProcessOrder(row.orderId).then(response => {
          if (!response.data) return
          this.handleProcess.processOrder = response.data
          this.handleProcess.processTask = row
          if (this.handleProcess.processOrder.formType === this.ProcessFormType.PROCESS_FORM) { // 流程表单
            if (this.handleProcess.processOrder.formConf && this.handleProcess.processOrder.formFields) {
              let formConf = JSON.parse(this.handleProcess.processOrder.formConf)
              formConf.formBtns = false
              formConf.disabled = true
              this.handleProcess.parserForm = {
                fields: JSON.parse(this.handleProcess.processOrder.formFields),
                ...formConf
              }
              this.fillFormData(this.handleProcess.parserForm, JSON.parse(this.handleProcess.processOrder.formData))
            }
          }
          this.handleProcess.openProcess = true
        })
      },
      onRemindClick(row) {

      },
      handleClick(tab, event) {
        this.handleProcess.fromParser = false
        this.handleProcess.openProcessView = false
        if (tab.name == 'formInfo') {
          this.handleProcess.fromParser = true
        }
        if (tab.name == 'processView') {
          this.handleProcess.openProcessView = true
        }
        if (tab.name == 'flowInfo' && !this.flowInfoList) {
          this.handleProcess.loading = true
          detailProcessHistory(this.handleProcess.processOrder.id).then(response => {
            this.$nextTick(() => {
              this.handleProcess.flowInfoList = response.data
              this.handleProcess.loading = false
            })
          })
        }
      },
      onCancelProcessOrder(row) {
        cancelProcessOrder(row.orderId).then(response => {
            this.messageSuccess('流程撤销成功！')
            this.onSearch()
          }
        )
      },
      handleApproveProcessTask() {
        this.$refs.taskForm.validate(valid => {
          if (valid) {
            approveProcessTask({
              id: this.handleProcess.processTask.taskId,
              approveMessage: this.taskForm.approveMessage
            }).then(response => {
              this.messageSuccess('任务执行成功！')
              this.handleProcess.openProcess = false
              this.onSearch()
            })
          }
        })
      },
      handleRefuseProcessTask() {
        this.$refs.taskForm.validate(valid => {
          if (valid) {
            refuseProcessTask({
              id: this.handleProcess.processTask.taskId,
              approveMessage: this.taskForm.approveMessage
            }).then(response => {
              this.messageSuccess('任务拒绝成功！')
              this.handleProcess.openProcess = false
              this.onSearch()
            })
          }
        })
      }
    },
    provide() {
      return {
        preview: () => this.isPreview
      }
    },
    computed: {
      ...mapGetters(['onlinePageCache'])
    },
    mounted() {
      selectMainProcessDefinition(this.processId).then(response => {
          if (response.data) {
            this.processDefinition = response.data
            this.processDefinitionId = response.data.id
            this.processName = response.data.processName
          }
        }
      )
    }
  }
</script>

<style>
</style>
