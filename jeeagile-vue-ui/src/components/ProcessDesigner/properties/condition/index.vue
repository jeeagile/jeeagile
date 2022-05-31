<template>
  <div class="properties-item__content">
    <el-form v-model="flowConditionInfo" :inline="false" label-width="100px" size="small" label-position="right">
      <el-form-item label="流转类型:">
        <el-select v-model="flowConditionInfo.flowType" @change="updateFlowType">
          <el-option label="普通流转路径" value="normal"/>
          <el-option label="默认流转路径" value="default"/>
          <el-option label="条件流转路径" value="condition"/>
        </el-select>
      </el-form-item>
      <el-form-item label="条件类型:" v-if="flowConditionInfo.flowType === 'condition'">
        <el-select v-model="flowConditionInfo.conditionType">
          <el-option label="表达式" value="expression"/>
          <el-option label="脚本" value="script"/>
        </el-select>
      </el-form-item>
      <el-form-item label="表达式:" v-if="flowConditionInfo.conditionType && flowConditionInfo.conditionType === 'expression'">
        <el-input v-model="flowConditionInfo.body" clearable @change="updateFlowCondition"/>
      </el-form-item>
      <template v-if="flowConditionInfo.conditionType && flowConditionInfo.conditionType === 'script'">
        <el-form-item label="脚本语言:">
          <el-input v-model="flowConditionInfo.language" clearable @change="updateFlowCondition"/>
        </el-form-item>
        <el-form-item label="脚本类型:">
          <el-select v-model="flowConditionInfo.scriptType">
            <el-option label="内联脚本" value="inline"/>
            <el-option label="外部脚本" value="external"/>
          </el-select>
        </el-form-item>
        <el-form-item label="脚本内容:" v-if="flowConditionInfo.scriptType === 'inline'">
          <el-input v-model="flowConditionInfo.body" type="textarea" clearable @change="updateFlowCondition"/>
        </el-form-item>
        <el-form-item label="资源地址:" v-if="flowConditionInfo.scriptType === 'external'">
          <el-input v-model="flowConditionInfo.resource" clearable @change="updateFlowCondition"/>
        </el-form-item>
      </template>
    </el-form>
  </div>
</template>

<script>
  import processHelper from '../../helper/ProcessHelper'

  export default {
    name: 'Template',
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
        activeElementSource: null,
        activeElementSourceRef: null,
        flowConditionInfo: {
          flowType: '',       // 流转类型
          conditionType: '',  // 条件类型
          scriptType: '',     // 脚本类型
          body: '',           // 表达式/脚本内容
          resource: '',       // 资源地址
          language: ''        // 脚本语言
        }
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            this.initFlowCondition()
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      initFlowCondition() {
        this.activeElementSource = this.activeElement.source
        this.activeElementSourceRef = this.activeElement.businessObject.sourceRef
        if (this.activeElementSourceRef && this.activeElementSourceRef.default && this.activeElementSourceRef.default.id === this.activeElement.id) {
          // 默认流转路径
          this.flowConditionInfo = { flowType: 'default' }
        } else if (!this.activeElement.businessObject.conditionExpression) {
          // 普通流转路径
          this.flowConditionInfo = { flowType: 'normal' }
        } else {
          // 条件流转路径
          const conditionExpression = this.activeElement.businessObject.conditionExpression
          this.flowConditionInfo = { ...conditionExpression, flowType: 'condition' }

          if (this.flowConditionInfo.resource) {
            this.$set(this.flowConditionInfo, 'conditionType', 'script')
            this.$set(this.flowConditionInfo, 'scriptType', 'external')
            return
          }
          if (conditionExpression.language) {
            this.$set(this.flowConditionInfo, 'conditionType', 'script')
            this.$set(this.flowConditionInfo, 'scriptType', 'inline')
            return
          }
          this.$set(this.flowConditionInfo, 'conditionType', 'expression')
        }
      },
      updateFlowType(flowType) {
        switch (flowType) {
          case 'default':  // 默认流转路径
            processHelper.updateProperties(this.activeElement, { conditionExpression: null })
            processHelper.updateProperties(this.activeElementSource, { default: this.activeElement })
            break
          case 'condition': // 条件流转路径
            // eslint-disable-next-line no-case-declarations
            const flowConditionRef = processHelper.createElement('bpmn:FormalExpression')
            processHelper.updateProperties(this.activeElement, { conditionExpression: flowConditionRef })
            break
          default:         // 普通流转路径
            if (this.activeElementSourceRef.default && this.activeElementSourceRef.default?.id === this.activeElement?.id) {
              processHelper.updateProperties(this.activeElementSource, { default: null })
            }
            processHelper.updateProperties(this.activeElement, { conditionExpression: null })
            break
        }
      },
      updateFlowCondition() {
        let conditionElement
        let { conditionType, scriptType, body, resource, language } = this.flowConditionInfo
        if (conditionType === 'expression') {
          conditionElement = processHelper.createElement('bpmn:FormalExpression', { body })
        } else {
          if (scriptType === 'inline') {
            conditionElement = processHelper.createElement('bpmn:FormalExpression', { body, language })
            this.$set(this.flowConditionInfo, 'resource', '')
          } else {
            this.$set(this.flowConditionInfo, 'body', '')
            conditionElement = processHelper.createElement('bpmn:FormalExpression', { resource, language })
          }
        }
        processHelper.updateProperties(this.activeElement, { conditionExpression: conditionElement })
      }
    }
  }
</script>
<style lang='scss'>
</style>
