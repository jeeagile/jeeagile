<template>
  <div class="properties-item__content">
    <el-form :inline="false" label-width="100px" size="small" label-position="right">
      <el-form-item label="类型:">
        <el-select v-model="loopCharacteristicsType" @change="updateLoopCharacteristicsType">
          <el-option label="非会签" value=""/>
          <el-option label="并行多实例" value="ParallelMultiInstance"/>
          <el-option label="串行多实例" value="SequentialMultiInstance"/>
          <el-option label="循环" value="StandardLoop"/>
        </el-select>
      </el-form-item>
    </el-form>
    <el-form v-if="loopCharacteristicsType === 'ParallelMultiInstance' || loopCharacteristicsType === 'SequentialMultiInstance'"
             v-model="loopCharacteristicsInfo" :inline="false" label-width="100px" size="small" label-position="right">
      <el-form-item label="实例数量:">
        <el-input v-model="loopCharacteristicsInfo.loopCardinality" clearable @change="updateLoopCardinality"/>
      </el-form-item>
      <el-form-item label="集合:">
        <el-input v-model="loopCharacteristicsInfo.collection" clearable @change="updateLoopCharacteristicsInfo('collection')"/>
      </el-form-item>
      <el-form-item label="元素变量:">
        <el-input v-model="loopCharacteristicsInfo.elementVariable" clearable @change="updateLoopCharacteristicsInfo('elementVariable')"/>
      </el-form-item>
      <el-form-item label="完成条件:">
        <el-input v-model="loopCharacteristicsInfo.completionCondition" clearable @change="updateCompletionCondition"/>
      </el-form-item>
      <el-divider>异步状态</el-divider>
      <el-form-item label="" label-width="30px" align="left">
        <el-checkbox v-model="loopCharacteristicsInfo.asyncBefore" label="异步前" @change="updateLoopCharacteristicsAsync"/>
        <el-checkbox v-model="loopCharacteristicsInfo.asyncAfter" label="异步后" @change="updateLoopCharacteristicsAsync"/>
        <el-checkbox v-if="loopCharacteristicsInfo.asyncAfter || loopCharacteristicsInfo.asyncBefore"
                     v-model="loopCharacteristicsInfo.exclusive" label="排除" @change="updateLoopCharacteristicsAsync"/>
      </el-form-item>
      <el-form-item v-if="loopCharacteristicsInfo.asyncAfter || loopCharacteristicsInfo.asyncBefore" label="重试周期:">
        <el-input v-model="loopCharacteristicsInfo.timeCycle" clearable @change="updateLoopCharacteristicsTimeCycle"/>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import processHelper from '../../helper/ProcessHelper'

  export default {
    name: 'MultiInstance',
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
        loopCharacteristicsElement: null,
        loopCharacteristicsType: '',
        loopCharacteristicsInfo: {
          loopCardinality: '',      // 实例数量
          collection: '',           // 集合
          elementVariable: '',      // 元素变量
          completionCondition: '',  // 完成条件
          asyncAfter: false,        // 异步前
          asyncBefore: false,       // 异步后
          exclusive: false,         // 排除
          timeCycle: ''             // 重试周期
        }
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            this.initLoopCharacteristicsInfo()
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      initLoopCharacteristicsInfo() {
        const businessObject = this.activeElement.businessObject
        if (!businessObject.loopCharacteristics) {
          this.loopCharacteristicsType = ''
          this.loopCharacteristicsInfo = {}
          return
        }
        if (businessObject.loopCharacteristics.$type === 'bpmn:StandardLoopCharacteristics') {
          this.loopCharacteristicsType = 'StandardLoop'
          this.loopCharacteristicsInfo = {}
          return
        }
        if (businessObject.loopCharacteristics.isSequential) {
          this.loopCharacteristicsType = 'SequentialMultiInstance'
        } else {
          this.loopCharacteristicsType = 'ParallelMultiInstance'
        }
        this.loopCharacteristicsInfo = {
          ...businessObject.loopCharacteristics,
          completionCondition: businessObject.loopCharacteristics?.completionCondition?.body ?? '',
          loopCardinality: businessObject.loopCharacteristics?.loopCardinality?.body ?? ''
        }
        this.loopCharacteristicsElement = businessObject.loopCharacteristics
        if (this.loopCharacteristicsElement.extensionElements &&
          this.loopCharacteristicsElement.extensionElements.values &&
          this.loopCharacteristicsElement.extensionElements.values.length
        ) {
          const timeCycleElement = this.loopCharacteristicsElement.extensionElements.values.find(ex => {
            return ex.$type === `${this.processInfo.processPrefix}:FailedJobRetryTimeCycle`
          })
          this.$set(this.loopCharacteristicsInfo, 'timeCycle', timeCycleElement?.body)
        }
      },
      updateLoopCharacteristicsType() {
        if (this.loopCharacteristicsType === '') { // 非会签
          this.loopCharacteristicsInfo = {}
          processHelper.updateProperties(this.activeElement, { loopCharacteristics: null })
          return
        }
        if (this.loopCharacteristicsType === 'StandardLoop') { // 循环
          this.loopCharacteristicsInfo = {}
          const standardLoopElement = processHelper.createElement('bpmn:StandardLoopCharacteristics')
          processHelper.updateProperties(this.activeElement, { loopCharacteristics: standardLoopElement })
          this.loopCharacteristicsElement = null
          return
        }
        if (this.loopCharacteristicsType === 'SequentialMultiInstance') { // 串行多实例
          this.loopCharacteristicsElement = processHelper.createElement('bpmn:MultiInstanceLoopCharacteristics', { isSequential: true })
        } else { // 并行多实例
          this.loopCharacteristicsElement = processHelper.createElement('bpmn:MultiInstanceLoopCharacteristics')
        }
        processHelper.updateProperties(this.activeElement, { loopCharacteristics: this.loopCharacteristicsElement })
      },
      updateLoopCardinality(loopCardinality) {
        let loopCardinalityElement = null
        if (loopCardinality && loopCardinality.length) {
          loopCardinalityElement = processHelper.createElement('bpmn:FormalExpression', { body: loopCardinality })
        }
        processHelper.updateModdleProperties(this.activeElement, this.loopCharacteristicsElement, { loopCardinality: loopCardinalityElement })
      },
      updateCompletionCondition(completionCondition) {
        let completionConditionElement = null
        if (completionCondition && completionCondition.length) {
          completionConditionElement = processHelper.createElement('bpmn:FormalExpression', { body: completionCondition })
        }
        processHelper.updateModdleProperties(this.activeElement, this.loopCharacteristicsElement, { completionCondition: completionConditionElement })
      },
      updateLoopCharacteristicsInfo(key) {
        const properties = Object.create(null)
        properties[key] = this.loopCharacteristicsInfo[key]
        processHelper.updateModdleProperties(this.activeElement, this.loopCharacteristicsElement, properties)
      },
      updateLoopCharacteristicsAsync() {
        const properties = Object.create(null)
        properties.asyncBefore = this.loopCharacteristicsInfo.asyncBefore
        properties.asyncAfter = this.loopCharacteristicsInfo.asyncAfter
        if (!properties.asyncBefore && !properties.asyncAfter) {
          this.loopCharacteristicsInfo.exclusive = false
          this.loopCharacteristicsInfo.timeCycle = null
          properties.exclusive = null
          this.updateLoopCharacteristicsTimeCycle()
        } else {
          properties.exclusive = this.loopCharacteristicsInfo.exclusive
        }
        processHelper.updateModdleProperties(this.activeElement, this.loopCharacteristicsElement, properties)
      },
      updateLoopCharacteristicsTimeCycle() {
        let extensionElements = this.loopCharacteristicsElement.get('extensionElements')
        if (!extensionElements) {
          extensionElements = processHelper.createElement('bpmn:ExtensionElements')
        }
        extensionElements.values = extensionElements.values?.filter(item => item.$type !== `${this.processInfo.processPrefix}:FailedJobRetryTimeCycle`) ?? []
        if (this.loopCharacteristicsInfo.timeCycle) {
          const properties = { body: this.loopCharacteristicsInfo.timeCycle }
          const timeCycleElementName = `${this.processInfo.processPrefix}:FailedJobRetryTimeCycle`
          const timeCycleElement = processHelper.createElement(timeCycleElementName, properties)
          extensionElements.get('values').push(timeCycleElement)
          processHelper.updateModdleProperties(this.activeElement, this.loopCharacteristicsElement, { extensionElements: extensionElements })
        }
      }
    }
  }
</script>
<style lang='scss'>
</style>
