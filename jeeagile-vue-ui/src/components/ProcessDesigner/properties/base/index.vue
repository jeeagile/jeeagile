<template>
  <div class="properties-item__content">
    <el-form v-model="baseInfo" :inline="false" label-width="100px" size="small" label-position="right">
      <el-form-item label="编号:">
        <el-input v-model="baseInfo.id" @change="updateBaseInfo('id')"/>
      </el-form-item>
      <el-form-item label="名称:" v-if="baseInfo.$type != 'bpmn:TextAnnotation'">
        <el-input v-model="baseInfo.name" @change="updateBaseInfo('name')"/>
      </el-form-item>
      <el-form-item label="Text:" v-if="baseInfo.$type === 'bpmn:TextAnnotation'">
        <el-input type="textarea" v-model="baseInfo.text" size="mini" resize="vertical"
                  :autosize="{ minRows: 2, maxRows: 4 }" @change="updateBaseInfo('text')"/>
      </el-form-item>
      <template v-if="baseInfo.$type === 'bpmn:Process'">
        <el-form-item label="版本标签:">
          <el-input v-model="baseInfo.versionTag" @change="updateBaseInfo('versionTag')"/>
        </el-form-item>
        <el-form-item label="可执行:">
          <el-switch v-model="baseInfo.isExecutable" active-text="是" inactive-text="否"
                     @change="updateBaseInfo('isExecutable')"/>
        </el-form-item>
      </template>
    </el-form>
  </div>
</template>

<script>
  import processHelper from '../../helper/ProcessHelper'

  export default {
    name: 'BaseInfo',
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
        processData: this.processInfo,
        baseInfo: {
          id: '',          // 编号
          name: '',        // 名称
          versionTag: '',  // 版本标签
          isExecutable: '' // 可执行
        }
      }
    },
    watch: {
      activeElement: {
        handler(newElement, oldElement) {
          if (this.activeElement) {
            const businessObject = this.activeElement.businessObject
            this.baseInfo = JSON.parse(JSON.stringify(businessObject))
            if (this.baseInfo.$type != 'bpmn:TextAnnotation') {
              if (!this.baseInfo.name || this.baseInfo.name === '') {
                this.$set(this.baseInfo, 'name', this.processInfo.elementName)
              }
              if (newElement?.id === oldElement?.id) {
                if (newElement.type !== oldElement.type) {
                  this.$set(this.baseInfo, 'name', this.processInfo.elementName)
                }
                if (newElement.businessObject?.eventDefinitions !== oldElement.businessObject?.eventDefinitions) {
                  this.$set(this.baseInfo, 'name', this.processInfo.elementName)
                }
              }
              this.updateBaseInfo('name')
            }
            if (this.baseInfo.$type == 'bpmn:Process') {
              this.$set(this.baseInfo, 'versionTag', 'v' + this.processInfo.processVersion)
            }
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      updateBaseInfo(key) {
        this.updateProcessInfo()
        const properties = Object.create(null)
        properties[key] = this.baseInfo[key]
        processHelper.updateProperties(this.activeElement, properties)
      },
      updateProcessInfo() {
        const elementType = this.activeElement.type
        if (elementType === 'bpmn:Collaboration' || elementType === 'bpmn:Process') {
          this.processData.processCode = this.baseInfo.id
          this.processData.processName = this.baseInfo.name
        }
      }
    }
  }
</script>
<style lang='scss'>
</style>
