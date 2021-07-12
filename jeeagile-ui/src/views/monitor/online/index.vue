<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParam" :inline="true" label-width="68px">
      <el-form-item label="登录地址" prop="ipAddr">
        <el-input v-model="queryParam.queryCond.ipAddr" placeholder="请输入登录地址" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="用户名称" prop="userName">
        <el-input v-model="queryParam.queryCond.userName" placeholder="请输入用户名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>

    </el-form>
    <el-row :gutter="10" class="mb8">
      <right-toolbar :show-search.sync="showSearch" @queryTable="getOnlineUserList"/>
    </el-row>
    <el-table v-loading="loading" :data="onlineList.slice((queryParam.currentPage-1)*queryParam.pageSize,queryParam.currentPage*queryParam.pageSize)" style="width: 100%;">
      <el-table-column label="序号" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ (queryParam.currentPage - 1) * queryParam.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="登录名称" align="center" prop="userName" :show-overflow-tooltip="true"/>
      <el-table-column label="部门名称" align="center" prop="deptName"/>
      <el-table-column label="主机IP" align="center" prop="loginIp" :show-overflow-tooltip="true"/>
      <el-table-column label="登录地址" align="center" prop="loginAddress" :show-overflow-tooltip="true"/>
      <el-table-column label="登录设备" align="center" prop="deviceName"/>
      <el-table-column label="操作系统" align="center" prop="osName"/>
      <el-table-column label="浏览器" align="center" prop="browserName"/>
      <el-table-column label="登录时间" align="center" prop="startAccessTime"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleForceLogout(scope.row)" v-hasPerm="['monitor:online:forceLogout']">
            强退
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal" :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize" @pagination="getOnlineUserList"/>

  </div>
</template>

<script>
  import { getOnlineUserList, forceLogout } from '@/api/monitor/online'

  export default {
    name: 'Online',
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 表格数据
        onlineList: [],
        // 显示搜索条件
        showSearch: true,
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            ipAddr: undefined,
            userName: undefined
          }
        }
      }
    },
    created() {
      this.getOnlineUserList()
    },
    methods: {
      /** 查询登录日志列表 */
      getOnlineUserList() {
        this.loading = true
        getOnlineUserList(this.queryParam).then(response => {
          this.queryParam.pageTotal = response.data.pageTotal
          this.onlineList = response.data.recordList
          this.loading = false
        })
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.currentPage = 1
        this.getOnlineUserList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 强退按钮操作 */
      handleForceLogout(row) {
        this.$confirm('是否确认强退用户名称为"' + row.userName + '"的用户吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return forceLogout(row.userToken)
        }).then(() => {
          this.getOnlineUserList()
          this.messageSuccess('强退成功')
        })
      }
    }
  }
</script>

