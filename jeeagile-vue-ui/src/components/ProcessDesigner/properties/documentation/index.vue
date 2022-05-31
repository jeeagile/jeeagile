<template>
  <div class="properties-item__content">
    <el-form :inline="false" size="small" label-width="100px" label-position="right">
      <el-form-item label="元素文档:">
        <el-input type="textarea" v-model="documentation" size="mini" resize="vertical"
                  :autosize="{ minRows: 2, maxRows: 4 }" @change="updateDocumentation"/>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
  import processHelper from '../../helper/ProcessHelper'

  export default {
    name: 'Documentation',
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
        documentation: null
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            const documentations = this.activeElement.businessObject?.documentation
            this.documentation = documentations && documentations.length ? documentations[0].text : ''
          } else {
            this.documentation = ''
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      updateDocumentation() {
        const properties = { text: this.documentation }
        const documentation = processHelper.createElement('bpmn:Documentation', properties)
        const documentationProperties = { documentation: [documentation] }
        processHelper.updateProperties(this.activeElement, documentationProperties)
      }
    }
  }
</script>
<style lang='scss'>
</style>
