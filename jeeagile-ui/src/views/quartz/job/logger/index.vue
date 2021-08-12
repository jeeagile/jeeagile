<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParam" :inline="true" label-width="68px">
      <el-form-item label="任务编码" prop="jobCode">
        <el-input v-model="queryParam.queryCond.jobCode" placeholder="请输入任务编码" clearable style="width: 240px;"
                  size="small" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="任务名称" prop="jobName">
        <el-input v-model="queryParam.queryCond.jobName" placeholder="请输入任务名称" clearable style="width: 240px;"
                  size="small" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="任务分组" prop="jobGroup">
        <el-input v-model="queryParam.queryCond.jobGroup" placeholder="请输入任务分组" clearable style="width: 240px;"
                  size="small" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="执行状态" prop="status">
        <el-select v-model="queryParam.queryCond.status" placeholder="请选择执行状态" clearable size="small"
                   style="width: 240px">
          <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel"
                     :value="dict.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item label="执行时间">
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
                   v-hasPerm="['job:logger:delete']">
          删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleClear"
                   v-hasPerm="['job:logger:clear']">
          清空
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getLoggerList"/>
    </el-row>

    <el-table v-loading="loading" :data="loggerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="任务编码" align="center" prop="jobCode"/>
      <el-table-column label="任务名称" align="center" prop="jobName" :show-overflow-tooltip="true"/>
      <el-table-column label="任务分组" align="center" prop="jobGroup" :show-overflow-tooltip="true"/>
      <el-table-column label="Bean名称" align="center" prop="beanName" :show-overflow-tooltip="true"/>
      <el-table-column label="执行方法" align="center" prop="methodName" :show-overflow-tooltip="true"/>
      <el-table-column label="执行时间" align="center" prop="startTime" width="150px"/>
      <el-table-column label="执行状态" align="center" prop="status" :formatter="statusFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)"
                     v-hasPerm="['job:logger:detail']">
            详细
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getLoggerList"/>

    <!-- 任务执行日志详细 -->
    <el-dialog title="任务执行日志详细" :visible.sync="openDialog" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="120px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务编码：">{{ form.jobCode }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务名称：">{{ form.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组：">{{ form.jobGroup }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Cron表达式：">{{ form.jobCron }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Bean名称：">{{ form.beanName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行方法：">{{ form.methodName }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行参数：">{{ form.methodParam }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始时间：">{{ form.startTime }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间：">{{ form.stopTime }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行状态：">{{ statusFormat(form) }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行时间：">{{ form.executeTime }}毫秒</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item v-if="form.status === 1" label="异常信息：">{{ form.message }}</el-form-item>
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
  import { selectLoggerPage, deleteLogger, clearLogger } from '@/api/quartz/logger'

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
            jobCode: undefined,
            jobName: undefined,
            jobGroup: undefined,
            status: undefined
          }
        }
      }
    },
    created() {
      const jobCode = this.$route.params && this.$route.params.jobCode
      this.queryParam.queryCond.jobCode = jobCode
      this.getLoggerList()
      this.getDictDataByDictType('sys_common_status').then(response => {
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
      /** 执行状态字典翻译 */
      statusFormat(row) {
        return this.handleDictLabel(this.statusOptions, row.status)
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

