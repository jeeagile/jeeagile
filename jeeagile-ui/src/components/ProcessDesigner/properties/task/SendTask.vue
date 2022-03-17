<template>
  <div class="properties-item__content">
    <el-form v-model="sendTaskInfo" :inline="false" label-width="100px" size="small" label-position="right">
      <el-form-item label="实现方式:">
        <el-select v-model="sendTaskInfo.achieveType" value-key="value">
          <el-option v-for="item in achieveTypeOptions" :key="item.value" :label="item.label" :value="item"/>
        </el-select>
      </el-form-item>
      <el-form-item v-if="sendTaskInfo.achieveType" :label="`${sendTaskInfo.achieveType.label}:`">
        <el-input v-model="sendTaskInfo.achieveValue" clearable @change="updateSendTaskInfo"/>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import processHelper from '../../helper/ProcessHelper'

  export default {
    name: 'SendTask',
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
        achieveTypeOptions: [
          {
            value: 'class',
            label: 'Java类'
          },
          {
            value: 'expression',
            label: '表达式'
          },
          {
            value: 'delegateExpression',
            label: '代理表达式'
          },
          {
            value: 'topic',
            label: '外部'
          }
        ],
        sendTaskInfo: {
          achieveType: null,
          achieveLabel: '',
          achieveValue: ''
        }
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            this.initAchieveType()
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      initAchieveType() {
        let businessObject = this.activeElement.businessObject
        if (!businessObject) return
        for (let item of this.achieveTypeOptions) {
          if (businessObject[item.value]) {
            this.sendTaskInfo.achieveType = item
            this.sendTaskInfo.achieveValue = businessObject[item.value]
          }
        }
      },
      updateSendTaskInfo() {
        const properties = Object.create(null)
        for (let item of this.achieveTypeOptions) {
          if (item.value === this.sendTaskInfo.achieveType.value) {
            properties[item.value] = this.sendTaskInfo.achieveValue
          } else {
            properties[item.value] = null
          }
        }
        processHelper.updateProperties(this.activeElement, properties)
      }
    }
  }
</script>
<style lang='scss'>
</style>
