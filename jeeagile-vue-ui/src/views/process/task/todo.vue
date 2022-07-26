<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模型编码" prop="modelCode">
        <el-input v-model="queryParam.queryCond.modelCode" placeholder="请输入模型编码" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="模型名称" prop="modelName">
        <el-input v-model="queryParam.queryCond.modelName" placeholder="请输入模型名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="发起人" prop="modelName">
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
      <el-table-column label="模型编码" align="center" prop="modelCode"/>
      <el-table-column label="模型名称" align="center" prop="modelName" :show-overflow-tooltip="true"/>
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
      <process-view key="designer" v-model="processXml" :high-line-data="highLineData"/>
    </el-dialog>
    <el-dialog title="流程办理" :visible.sync="handleProcess.openProcess" width="750px" append-to-body>
      <el-tabs v-model="handleProcess.activeName" @tab-click="handleClick">
        <el-tab-pane label="表单信息" name="formInfo">
          <form-parser :key="new Date().getTime()" :form-conf="handleProcess.parserForm"
                       :form-data="handleProcess.formData"/>
        </el-tab-pane>
        <el-tab-pane label="流程视图" name="processView">
          <process-view key="designer" v-model="handleProcess.processXml" :high-line-data="handleProcess.highLineData"/>
        </el-tab-pane>
        <el-tab-pane label="流转信息" name="flowInfo">
          <el-table v-loading="handleProcess.loading" :data="handleProcess.flowInfoList">
            <el-table-column label="执行环节" align="center" prop="activityName" :show-overflow-tooltip="true"/>
            <el-table-column label="执行人" align="center" prop="assigneeName" :show-overflow-tooltip="true"/>
            <el-table-column label="开始时间" width="150" align="center" prop="startTime"/>
            <el-table-column label="结束时间" width="150" align="center" prop="endTime"/>
            <el-table-column label="办理状态" align="center" prop="status"/>
            <el-table-column label="审批意见" align="center" prop="message"/>
            <el-table-column label="任务历时" align="center" prop="durationTime"/>
          </el-table>
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
    detailProcessInstance,
    detailProcessHistory
  } from '@/api/process/instance'
  import FormParser from '@/components/FormDesigner/parser/Parser'

  export default {
    name: 'Apply',
    components: { FormParser },
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
            modelCode: undefined,
            modelName: undefined,
            formName: undefined,
            startUserName: undefined
          }
        },
        openView: false,
        processXml: undefined,
        highLineData: [],
        handleProcess: {
          loading: true,
          openProcess: false,
          processInstances: undefined,
          processTask: undefined,
          activeName: 'formInfo',
          formView: false,
          parserForm: undefined,
          formData: undefined,
          processXml: undefined,
          flowInfoList: undefined,
          highLineData: undefined
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
            this.queryParam.pageTotal = response.data.pageTotal
            this.processTodoList = response.data.records
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
        detailProcessInstance(row.instanceId).then(response => {
          this.handleProcess.processInstances = response.data
          this.handleProcess.processTask = row
          if (this.handleProcess.processInstances.formConf && this.handleProcess.processInstances.formFields) {
            let formConf = JSON.parse(this.handleProcess.processInstances.formConf)
            formConf.formBtns = false
            formConf.disabled = true
            this.handleProcess.parserForm = {
              fields: JSON.parse(this.handleProcess.processInstances.formFields),
              ...formConf
            }
            this.fillFormData(this.handleProcess.parserForm, JSON.parse(this.handleProcess.processInstances.formData))
            this.handleProcess.openProcess = true
          }
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
        detailProcessInstance(row.instanceId).then(response => {
          this.$nextTick(() => {
            this.processXml = response.data.modelXml
            this.highLineData = response.data.highLineData
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
        if (tab.name == 'processView' && !this.handleProcess.processXml) {
          this.handleProcess.processXml = this.handleProcess.processInstances.modelXml
          this.handleProcess.highLineData = this.handleProcess.processInstances.highLineData
        }

        if (tab.name == 'flowInfo' && !this.flowInfoList) {
          this.handleProcess.loading = true
          detailProcessHistory(this.handleProcess.processInstances.id).then(response => {
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
