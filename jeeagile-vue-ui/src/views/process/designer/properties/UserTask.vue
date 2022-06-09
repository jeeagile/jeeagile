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
        <el-input v-model="candidateGroupsName">
          <i slot="append" class="el-icon-s-check" @click="handleSelectGroup"/>
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
    <el-dialog :visible.sync="userVisible" v-if="userVisible" title="选择用户" width="800px" append-to-body>
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
      <el-table v-loading="userLoading" ref="userTable" :row-key="handleSelectionUserTableKey" :data="userList"
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

    <el-dialog :visible.sync="groupVisible" v-if="groupVisible" title="选择候选分组" width="800px" append-to-body>
      <el-form :model="groupQueryParam" ref="groupQueryParam" :inline="true">
        <el-row>
          <el-form-item label="候选分组类型:">
            <el-radio-group v-model="groupType" @change="handleGroupType">
              <el-radio label="role">角色</el-radio>
              <el-radio label="dept">部门</el-radio>
              <el-radio label="post">岗位</el-radio>
              <el-radio label="group">分组</el-radio>
              <el-radio label="script">脚本</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-row>
        <el-row>
          <template v-if="groupType=='role'">
            <el-form-item label="角色编码:" prop="roleCode">
              <el-input
                v-model="groupQueryParam.data.roleCode"
                placeholder="请输入角色编码"
                clearable
                size="small"
                style="width: 150px"
                @keyup.enter.native="handleGroupQuery"
              />
            </el-form-item>
            <el-form-item label="角色名称:" prop="roleName">
              <el-input
                v-model="groupQueryParam.data.roleName"
                placeholder="请输入角色名称"
                clearable
                size="small"
                style="width: 150px"
                @keyup.enter.native="handleGroupQuery"
              />
            </el-form-item>
          </template>
          <template v-if="groupType=='post'">
            <el-form-item label="岗位编码:" prop="postCode">
              <el-input
                v-model="groupQueryParam.data.postCode"
                placeholder="请输入岗位编码"
                clearable
                size="small"
                style="width: 150px"
                @keyup.enter.native="handleGroupQuery"
              />
            </el-form-item>
            <el-form-item label="岗位名称:" prop="postName">
              <el-input
                v-model="groupQueryParam.data.postName"
                placeholder="请输入岗位名称"
                clearable
                size="small"
                style="width: 150px"
                @keyup.enter.native="handleGroupQuery"
              />
            </el-form-item>
          </template>
          <template v-if="groupType=='dept'">
            <el-form-item label="部门编码:" prop="deptCode">
              <el-input
                v-model="groupQueryParam.data.deptCode"
                placeholder="请输入部门编码"
                clearable
                size="small"
                style="width: 150px"
                @keyup.enter.native="handleGroupQuery"
              />
            </el-form-item>
            <el-form-item label="部门名称:" prop="deptName">
              <el-input
                v-model="groupQueryParam.data.deptName"
                placeholder="请输入部门名称"
                clearable
                size="small"
                style="width: 150px"
                @keyup.enter.native="handleGroupQuery"
              />
            </el-form-item>
          </template>
          <template v-if="groupType=='group'">
            <el-form-item label="用户分组编码:" prop="groupCode">
              <el-input
                v-model="groupQueryParam.data.groupCode"
                placeholder="请输入用户分组编码"
                clearable
                size="small"
                style="width: 150px"
                @keyup.enter.native="handleGroupQuery"
              />
            </el-form-item>
            <el-form-item label="用户分组名称:" prop="groupName">
              <el-input
                v-model="groupQueryParam.data.groupName"
                placeholder="请输入用户分组名称"
                clearable
                size="small"
                style="width: 150px"
                @keyup.enter.native="handleGroupQuery"
              />
            </el-form-item>
          </template>
          <template v-if="groupType=='script'">
            <el-form-item label="脚本编码:" prop="scriptCode">
              <el-input
                v-model="groupQueryParam.data.userName"
                placeholder="请输入脚本编码"
                clearable
                size="small"
                style="width: 150px"
                @keyup.enter.native="handleGroupQuery"
              />
            </el-form-item>
            <el-form-item label="脚本名称:" prop="scriptName">
              <el-input
                v-model="groupQueryParam.data.scriptName"
                placeholder="请输入脚本名称"
                clearable
                size="small"
                style="width: 150px"
                @keyup.enter.native="handleGroupQuery"
              />
            </el-form-item>
          </template>
          <el-form-item>
            <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleGroupQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetGroupQuery">重置</el-button>
          </el-form-item>
        </el-row>
      </el-form>
      <el-table v-loading="groupLoading" ref="groupTable" :row-key="handleSelectionGroupTableKey" :data="groupList"
                @selection-change="handleSelectionGroupChange">
        <el-table-column type="selection" :reserve-selection="true" width="50" align="center"/>
        <template v-if="groupType=='role'">
          <el-table-column label="角色编码" align="center" prop="roleCode" :show-overflow-tooltip="true"/>
          <el-table-column label="角色名称" align="center" prop="roleName" :show-overflow-tooltip="true"/>
        </template>
        <template v-if="groupType=='post'">
          <el-table-column label="岗位编码" align="center" prop="postCode" :show-overflow-tooltip="true"/>
          <el-table-column label="岗位名称" align="center" prop="postName" :show-overflow-tooltip="true"/>
        </template>
        <template v-if="groupType=='dept'">
          <el-table-column label="部门编码" align="center" prop="deptCode" :show-overflow-tooltip="true"/>
          <el-table-column label="部门名称" align="center" prop="deptName" :show-overflow-tooltip="true"/>
        </template>
        <template v-if="groupType=='group'">
          <el-table-column label="分组编码" align="center" prop="groupCode" :show-overflow-tooltip="true"/>
          <el-table-column label="分组名称" align="center" prop="groupName" :show-overflow-tooltip="true"/>
        </template>
        <template v-if="groupType=='script'">
          <el-table-column label="脚本编码" align="center" prop="scriptCode" :show-overflow-tooltip="true"/>
          <el-table-column label="脚本名称" align="center" prop="scriptName" :show-overflow-tooltip="true"/>
        </template>
      </el-table>
      <pagination v-show="groupQueryParam.pageTotal>0" :total-page="groupQueryParam.pageTotal"
                  :current-page.sync="groupQueryParam.currentPage" :limit.sync="groupQueryParam.pageSize"
                  :page-size="groupQueryParam.pageSize" :page-sizes="[5,10]" @pagination="getGroupList"
      />
      <div slot="footer">
        <el-button size="mini" type="primary" @click="submitSelectGroup">确 认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import processHelper from '@/components/ProcessDesigner/helper/ProcessHelper'
  import {
    selectUserPage,
    selectRolePage,
    selectDeptPage,
    selectPostPage,
    selectGroupPage,
    selectScriptPage,
    detailUserNickName,
    detailRoleName,
    detailDeptName,
    detailPostName,
    detailGroupName,
    detailScriptName
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
        userVisible: false,
        userLoading: false,
        userType: 'assignee',
        userList: [],
        assigneeNickName: undefined,
        candidateNickName: undefined,
        candidateGroupsName: undefined,

        groupVisible: false,
        groupLoading: false,
        groupType: 'post',
        groupList: [],
        selectGroupList: [],
        groupQueryParam: {
          pageTotal: 0,
          pageSize: 5,
          currentPage: 1,
          data: {
            roleCode: undefined,
            roleName: undefined,
            postCode: undefined,
            postName: undefined,
            deptCode: undefined,
            deptName: undefined,
            groupCode: undefined,
            groupName: undefined,
            scriptCode: undefined,
            scriptName: undefined
          }
        },

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
            this.initCandidateGroups()
          }
        },
        deep: true,
        immediate: true
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
      initCandidateGroups() {
        if (this.userTaskInfo.candidateGroups) {
          this.groupType = this.userTaskInfo.candidateGroups.split(':')[0]
          const candidateGroups = this.userTaskInfo.candidateGroups.split(':')[1]
          switch (this.groupType) {
          case 'role':
            detailRoleName(candidateGroups).then(response => {
              this.candidateGroupsName = response.data.toString()
            })
            break
          case 'post':
            detailPostName(candidateGroups).then(response => {
              this.candidateGroupsName = response.data.toString()
            })
            break
          case 'dept':
            detailDeptName(candidateGroups).then(response => {
              this.candidateGroupsName = response.data.toString()
            })
            break
          case 'group':
            detailGroupName(candidateGroups).then(response => {
              this.candidateGroupsName = response.data.toString()
            })
            break
          case 'script':
            detailScriptName(candidateGroups).then(response => {
              this.candidateGroupsName = response.data.toString()
            })
            break
          default:
            this.candidateGroupsName = undefined
          }
        } else {
          this.candidateGroupsName = undefined
        }
      },
      /** 查询用户列表 */
      getUserList() {
        this.userLoading = true
        selectUserPage(this.userQueryParam).then(response => {
            this.userQueryParam.pageTotal = response.data.pageTotal
            this.userList = response.data.records
            this.userLoading = false
          }
        )
      },
      updateUserTaskInfo(key) {
        const properties = Object.create(null)
        properties[key] = this.userTaskInfo[key]
        if (key === 'candidateGroups' && this.groupType?.length > 0) {
          properties[key] = this.groupType + ':' + this.userTaskInfo[key]
        }
        processHelper.updateProperties(this.activeElement, properties)
      },
      handleSelectUser(userType) {
        this.userType = userType
        this.selectUserList = []
        this.userVisible = true
        this.$nextTick(() => {
          this.$refs.userTable.clearSelection()
          if (this.userType === 'assignee') {
            if (this.userTaskInfo.assignee) {
              this.userList.map(row => {
                if (row.id === this.userTaskInfo.assignee) {
                  this.$refs.userTable.toggleRowSelection(row, true)
                }
              })
            }
          } else {
            this.userTaskInfo.candidateUsers.split(',').map(userId => {
              if (userId) {
                this.userList.map(row => {
                  if (row.id === userId) {
                    this.$refs.userTable.toggleRowSelection(row, true)
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
            this.$refs.userTable.clearSelection()
            this.$refs.userTable.toggleRowSelection(selection.pop())
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
        this.resetForm('userQueryParam')
        this.handleUserQuery()
      },
      /** 选择分组 */
      handleSelectGroup() {
        this.groupVisible = true
        this.getGroupList()
      },
      /** 处理选择分组 */
      handleGroupType() {
        this.$refs.groupTable.clearSelection()
        this.getGroupList()
      },
      /** 获取分组列表 */
      getGroupList() {
        switch (this.groupType) {
        case 'role':
          this.getRoleList()
          break
        case 'post':
          this.getPostList()
          break
        case 'dept':
          this.getDeptList()
          break
        case 'group':
          this.getUsrGroupList()
          break
        case 'script':
          this.getScriptList()
          break
        default:
          this.getPostList()
        }
      },
      getRoleList() {
        this.groupLoading = true
        selectRolePage(this.queryParam).then(response => {
            this.groupList = response.data?.records.map(item => {
              return { ...item, tableKey: item.roleCode, label: item.roleName }
            })
            this.groupQueryParam.pageTotal = response.data?.pageTotal
            this.groupLoading = false
          }
        )
      },
      getDeptList() {
        this.groupLoading = true
        selectDeptPage(this.queryParam).then(response => {
            this.groupList = response.data?.records.map(item => {
              return { ...item, tableKey: item.deptCode, label: item.deptName }
            })
            this.groupQueryParam.pageTotal = response.data?.pageTotal
            this.groupLoading = false
          }
        )
      },
      getPostList() {
        this.groupLoading = true
        selectPostPage(this.queryParam).then(response => {
            this.groupList = response.data?.records.map(item => {
              return { ...item, tableKey: item.postCode, label: item.postName }
            })
            this.groupQueryParam.pageTotal = response.data?.pageTotal
            this.groupLoading = false
          }
        )
      },
      getUsrGroupList() {
        this.groupLoading = true
        selectGroupPage(this.queryParam).then(response => {
            this.groupList = response.data?.records.map(item => {
              return { ...item, tableKey: item.groupCode, label: item.groupName }
            })
            this.groupQueryParam.pageTotal = response.data?.pageTotal
            this.groupLoading = false
          }
        )
      },
      getScriptList() {
        this.groupLoading = true
        selectScriptPage(this.queryParam).then(response => {
            this.groupList = response.data?.records.map(item => {
              return { ...item, tableKey: item.scriptCode, label: item.scriptName }
            })
            this.groupQueryParam.pageTotal = response.data?.pageTotal
            this.groupLoading = false
          }
        )
      },
      handleSelectionGroupTableKey(row) {
        return row?.tableKey
      },
      handleSelectionGroupChange(selection) {
        this.selectGroupList = selection
      },
      submitSelectGroup() {
        this.candidateGroupsName = this.selectGroupList.map(item => item.label).toString()
        this.userTaskInfo.candidateGroups = this.selectGroupList.map(item => item.tableKey).toString()
        this.updateUserTaskInfo('candidateGroups')
        this.groupVisible = false
      },
      /** 搜索按钮操作 */
      handleGroupQuery() {
        this.groupQueryParam.page = 1
        this.getGroupList()
      },
      /** 重置按钮操作 */
      resetGroupQuery() {
        this.resetForm('userGroupParam')
        this.handleGroupQuery()
      }
    }
  }
</script>
<style lang='scss'>
  .el-input-group__append, .el-input-group__prepend {
    padding: 0 12px
  }
</style>
