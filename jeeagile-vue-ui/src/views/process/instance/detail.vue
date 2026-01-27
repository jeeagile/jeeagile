<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="表单信息" name="formInfo">
        <process-form-parser :key="new Date().getTime()" :form-conf="parserForm" :form-data="formData"
                             v-if="processInstances.formType === ProcessFormType.PROCESS_FORM"/>
      </el-tab-pane>
      <el-tab-pane label="流程视图" name="processView">
        <process-view key="designer" v-model="processXml" :high-line-data="highLineData"
                      style="width:100%;height: 500px"/>
      </el-tab-pane>
      <el-tab-pane label="流转信息" name="flowInfo">
        <el-table v-loading="loading" :data="flowInfoList">
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
  </div>
</template>

<script>
  import ProcessFormParser from '@/components/FormDesigner/parser/Parser'
  import {
    detailProcessInstance,
    detailProcessHistory
  } from '@/api/process/instance'

  export default {
    name: 'DetailInstance',
    components: {
      ProcessFormParser
    },
    data() {
      return {
        loading: true,
        processInstances: {},
        activeName: 'formInfo',
        formView: false,
        parserForm: undefined,
        formData: undefined,
        processXml: undefined,
        flowInfoList: undefined,
        highLineData: undefined
      }
    },
    created() {
      const instanceId = this.$route.params && this.$route.params.instanceId
      this.getProcessInstanceInfo(instanceId)
    },
    methods: {
      getProcessInstanceInfo(instanceId) {
        this.activeName = 'formInfo'
        detailProcessInstance(instanceId).then(response => {
          this.$nextTick(() => {
            this.processInstances = response.data
            this.processXml = this.processInstances.modelXml
            this.highLineData = this.processInstances.highLineData
            if (this.processInstances.formType === this.ProcessFormType.PROCESS_FORM) { // 流程表单
              if (this.processInstances.formConf && this.processInstances.formFields) {
                let formConf = JSON.parse(this.processInstances.formConf)
                formConf.formBtns = false
                formConf.disabled = true
                this.parserForm = {
                  fields: JSON.parse(this.processInstances.formFields),
                  ...formConf
                }
                this.fillFormData(this.parserForm, JSON.parse(this.processInstances.formData))
              }
            }
          })
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
      handleClick(tab, event) {
        if (tab.name == 'flowInfo' && !this.flowInfoList) {
          this.loading = true
          detailProcessHistory(this.processInstances.id).then(response => {
            this.$nextTick(() => {
              this.flowInfoList = response.data
              this.loading = false
            })
          })
        }
      }
    }
  }
</script>
