<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParam" :inline="true" label-width="68px">
      <el-form-item label="登录用户" prop="loginName">
        <el-input v-model="queryParam.queryCond.loginName" placeholder="请输入登录用户名" clearable style="width: 240px;" size="small" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParam.queryCond.status" placeholder="登录状态" clearable size="small" style="width: 240px">
          <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item label="操作时间">
        <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"/>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete" v-hasPerm="['logger:login:delete']">
          删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleClean" v-hasPerm="['logger:login:clear']">
          清空
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getLoginList"/>
    </el-row>

    <el-table v-loading="loading" :data="loginList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="登录用户名" align="center" prop="loginName"/>
      <el-table-column label="主机" align="center" prop="loginIp" width="130" :show-overflow-tooltip="true"/>
      <el-table-column label="操作地点" align="center" prop="loginAddress" :show-overflow-tooltip="true"/>
      <el-table-column label="设备名称" align="center" prop="loginDevice" :show-overflow-tooltip="true"/>
      <el-table-column label="操作系统" align="center" prop="loginOs" :show-overflow-tooltip="true"/>
      <el-table-column label="浏览器" align="center" prop="loginBrowser" :show-overflow-tooltip="true"/>
      <el-table-column label="登录状态" align="center" prop="status" :formatter="statusFormat"/>
      <el-table-column label="登录信息" align="center" prop="message" :show-overflow-tooltip="true"/>
      <el-table-column label="登录日期" align="center" prop="loginTime" width="180"/>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal" :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize" @pagination="getLoginList"/>
  </div>
</template>

<script>
  import { selectLoginPage, deleteLogin, cleanLogin } from '@/api/logger/login'

  export default {
    name: 'Login',
    data() {
      return {
        // 遮罩层
        loading: true,
        // 已选择的列表
        selectRowList: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 表格数据
        loginList: [],
        // 是否显示弹出层
        openDialog: false,
        // 类型数据字典
        typeOptions: [],
        // 类型数据字典
        statusOptions: [],
        // 日期范围
        dateRange: [],
        // 表单参数
        form: {},
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            title: undefined,
            type: undefined,
            status: undefined,
            loginName: undefined
          }
        }
      }
    },
    created() {
      this.getLoginList()
      this.getDictDataByDictType('sys_common_status').then(response => {
        this.statusOptions = response.data
      })
    },
    methods: {
      /** 查询登录日志 */
      getLoginList() {
        this.loading = true
        selectLoginPage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.loginList = response.data.recordList
            this.loading = false
          }
        )
      },
      /** 操作日志状态字典翻译 */
      statusFormat(row) {
        return this.handleDictLabel(this.statusOptions, row.status)
      },
      /** 操作日志类型字典翻译 */
      typeFormat(row) {
        return this.handleDictLabel(this.typeOptions, row.type)
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.currentPage = 1
        this.getLoginList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = []
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 多选框选中数据 */
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 详细按钮操作 */
      handleView(row) {
        this.openDialog = true
        this.form = row
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除日志编号为"' + row.id + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteLogin(row.id)
        }).then(() => {
          this.getLoginList()
          this.messageSuccess('删除成功')
        })
      },
      /** 清空按钮操作 */
      handleClean() {
        this.$confirm('是否确认清空所有操作日志数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return cleanLogin()
        }).then(() => {
          this.getLoginList()
          this.messageSuccess('清空成功')
        })
      }
    }
  }
</script>

