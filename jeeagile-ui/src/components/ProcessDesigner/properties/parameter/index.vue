<template>
  <div class="properties-item__content">
    <template>
      <el-divider>输入参数</el-divider>
      <input-parameter :process-modeler="processModeler" :process-info="processInfo" :active-element="activeElement"/>
    </template>

    <template>
      <el-divider>输出参数</el-divider>
      <output-parameter :process-modeler="processModeler" :process-info="processInfo" :active-element="activeElement"/>
    </template>
  </div>
</template>

<script>
  import InputParameter from './input'
  import OutputParameter from './output'
  import { ParameterVisible } from '../properties'

  export default {
    name: 'Listener',
    components: { InputParameter, OutputParameter },
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
        parameterVisible: ParameterVisible,
        inputVisible: false,
        outputVisible: false
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            const elementType = this.activeElement.type
            this.inputVisible = this.parameterVisible[elementType]?.inputVisible
            this.outputVisible = this.parameterVisible[elementType]?.outputVisible
          }
        },
        deep: true,
        immediate: true
      }
    }
  }
</script>
<style lang='scss'>
</style>
