<template>
  <div class="properties-item__content">
    <el-form v-model="templateInfo" :inline="false" label-width="100px" size="small" label-position="right">
      <el-form-item label="模板:">
        <el-input v-model="templateInfo.id" @change="updateBaseInfo('id')"/>
      </el-form-item>
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
        templateInfo: {}
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            let businessObject = this.activeElement.businessObject
            this.templateInfo = JSON.parse(JSON.stringify(businessObject))
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      updateBaseInfo(key) {
        const properties = Object.create(null)
        properties[key] = this.templateInfo[key]
        processHelper.updateProperties(this.activeElement, properties)
      }
    }
  }
</script>
<style lang='scss'>
</style>
