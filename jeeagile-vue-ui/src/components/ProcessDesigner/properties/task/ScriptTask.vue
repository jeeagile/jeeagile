<template>
  <div class="properties-item__content">
    <el-form v-model="scriptTaskInfo" :inline="false" label-width="100px" size="small" label-position="right">
      <el-form-item label="脚本格式:">
        <el-input v-model="scriptTaskInfo.scriptFormat" @change="updateScriptTaskInfo('scriptFormat')"/>
      </el-form-item>
      <el-form-item label="脚本类型:">
        <el-select v-model="scriptTaskInfo.scriptType">
          <el-option label="内联脚本" value="inline"/>
          <el-option label="外部资源" value="external"/>
        </el-select>
      </el-form-item>
      <el-form-item v-show="scriptTaskInfo.scriptType === 'inline'" label="脚本:">
        <el-input v-model="scriptTaskInfo.script" type="textarea" resize="vertical" clearable
                  :autosize="{ minRows: 2, maxRows: 4 }" @change="updateScriptTaskInfo('script')"
        />
      </el-form-item>
      <el-form-item v-show="scriptTaskInfo.scriptType === 'external'" label="资源地址:">
        <el-input v-model="scriptTaskInfo.resource" clearable @change="updateScriptTaskInfo('resource')"/>
      </el-form-item>
      <el-form-item label="结果变量">
        <el-input v-model="scriptTaskInfo.resultVariable" clearable @change="updateScriptTaskInfo('resultVariable')"/>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import processHelper from '../../helper/ProcessHelper'

  export default {
    name: 'ScriptTask',
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
        scriptTaskInfo: {
          scriptFormat: '',  // 脚本格式
          scriptType: '',    // 脚本类型
          script: '',        // 脚本
          resource: '',      // 资源地址
          resultVariable: '' // 结果变量
        }
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            let businessObject = this.activeElement.businessObject
            this.scriptTaskInfo = JSON.parse(JSON.stringify(businessObject))
            this.initScriptType()
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      initScriptType() {
        this.$set(this.scriptTaskInfo, 'scriptType', this.scriptTaskInfo.script ? 'inline' : 'external')
      },
      updateScriptTaskInfo(key) {
        const properties = Object.create(null)
        properties.scriptFormat = this.scriptTaskInfo.scriptFormat || null
        properties.resultVariable = this.scriptTaskInfo.resultVariable || null
        if (this.scriptTaskInfo.scriptType === 'inline') {
          properties.script = this.scriptTaskInfo.script || null
          properties.resource = null
        } else {
          properties.resource = this.scriptTaskInfo.resource || null
          properties.script = null
        }
        processHelper.updateProperties(this.activeElement, properties)
      }
    }
  }
</script>
<style lang='scss'>
</style>
