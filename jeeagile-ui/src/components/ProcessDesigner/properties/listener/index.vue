<template>
  <div class="properties-item__content">
    <template>
      <el-divider>执行监听器</el-divider>
      <execution-listener :process-modeler="processModeler" :process-info="processInfo" :active-element="activeElement"/>
    </template>

    <template>
      <el-divider>任务监听器</el-divider>
      <task-listener :process-modeler="processModeler" :process-info="processInfo" :active-element="activeElement"/>
    </template>
  </div>
</template>

<script>
  import ExecutionListener from './ExecutionListener'
  import TaskListener from './TaskListener'
  import { ListenerVisible } from '../properties'

  export default {
    name: 'Listener',
    components: { ExecutionListener, TaskListener },
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
        listenerVisible: ListenerVisible,
        executionVisible: false,
        taskVisible: false
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            const elementType = this.activeElement.type
            this.executionVisible = this.listenerVisible[elementType]?.executionVisible
            this.taskVisible = this.listenerVisible[elementType]?.executionVisible
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
