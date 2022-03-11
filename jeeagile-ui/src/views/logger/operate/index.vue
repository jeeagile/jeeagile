<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParam" :inline="true" label-width="68px">
      <el-form-item label="操作模块" prop="operateModule">
        <el-input v-model="queryParam.queryCond.operateModule" placeholder="请输入操作模块" clearable style="width: 240px;"
                  size="small" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="操作人员" prop="operateUser">
        <el-input v-model="queryParam.queryCond.operateUser" placeholder="请输入操作人员" clearable style="width: 240px;"
                  size="small" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="操作类型" prop="operateType">
        <el-select v-model="queryParam.queryCond.operateType" placeholder="操作类型" clearable size="small"
                   style="width: 240px">
          <el-option v-for="dict in typeOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParam.queryCond.status" placeholder="操作状态" clearable size="small" style="width: 240px">
          <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel"
                     :value="dict.dictValue"/>
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
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete"
                   v-hasPerm="['logger:operate:delete']">
          删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleClear"
                   v-hasPerm="['logger:operate:clear']">
          清空
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getLoggerList"/>
    </el-row>

    <el-table v-loading="loading" :data="loggerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="操作模块" align="center" prop="operateModule"/>
      <el-table-column label="操作类型" align="center" prop="operateType" :formatter="typeFormat"/>
      <el-table-column label="操作人员" align="center" prop="operateUser"/>
      <el-table-column label="请求方式" align="center" prop="requestMethod"/>
      <el-table-column label="操作主机" align="center" prop="operateIp" width="130" :show-overflow-tooltip="true"/>
      <el-table-column label="操作地址" align="center" prop="operateAddress" :show-overflow-tooltip="true"/>
      <el-table-column label="操作状态" align="center" prop="status" :formatter="statusFormat"/>
      <el-table-column label="操作日期" align="center" prop="createTime" width="180"/>
      <el-table-column label="执行时间" align="center" prop="executeTime" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.executeTime < 30000" type="success" size="mini">{{scope.row.executeTime}}ms</el-tag>
          <el-tag v-else-if="scope.row.executeTime < 90000" type="warning" size="mini">{{scope.row.executeTime}}ms
          </el-tag>
          <el-tag v-else size="mini" type="danger">{{scope.row.executeTime}}ms</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)"
                     v-hasPerm="['logger:operate:detail']">
            详细
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getLoggerList"/>

    <!-- 操作日志详细 -->
    <el-dialog title="操作日志详细" :visible.sync="openDialog" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="24">
            <el-form-item label="操作模块：">{{ form.operateModule }} / {{ typeFormat(form) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="操作描述：">{{ form.operateNotes }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求方式：">{{ form.requestMethod }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行时间：">{{ form.executeTime }}ms</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="登录信息：">{{ form.operateUser }} / {{ form.operateIp }} / {{ form.operateAddress }}
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="服务地址：">{{ form.serverAddress }}{{ form.requestUri }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="操作方法：">{{ form.executeMethod }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="请求参数：">{{ form.requestParam }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="返回参数：">{{ form.responseParam }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作状态：">{{ statusFormat(form) }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作时间：">{{ form.createTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item v-if="form.status === 1" label="操作信息：">{{ form.message }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openDialog = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { selectLoggerPage, deleteLogger, clearLogger } from '@/api/logger/logger'

  export default {
    name: 'Operate',
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
        loggerList: [],
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
            operateModule: undefined,
            operateType: undefined,
            operateUser: undefined,
            status: undefined
          }
        }
      }
    },
    created() {
      this.getLoggerList()
      this.getDictDataByDictType('sys_logger_type').then(response => {
        this.typeOptions = response.data
      })
      this.getDictDataByDictType('sys_logger_status').then(response => {
        this.statusOptions = response.data
      })
    },
    methods: {
      /** 查询登录日志 */
      getLoggerList() {
        this.loading = true
        selectLoggerPage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.loggerList = response.data.recordList
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
        return this.handleDictLabel(this.typeOptions, row.operateType)
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.currentPage = 1
        this.getLoggerList()
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
          return deleteLogger(row.id)
        }).then(() => {
          this.getLoggerList()
          this.messageSuccess('删除成功')
        })
      },
      /** 清空按钮操作 */
      handleClear() {
        this.$confirm('是否确认清空所有操作日志数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return clearLogger()
        }).then(() => {
          this.getLoggerList()
          this.messageSuccess('清空成功')
        })
      }
    }
  }
</script>

