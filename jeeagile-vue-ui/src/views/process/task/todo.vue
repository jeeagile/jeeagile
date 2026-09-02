<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="流程编码" prop="processCode">
        <el-input v-model="queryParam.queryCond.processCode" placeholder="请输入流程编码" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="流程名称" prop="processName">
        <el-input v-model="queryParam.queryCond.processName" placeholder="请输入流程名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="发起人" prop="processName">
        <el-input v-model="queryParam.queryCond.startUserName" placeholder="请输入流程发起人名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getProcessTodoList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="processTodoList">
      <el-table-column label="流程编码" align="center" prop="processCode"/>
      <el-table-column label="流程名称" align="center" prop="processName" :show-overflow-tooltip="true"/>
      <el-table-column label="表单名称" align="center" prop="formName" :show-overflow-tooltip="true"/>
      <el-table-column label="发起人" width="150" align="center" prop="startUserName"/>
      <el-table-column label="创建时间" width="150" align="center" prop="startTime"/>
      <el-table-column label="任务状态" align="center" :formatter="taskStatusFormat"/>
      <el-table-column label="操作" width="200px" align="center" class-name="small-padding">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleProcessTask(scope.row)">
            流程办理
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleProcessView(scope.row)">
            流程进度
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getProcessTodoList"/>

    <el-dialog title="流程进度" :visible.sync="openView" width="700px" append-to-body>
      <process-view key="designer" v-model="processXml" :high-line-data="highLineData" style="height: 400px"/>
    </el-dialog>
    <el-dialog title="流程办理" :visible.sync="handleProcess.openProcess" width="750px" append-to-body>
      <el-tabs v-model="handleProcess.activeName" @tab-click="handleClick">
        <el-tab-pane label="表单信息" name="formInfo">
          <div v-if="handleProcess.fromParser">
            <process-form-parser :key="new Date().getTime()" :form-conf="handleProcess.parserForm"
                                 :form-data="handleProcess.processOrder.formData"
                                 v-if="handleProcess.processOrder.formType === ProcessFormType.PROCESS_FORM && handleProcess.fromParser"
                                 style="height: 260px"/>
            <online-form-parser ref="onlineForm" :key="new Date().getTime()"
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
  import {
    selectTodoPage,
    approveProcessTask,
    refuseProcessTask
  } from '@/api/process/task'
  import {
    detailProcessOrder,
    detailProcessHistory
  } from '@/api/process/order'
  import ProcessFormParser from '@/components/FormDesigner/parser/Parser'
  import OnlineFormParser from '../../online/index'

  export default {
    name: 'Apply',
    components: { ProcessFormParser, OnlineFormParser },
    data() {
      return {
        // 遮罩层
        loading: true,

        // 显示搜索条件
        showSearch: true,
        taskStatusOptionList: [],
        // 流程列表
        processTodoList: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            processCode: undefined,
            processName: undefined,
            formName: undefined,
            startUserName: undefined
          }
        },
        openView: false,
        processXml: undefined,
        highLineData: [],
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
    created() {
      this.getProcessTodoList()
      this.getSysDictDataList('process_task_status').then(response => {
        this.taskStatusOptionList = response.data
      })
    },
    methods: {
      /** 查询流程表单列表 */
      getProcessTodoList() {
        selectTodoPage(this.queryParam).then(response => {
            this.queryParam.pageTotal = (response.data && response.data.pageTotal) || 0
            this.processTodoList = (response.data && response.data.records) || []
            this.loading = false
          }
        )
      },
      // 参数系统内置字典翻译
      taskStatusFormat(row, column) {
        return this.handleDictLabel(this.taskStatusOptionList, row.taskStatus)
      },
      /** 流程办理 */
      handleProcessTask(row) {
        this.handleProcess.openProcess = false
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
      fillFormData(form, data) {
        form.fields.forEach(item => {
          const val = data[item.__vModel__]
          if (val) {
            item.__config__.defaultValue = val
          }
        })
      },
      /** 流程进度 */
      handleProcessView(row) {
        this.openView = false
        detailProcessOrder(row.orderId).then(response => {
          if (!response.data) return
          this.$nextTick(() => {
            this.processXml = (response.data && response.data.processXml) || ''
            this.highLineData = (response.data && response.data.highLineData) || []
            this.openView = true
          })
        })
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getProcessTodoList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
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
      handleApproveProcessTask() {
        this.$refs.taskForm.validate(valid => {
          if (valid) {
            approveProcessTask({
              id: this.handleProcess.processTask.id,
              approveMessage: this.taskForm.approveMessage
            }).then(response => {
              this.messageSuccess('任务执行成功！')
              this.handleProcess.openProcess = false
              this.getProcessTodoList()
            })
          }
        })
      },
      handleRefuseProcessTask() {
        this.$refs.taskForm.validate(valid => {
          if (valid) {
            refuseProcessTask({
              id: this.handleProcess.processTask.id,
              approveMessage: this.taskForm.approveMessage
            }).then(response => {
              this.messageSuccess('任务拒绝成功！')
              this.handleProcess.openProcess = false
              this.getProcessTodoList()
            })
          }
        })
      }
    }
  }
</script>
