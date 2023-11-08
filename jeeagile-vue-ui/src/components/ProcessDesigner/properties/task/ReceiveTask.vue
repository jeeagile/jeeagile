<template>
  <div class="properties-item__content">
    <el-form v-model="receiveTaskInfo" :inline="false" label-width="100px" size="small" label-position="right">
      <el-form-item label="消息实例:">
        <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: nowrap">
          <el-select v-model="receiveTaskInfo.messageId" @change="updateReceiveTaskInfo">
            <el-option v-for="id in Object.keys(messageOptions)" :key="id" :value="id" :label="messageOptions[id]"/>
          </el-select>
          <el-button size="mini" type="primary" icon="el-icon-plus" style="margin-left: 5px" @click="openMessageInfo"/>
        </div>
      </el-form-item>
    </el-form>

    <el-dialog :visible.sync="messageVisible" title="创建新消息" width="450px" destroy-on-close append-to-body>
      <el-form :model="newMessageInfo" size="mini" label-width="90px" @submit.native.prevent>
        <el-form-item label="消息ID:">
          <el-input v-model="newMessageInfo.id" clearable/>
        </el-form-item>
        <el-form-item label="消息名称:">
          <el-input v-model="newMessageInfo.name" clearable/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="mini" type="primary" @click="createNewMessageInfo">确 认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import processHelper from '../../helper/ProcessHelper'

  export default {
    name: 'ReceiveTask',
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
        messageVisible: false,
        messageOptions: {},
        messageRefsMap: {},
        newMessageInfo: {
          id: '',    // 编号
          name: ''   // 名称
        },
        receiveTaskInfo: {
          messageId: null
        }
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            this.initMessageOptions()
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      initMessageOptions() {
        const rootElements = this.processModeler.getDefinitions().rootElements
        rootElements.filter(element => element.$type === 'bpmn:Message')
          .forEach(messageElement => {
            this.messageRefsMap[messageElement.id] = messageElement
            this.$set(this.messageOptions, messageElement.id, messageElement.name)
          })
        this.$set(this.messageOptions, '-1', '无')
      },
      openMessageInfo() {
        this.messageVisible = true
        this.newMessageInfo = {}
      },
      createNewMessageInfo() {
        if (this.messageOptions[this.newMessageInfo.id]) {
          this.$message.error('该消息实例已存在，请核对消息ID！')
          return
        }
        const messageElement = processHelper.createElement('bpmn:Message', this.newMessageInfo)
        this.processModeler.getDefinitions().rootElements.push(messageElement)
        this.$set(this.messageOptions, this.newMessageInfo.id, this.newMessageInfo.name)
        this.messageRefsMap[this.newMessageInfo.id] = messageElement
        this.messageVisible = false
      },
      updateReceiveTaskInfo() {
        const properties = Object.create(null)
        if (this.receiveTaskInfo.messageId === '-1') {
          properties.messageRef = null
        } else {
          properties.messageRef = this.messageRefsMap[this.receiveTaskInfo.messageId]
        }
        processHelper.updateProperties(this.activeElement, properties)
      }
    }
  }
</script>
<style lang='scss'>
</style>
