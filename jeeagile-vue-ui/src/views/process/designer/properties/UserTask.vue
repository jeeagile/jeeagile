<template>
  <div class="properties-item__content">
    <el-form v-model="userTaskInfo" :inline="false" label-width="100px" size="small" label-position="right">
      <el-form-item label="执行用户:">
        <el-input v-model="assigneeNickName">
          <i slot="append" class="el-icon-user" @click="handleSelectUser('assignee')"/>
        </el-input>
      </el-form-item>
      <el-form-item label="候选用户:">
        <el-input v-model="candidateNickName">
          <i slot="append" class="el-icon-user" @click="handleSelectUser('candidateUsers')"/>
        </el-input>
      </el-form-item>
      <el-form-item label="候选分组:">
        <el-input v-model="userTaskInfo.candidateGroups" @change="updateUserTaskInfo('candidateGroups')">
          <!--          <el-select v-model="candidateType" slot="prepend" style="width: 75px;" @change="handlerCandidateTypeChange">-->
          <!--            <el-option label="角色" value="role"></el-option>-->
          <!--            <el-option label="岗位" value="post"></el-option>-->
          <!--            <el-option label="分组" value="group"></el-option>-->
          <!--            <el-option label="部门" value="dept"></el-option>-->
          <!--            <el-option label="脚本" value="script"></el-option>-->
          <!--          </el-select>-->
          <!--          <i slot="append" class="el-icon-s-check" @click="showSelectGroup"/>-->
        </el-input>
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
    <el-dialog :visible.sync="userVisible" v-if="userVisible" title="选择用户" width="800px"
               append-to-body>
      <el-form :model="userQueryParam" ref="queryForm" :inline="true">
        <el-form-item label="用户名称:" prop="userName">
          <el-input v-model="userQueryParam.queryCond.userName" clearable size="small" style="width: 150px"
                    placeholder="请输入用户名称" @keyup.enter.native="handleUserQuery"/>
        </el-form-item>
        <el-form-item label="用户昵称:" prop="nickName">
          <el-input v-model="userQueryParam.queryCond.nickName" clearable size="small" style="width: 150px"
                    placeholder="请输入用户昵称" @keyup.enter.native="handleUserQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleUserQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetUserQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="userLoading" ref="selectUserTable" :row-key="handleSelectionUserTableKey" :data="userList"
                @selection-change="handleSelectionUserChange">
        <el-table-column type="selection" :reserve-selection="true" width="50" align="center"/>
        <el-table-column label="用户名称" align="center" prop="userName" :show-overflow-tooltip="true"/>
        <el-table-column label="用户昵称" align="center" prop="nickName" :show-overflow-tooltip="true"/>
      </el-table>

      <pagination v-show="userQueryParam.pageTotal>0" :total-page="userQueryParam.pageTotal"
                  :current-page.sync="userQueryParam.currentPage" :limit.sync="userQueryParam.pageSize"
                  :page-size="userQueryParam.pageSize" :page-sizes="[5,10]" @pagination="getUserList"
      />
      <div slot="footer">
        <el-button size="mini" type="primary" @click="submitSelectUser">确 认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import processHelper from '@/components/ProcessDesigner/helper/ProcessHelper'
  import {
    selectUserPage,
    detailUserNickName
  } from '@/api/process/designer'

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
        selectUserList: [],
        userQueryParam: {
          pageTotal: 0,
          pageSize: 5,
          currentPage: 1,
          queryCond: {
            userName: undefined,
            nickName: undefined
          }
        },
        userLoading: false,
        userVisible: false,
        userType: 'assignee',
        userList: [],
        assigneeNickName: undefined,
        candidateNickName: undefined,
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
            this.initAssigneeUser()
            this.initCandidateUsers()
          }
        }
      }
    },
    created() {
      this.getUserList()
    },
    methods: {
      initAssigneeUser() {
        if (this.userTaskInfo.assignee) {
          detailUserNickName(this.userTaskInfo.assignee).then(response => {
              this.assigneeNickName = response.data.toString()
            }
          )
        } else {
          this.assigneeNickName = undefined
        }
      },
      initCandidateUsers() {
        if (this.userTaskInfo.candidateUsers) {
          detailUserNickName(this.userTaskInfo.candidateUsers).then(response => {
              this.candidateNickName = response.data.toString()
            }
          )
        } else {
          this.candidateNickName = undefined
        }
      },
      /** 查询用户列表 */
      getUserList() {
        this.userLoading = true
        selectUserPage(this.userQueryParam).then(response => {
            this.userQueryParam.pageTotal = response.data.pageTotal
            this.userList = response.data.recordList
            this.userLoading = false
          }
        )
      },
      updateUserTaskInfo(key) {
        const properties = Object.create(null)
        properties[key] = this.userTaskInfo[key]
        processHelper.updateProperties(this.activeElement, properties)
      },
      handleSelectUser(userType) {
        this.userType = userType
        this.selectUserList = []
        this.userVisible = true
        this.$nextTick(() => {
          this.$refs.selectUserTable.clearSelection()
          if (this.userType === 'assignee') {
            if (this.userTaskInfo.assignee) {
              this.userList.map(row => {
                if (row.id === this.userTaskInfo.assignee) {
                  this.$refs.selectUserTable.toggleRowSelection(row, true)
                }
              })
            }
          } else {
            this.userTaskInfo.candidateUsers.split(',').map(userId => {
              if (userId) {
                this.userList.map(row => {
                  if (row.id === userId) {
                    this.$refs.selectUserTable.toggleRowSelection(row, true)
                  }
                })
              }
            })
          }
        })
      },
      handleSelectionUserTableKey(row) {
        return row.id
      },
      handleSelectionUserChange(selection) {
        if (this.userType === 'assignee') {
          this.selectUserList = []
          this.selectUserList.push(selection[0])
          if (selection.length > 1) {
            this.$refs.selectUserTable.clearSelection()
            this.$refs.selectUserTable.toggleRowSelection(selection.pop())
          }
        } else {
          this.selectUserList = selection
        }
      },
      submitSelectUser() {
        if (this.userType === 'assignee') {
          this.assigneeNickName = this.selectUserList[0]?.nickName
          this.userTaskInfo.assignee = this.selectUserList[0]?.id
          this.updateUserTaskInfo('assignee')
        } else {
          this.candidateNickName = (this.selectUserList.map(item => item.nickName)).toString()
          this.userTaskInfo.candidateUsers = (this.selectUserList.map(item => item.id)).toString()
          this.updateUserTaskInfo('candidateUsers')
        }
        this.userVisible = false
      },
      /** 搜索按钮操作 */
      handleUserQuery() {
        this.userQueryParam.page = 1
        this.getUserList()
      },
      /** 重置按钮操作 */
      resetUserQuery() {
        this.resetForm('queryUserForm')
        this.handleUserQuery()
      }
    }
  }
</script>
<style lang='scss'>
  .el-input-group__append, .el-input-group__prepend {
    padding: 0 12px
  }
</style>
