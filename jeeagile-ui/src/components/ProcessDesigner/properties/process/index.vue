<template>
  <div class="properties-item__content">
    <el-form v-if="processRef" v-model="processRefInfo" :inline="false" label-width="100px" size="small"
             label-position="right">
      <el-form-item label="编号:">
        <el-input v-model="processRefInfo.id" @change="updateProcessRefInfo('id')"/>
      </el-form-item>
      <el-form-item label="名称:">
        <el-input v-model="processRefInfo.name" @change="updateProcessRefInfo('name')"/>
      </el-form-item>
      <el-form-item label="版本标签:">
        <el-input v-model="processRefInfo.versionTag" @change="updateProcessRefInfo('versionTag')"/>
      </el-form-item>
      <el-form-item label="可执行:">
        <el-switch v-model="processRefInfo.isExecutable" active-text="是" inactive-text="否"
                   @change="updateProcessRefInfo('isExecutable')"/>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    name: 'Process',
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
        processRef: null,
        processRefInfo: {
          id: '',          // 编号
          name: '',        // 名称
          versionTag: '',  // 版本标签
          isExecutable: '' // 可执行
        }
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            const businessObject = this.activeElement.businessObject
            this.processRef = businessObject.processRef
            if (!this.processRef) return
            this.processRefInfo = JSON.parse(JSON.stringify(this.processRef))
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      updateProcessRefInfo(key) {
        this.activeElement.businessObject.processRef[key] = this.processRefInfo[key]
      }
    }
  }
</script>
<style lang='scss'>
</style>
