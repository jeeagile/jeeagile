<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParam" :inline="true" label-width="68px">
      <el-form-item label="任务编码" prop="jobGroup">
        <el-input v-model="queryParam.queryCond.jobCode" placeholder="请输入任务编码" clearable size="small" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="任务名称" prop="jobName">
        <el-input v-model="queryParam.queryCond.jobName" placeholder="请输入任务名称" clearable size="small" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="任务状态" prop="status">
        <el-select v-model="queryParam.queryCond.jobStatus" placeholder="请选择任务状态" clearable size="small">
          <el-option v-for="dict in jobStatusOption" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerm="['monitor:job:add']">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPerm="['monitor:job:update']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete" v-hasPerm="['monitor:job:delete']">
          删除
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getJobList"/>
    </el-row>

    <el-table v-loading="loading" :data="quartzJobList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="任务编码" align="center" prop="jobCode" :show-overflow-tooltip="true"/>
      <el-table-column label="任务名称" align="center" prop="jobName" :show-overflow-tooltip="true"/>
      <el-table-column label="Bean名称" align="center" prop="beanName" :show-overflow-tooltip="true"/>
      <el-table-column label="执行方法" align="center" prop="methodName" :show-overflow-tooltip="true"/>
      <el-table-column label="Cron执行表达式" align="center" prop="jobCron" :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.jobStatus" active-value="0" inactive-value="1" @change="handleStatusChange(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit"  @click="handleUpdate(scope.row)" v-hasPerm="['monitor:job:update']">
            编辑
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-caret-right" @click="handleExecute(scope.row)" v-hasPerm="['monitor:job:execute']">
            执行
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)" v-hasPerm="['monitor:job:detail']">
            详细
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal" :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize" @pagination="getJobList"/>


    <!-- 添加或修改定时任务对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务编码" prop="jobCode">
              <el-input v-model="form.jobCode" placeholder="请输入任务编码"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务名称" prop="jobName">
              <el-input v-model="form.jobName" placeholder="请输入任务名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Bean名称" prop="beanName">
              <el-input v-model="form.beanName" placeholder="请输入调用目标spring bean name"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行方法" prop="methodName">
              <el-input v-model="form.methodName" placeholder="请输入调用方法名"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行参数" prop="remark">
              <el-input v-model="form.methodParam" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Cron表达式" prop="jobCron">
              <el-input v-model="form.jobCron" placeholder="请输入cron执行表达式"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.jobStatus">
                <el-radio v-for="dict in jobStatusOption" :key="dict.dictValue" :label="dict.dictValue">
                  {{ dict.dictLabel }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 任务日志详细 -->
    <el-dialog title="任务详细" :visible.sync="openView" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="120px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务编码：">{{ form.jobCode }}</el-form-item>
            <el-form-item label="任务名称：">{{ form.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Bean名称：">{{ form.beanName }}</el-form-item>
            <el-form-item label="执行方法：">{{ form.methodName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Cron表达式：">{{ form.jobCron }}</el-form-item>
            <el-form-item label="任务状态：">{{ jobStatusFormat(form) }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { selectJobPage, detailJob, addJob, updateJob, deleteJob, executeJob, changeJobStatus } from '@/api/quartz/job'

  export default {
    name: 'Job',
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
        // 定时任务表格数据
        quartzJobList: [],
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        open: false,
        // 是否显示详细弹出层
        openView: false,
        // 状态字典
        jobStatusOption: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            jobCode: undefined,
            jobName: undefined,
            jobStatus: undefined
          }
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          jobCode: [
            { required: true, message: '任务编码不能为空', trigger: 'blur' }
          ],
          jobName: [
            { required: true, message: '任务名称不能为空', trigger: 'blur' }
          ],
          beanName: [
            { required: true, message: '调用目标名不能为空', trigger: 'blur' }
          ],
          methodName: [
            { required: true, message: '调用方法名不能为空', trigger: 'blur' }
          ],
          jobCron: [
            { required: true, message: 'cron执行表达式不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getJobList()
      this.getDictDataByDictType('sys_job_status').then(response => {
        this.jobStatusOption = response.data
      })
    },
    methods: {
      /** 查询定时任务列表 */
      getJobList() {
        this.loading = true
        selectJobPage(this.queryParam).then(response => {
          this.queryParam.pageTotal = response.data.pageTotal
          this.quartzJobList = response.data.recordList
          this.loading = false
        })
      },
      // 状态字典翻译
      jobStatusFormat(row) {
        return this.handleDictLabel(this.jobStatusOption, row.jobStatus)
      },
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          jobCode: undefined,
          jobName: undefined,
          jobGroup: undefined,
          jobCron: undefined,
          jobStatus: '0',
          beanName: undefined,
          methodName: undefined,
          methodParam: undefined,
          remark: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.currentPage = 1
        this.getJobList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      // 任务状态修改
      handleStatusChange(row) {
        let text = row.jobStatus === '0' ? '启用' : '停用'
        this.$confirm('确认要"' + text + '""' + row.jobName + '"任务吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const data = { id: row.id, status: row.jobStatus }
          return changeJobStatus(data)
        }).then(() => {
          this.messageSuccess(text + '成功')
        }).catch(() => {
          row.jobStatus = row.jobStatus === '0' ? '1' : '0'
        })
      },
      /** 执行 */
      handleExecute(row) {
        this.$confirm('确认要执行"' + row.jobName + '"任务吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return executeJob(row.id)
        }).then(() => {
          this.messageSuccess('执行成功')
        })
      },
      /** 任务详细信息 */
      handleView(row) {
        detailJob(row.id).then(response => {
          this.form = response.data
          this.openView = true
        })
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.open = true
        this.dialogTitle = '添加任务'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailJob(row.id).then(response => {
          this.form = response.data
          this.open = true
          this.dialogTitle = '修改任务'
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateJob(this.form).then(response => {
                this.messageSuccess('修改成功')
                this.open = false
                this.getJobList()
              })
            } else {
              addJob(this.form).then(response => {
                this.messageSuccess('新增成功')
                this.open = false
                this.getJobList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除定时任务编号为"' + row.jobName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteJob(row.id)
        }).then(() => {
          this.getJobList()
          this.messageSuccess('删除成功')
        })
      }
    }
  }
</script>
