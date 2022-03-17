<template>
  <div class="properties-item__content">
    <el-form v-model="asyncInfo" :inline="false" label-width="30px" size="small" label-position="right" align="left">
      <el-form-item>
        <el-checkbox v-model="asyncInfo.asyncBefore" label="异步前" @change="updateAsyncInfo"/>
        <el-checkbox v-model="asyncInfo.asyncAfter" label="异步后" @change="updateAsyncInfo"/>
        <el-checkbox v-model="asyncInfo.exclusive" v-if="asyncInfo.asyncAfter || asyncInfo.asyncBefore" label="排除" @change="updateAsyncInfo"/>
      </el-form-item>
    </el-form>

    <el-form v-model="jobInfo" v-if="asyncInfo.asyncAfter || asyncInfo.asyncBefore" :inline="false" label-width="100px" size="small" label-position="right" align="left">
      <el-divider>工作配置</el-divider>
      <el-form-item label="工作优先级:">
        <el-input v-model="jobInfo.jobPriority" @change="updateJobPriority"/>
      </el-form-item>
      <el-form-item label="重试时间周期:">
        <el-input v-model="jobInfo.timeCycle" @change="updateTimeCycle"/>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import processHelper from '../../helper/ProcessHelper'

  export default {
    name: 'Job',
    props: {
      processModeler: {  // 流程Modeler
        type: Object,
        required: true
      },
      processInfo: {    // 流程基础信息
        type: Object,
        required: true
      },
      activeElement: {  // 当前选中元素
        type: Object,
        required: true
      }
    },
    data() {
      return {
        asyncInfo: {
          asyncAfter: false,
          asyncBefore: false,
          exclusive: false
        },
        jobInfo: {
          jobPriority: '',
          timeCycle: ''
        }
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            this.asyncInfo.asyncBefore = this.activeElement.businessObject?.asyncBefore
            this.asyncInfo.asyncAfter = this.activeElement.businessObject?.asyncAfter
            this.asyncInfo.exclusive = this.activeElement.businessObject?.exclusive
            if (this.asyncInfo.asyncBefore || this.asyncInfo.asyncAfter) {
              this.jobInfo.jobPriority = this.activeElement.businessObject?.jobPriority
              this.initFailedJobRetryTimeCycle()
            }
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      initFailedJobRetryTimeCycle() {
        const timeCycleElement = this.activeElement.businessObject?.extensionElements?.values?.find(ex => {
          return ex.$type === `${this.processInfo.processPrefix}:FailedJobRetryTimeCycle`
        })
        this.jobInfo.timeCycle = timeCycleElement?.body
      },
      updateAsyncInfo() {
        if (!this.asyncInfo.asyncBefore && !this.asyncInfo.asyncAfter) {
          this.asyncInfo.exclusive = null
          this.jobInfo.jobPriority = null
          this.jobInfo.timeCycle = null
          this.updateJobPriority()
          this.updateTimeCycle()
        }
        processHelper.updateProperties(this.activeElement, { ...this.asyncInfo })
      },
      updateJobPriority() {
        const properties = Object.create(null)
        properties.jobPriority = this.jobInfo.jobPriority
        processHelper.updateProperties(this.activeElement, { ...properties })
      },
      updateTimeCycle() {
        let extensionElements = this.activeElement.businessObject.get('extensionElements')
        if (!extensionElements) {
          extensionElements = processHelper.createElement('bpmn:ExtensionElements')
        }
        extensionElements.values = extensionElements.values?.filter(item => item.$type !== `${this.processInfo.processPrefix}:FailedJobRetryTimeCycle`) ?? []
        if (this.jobInfo.timeCycle) {
          const properties = { body: this.jobInfo.timeCycle }
          const timeCycleElementName = `${this.processInfo.processPrefix}:FailedJobRetryTimeCycle`
          const timeCycleElement = processHelper.createElement(timeCycleElementName, properties)
          extensionElements.get('values').push(timeCycleElement)
          processHelper.updateProperties(this.activeElement, { extensionElements: extensionElements })
        }
      }
    }
  }
</script>
<style lang='scss'>
</style>
