<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="租户编码" prop="tenantCode">
        <el-input v-model="queryParam.queryCond.tenantCode" placeholder="请输入租户编码" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="租户名称" prop="tenantName">
        <el-input v-model="queryParam.queryCond.tenantName" placeholder="请输入租户名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="启用状态" prop="enableStatus">
        <el-select v-model="queryParam.queryCond.enableStatus" placeholder="租户状态" clearable size="small">
          <el-option v-for="enableStatusOption in enableStatusOptionList" :key="enableStatusOption.dictValue"
                     :label="enableStatusOption.dictLabel" :value="enableStatusOption.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态" prop="auditStatus">
        <el-select v-model="queryParam.queryCond.auditStatus" placeholder="租户状态" clearable size="small">
          <el-option v-for="auditStatusOption in auditStatusOptionList" :key="auditStatusOption.dictValue"
                     :label="auditStatusOption.dictLabel" :value="auditStatusOption.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerm="['system:tenant:add']">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPerm="['system:tenant:update']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete"
                   v-hasPerm="['system:tenant:delete']">
          删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getTenantList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tenantList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="租户编码" align="center" prop="tenantCode"/>
      <el-table-column label="租户名称" align="center" prop="tenantName"/>
      <el-table-column label="启用状态" align="center" prop="enableStatus" :formatter="enableStatusFormat"/>
      <el-table-column label="审核状态" align="center" prop="auditStatus" :formatter="auditStatusFormat"/>
      <el-table-column label="有效期" align="center" prop="expirationDate" :formatter="expirationDateFormat"/>
      <el-table-column label="操作" align="center" width="250px">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-tickets"
                     @click="handleView(scope.row)"
                     v-hasPerm="['system:tenant:audit']">
            详细
          </el-button>
          <el-button v-if="scope.row.auditStatus==0" size="mini" type="text" icon="el-icon-tickets"
                     @click="handleAudit(scope.row)"
                     v-hasPerm="['system:tenant:audit']">
            审核
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPerm="['system:tenant:update']">
            修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPerm="['system:tenant:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getTenantList"/>

    <!-- 添加或修改租户对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="租户编码" prop="tenantCode">
          <el-input v-model="form.tenantCode" placeholder="请输入租户编码" :disabled="form.id != undefined"/>
        </el-form-item>
        <el-form-item label="租户名称" prop="tenantName">
          <el-input v-model="form.tenantName" placeholder="请输入租户名称"/>
        </el-form-item>
        <el-form-item label="有效日期" prop="expirationDate">
          <el-date-picker
            v-model="form.expirationDate"
            type="date"
            placeholder="选择日期"
            :picker-options="datePickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="启用状态" prop="enableStatus">
          <el-radio-group v-model="form.enableStatus">
            <el-radio v-for="enableStatusOption in enableStatusOptionList" :key="enableStatusOption.dictValue"
                      :label="enableStatusOption.dictValue">
              {{ enableStatusOption.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改租户对话框 -->
    <el-dialog title="租户审核" :visible.sync="auditDialog" width="500px" append-to-body>
      <el-form ref="form" :model="auditForm" :rules="auditRules" label-width="80px">
        <el-form-item label="租户编码" prop="tenantCode">
          <el-input v-model="auditForm.tenantCode" placeholder="请输入租户编码" disabled="true"/>
        </el-form-item>
        <el-form-item label="租户名称" prop="tenantName">
          <el-input v-model="auditForm.tenantName" placeholder="请输入租户名称" disabled="true"/>
        </el-form-item>
        <el-form-item label="租户类型" prop="tenantType">
          <el-select v-model="auditForm.tenantType" style="width: 100%">
            <el-option label="本地" value="0"/>
            <el-option label="远程" value="1"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleAuditPass">审核通过</el-button>
        <el-button type="info" @click="handleAuditReject">审核拒绝</el-button>
      </div>
    </el-dialog>

    <el-dialog title="租户详细信息" :visible.sync="viewDialog" width="500px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="租户编码：" prop="tenantCode">
              {{ form.tenantCode }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租户名称：">
              {{ form.tenantName }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="租户类型：">
              {{ tenantTypeFormat(form.tenantType) }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="有效日期：">
              {{form.expirationDate}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="启用状态：">
              {{ enableStatusFormat(form) }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核状态：">
              {{ auditStatusFormat(form) }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item v-if="form.auditStatus==1" label="访问地址：">
              {{ tenantLoginUrl(form) }}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewDialog = false">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {
    selectTenantPage,
    detailTenant,
    deleteTenant,
    addTenant,
    updateTenant,
    auditTenant
  } from '@/api/system/tenant'

  export default {
    name: 'Tenant',
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
        // 总条数
        total: 0,
        // 租户表格数据
        tenantList: [],
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        openDialog: false,
        auditDialog: false,
        viewDialog: false,
        // 状态数据字典
        enableStatusOptionList: [],
        // 审核状态字典
        auditStatusOptionList: [],
        datePickerOptions: {
          disabledDate(time) {
            return time.getTime() <= Date.now()
          }
        },
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            tenantCode: undefined,
            tenantName: undefined,
            enableStatus: undefined,
            auditStatus: undefined
          }
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          tenantName: [
            { required: true, message: '租户名称不能为空', trigger: 'blur' }
          ],
          tenantCode: [
            { required: true, message: '租户编码不能为空', trigger: 'blur' }
          ]
        },
        // 表单参数
        auditForm: {
          id: undefined,
          tenantCode: undefined,
          tenantName: undefined,
          tenantType: '0',
          auditStatus: undefined
        },
        auditRules: {
          tenantType: [
            { required: true, message: '租户类型不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getTenantList()
      this.getSysDictDataList('sys_enable_status').then(response => {
        this.enableStatusOptionList = response.data
      })
      this.getSysDictDataList('sys_audit_status').then(response => {
        this.auditStatusOptionList = response.data
      })
    },
    methods: {
      /** 查询租户列表 */
      getTenantList() {
        this.loading = true
        selectTenantPage(this.queryParam).then(response => {
          this.queryParam.pageTotal = response.data.pageTotal
          this.tenantList = response.data.records
          this.loading = false
        })
      },
      /** 启用状态字典翻译 */
      enableStatusFormat(row, column) {
        return this.handleDictLabel(this.enableStatusOptionList, row.enableStatus)
      },
      /** 审核状态字典翻译 */
      auditStatusFormat(row, column) {
        return this.handleDictLabel(this.auditStatusOptionList, row.auditStatus)
      },
      /** 租户类型翻译 */
      tenantTypeFormat(tenantType) {
        if (tenantType === '0') {
          return '本地'
        }
        if (tenantType === '1') {
          return '远程'
        }
      },
      /** 租户登录地址 */
      tenantLoginUrl(form) {
        return location.protocol + '//' + location.host + '/login?tenantId=' + form.id + '&tenantSign=' + form.tenantSign
      },
      /** 有效期格式化 */
      expirationDateFormat(row, column) {
        if (!row.expirationDate) {
          return '永久有效'
        } else {
          const expirationDate = Date.parse(row.expirationDate)
          const currentDate = Date.now()
          const millisecond = expirationDate - currentDate
          if (millisecond < 0) {
            return '已过期'
          } else {
            const mi = 60 * 1000
            const hh = 60 * 60 * 1000
            const dd = 24 * 60 * 60 * 1000
            const day = Math.round(millisecond / dd)
            const hour = Math.round((millisecond - day * dd) / hh)
            const minute = Math.round((millisecond - day * dd - hour * hh) / mi)
            if (day > 0) {
              return day + '天后过期'
            }
            if (hour > 0) {
              return hour + '小时后过期'
            }
            if (minute > 0) {
              return minute + '分钟后过期'
            }
            return '即将过期'
          }
        }
      },

      /** 取消按钮 */
      cancel() {
        this.openDialog = false
        this.reset()
      },
      /** 表单重置 */
      reset() {
        this.form = {
          id: undefined,
          tenantCode: undefined,
          tenantName: undefined,
          enableStatus: '0',
          expirationDate: undefined,
          remark: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getTenantList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 多选框选中数据 */
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.openDialog = true
        this.dialogTitle = '添加租户'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailTenant(row.id).then(response => {
          this.form = response.data
          this.openDialog = true
          this.dialogTitle = '修改租户'
        })
      },
      handleView(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailTenant(row.id).then(response => {
          this.form = response.data
          this.viewDialog = true
        })
      },
      handleAudit(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        detailTenant(row.id).then(response => {
          this.auditForm = response.data
          this.auditDialog = true
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateTenant(this.form).then(() => {
                this.messageSuccess('修改成功')
                this.openDialog = false
                this.getTenantList()
              })
            } else {
              addTenant(this.form).then(() => {
                this.messageSuccess('新增成功')
                this.openDialog = false
                this.getTenantList()
              })
            }
          }
        })
      },
      handleAuditPass() {
        this.auditForm.auditStatus = 1
        auditTenant(this.auditForm).then(() => {
          this.messageSuccess('租户审批成功！')
          this.auditDialog = false
          this.getTenantList()
        })
      },
      handleAuditReject() {
        this.auditForm.auditStatus = 2
        auditTenant(this.auditForm).then(() => {
          this.messageSuccess('租户审批已拒绝！')
          this.auditDialog = false
          this.getTenantList()
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除租户名称为"' + row.tenantName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteTenant(row.id)
        }).then(() => {
          this.getTenantList()
          this.messageSuccess('删除成功')
        })
      }
    }
  }
</script>
