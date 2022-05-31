<template>
  <div class="properties-item__content">
    <el-form v-model="userTaskInfo" :inline="false" label-width="100px" size="small" label-position="right">
      <el-form-item label="执行用户:">
        <el-input v-model="userTaskInfo.assignee" @change="updateUserTaskInfo('assignee')"/>
      </el-form-item>
      <el-form-item label="候选用户:">
        <el-input v-model="userTaskInfo.candidateUsers" @change="updateUserTaskInfo('candidateUsers')"/>
      </el-form-item>
      <el-form-item label="候选分组:">
        <el-input v-model="userTaskInfo.candidateGroups" @change="updateUserTaskInfo('candidateGroups')"/>
      </el-form-item>
      <el-form-item label="到期时间:">
        <el-input v-model="userTaskInfo.dueDate" @change="updateUserTaskInfo('dueDate')"/>
      </el-form-item>
      <el-form-item label="跟踪时间:">
        <el-input v-model="userTaskInfo.followUpDate" @change="updateUserTaskInfo('followUpDate')"/>
      </el-form-item>
      <el-form-item label="优先级:">
        <el-input v-model="userTaskInfo.priority" @change="updateUserTaskInfo('priority')"/>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import processHelper from '../../helper/ProcessHelper'

  export default {
    name: 'UserTask',
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
        userTaskInfo: {
          assignee: '',        // 执行用户
          candidateUsers: '',  // 候选用户
          candidateGroups: '', // 候选分组
          dueDate: '',         // 到期时间
          followUpDate: '',    // 跟踪时间
          priority: ''         // 优先级
        }
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            let businessObject = this.activeElement.businessObject
            this.userTaskInfo = JSON.parse(JSON.stringify(businessObject))
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      updateUserTaskInfo(key) {
        const properties = Object.create(null)
        properties[key] = this.userTaskInfo[key]
        processHelper.updateProperties(this.activeElement, properties)
      }
    }
  }
</script>
<style lang='scss'>
</style>
