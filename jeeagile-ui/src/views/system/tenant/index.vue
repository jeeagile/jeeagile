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
      <el-form-item label="状态" prop="tenantStatus">
        <el-select v-model="queryParam.queryCond.tenantStatus" placeholder="租户状态" clearable size="small">
          <el-option v-for="tenantStatusOption in tenantStatusOptionList" :key="tenantStatusOption.dictValue"
                     :label="tenantStatusOption.dictLabel" :value="tenantStatusOption.dictValue"/>
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
      <el-table-column label="租户状态" align="center" prop="tenantStatus" :formatter="tenantStatusFormat"/>
      <el-table-column label="启用日期" align="center" width="200px">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.enableStatus =='0'" type="success">
            {{scope.row.startDate}}至{{scope.row.endDate}}
          </el-tag>
          <el-tag v-else-if="scope.row.enableStatus =='1'" type="warning">
            {{scope.row.startDate}}至{{scope.row.endDate}}
          </el-tag>
          <el-tag v-else-if="scope.row.enableStatus =='2'" type="danger">
            {{scope.row.startDate}}至{{scope.row.endDate}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150px">
        <template slot-scope="scope">
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
        <el-form-item label="租户名称" prop="tenantName">
          <el-input v-model="form.tenantName" placeholder="请输入租户名称"/>
        </el-form-item>
        <el-form-item label="租户编码" prop="tenantCode">
          <el-input v-model="form.tenantCode" placeholder="请输入编码名称"/>
        </el-form-item>
        <el-form-item label="启用日期" prop="tenantDateRange">
          <el-date-picker v-model="form.tenantDateRange" size="small" value-format="yyyy-MM-dd"
                          type="daterange" unlink-panels style="width: 100%"
                          range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"/>
        </el-form-item>
        <el-form-item label="租户状态" prop="tenantStatus">
          <el-radio-group v-model="form.tenantStatus">
            <el-radio v-for="tenantStatusOption in tenantStatusOptionList" :key="tenantStatusOption.dictValue"
                      :label="tenantStatusOption.dictValue">
              {{ tenantStatusOption.dictLabel }}
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
  </div>
</template>

<script>
  import { selectTenantPage, detailTenant, deleteTenant, addTenant, updateTenant } from '@/api/system/tenant'

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
        // 状态数据字典
        tenantStatusOptionList: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            tenantCode: undefined,
            tenantName: undefined,
            tenantStatus: undefined
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
          ],
          tenantDateRange: [
            { type: 'array', required: true, message: '租户启用时间必须设置', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getTenantList()
      this.getDictDataByDictType('sys_normal_disable').then(response => {
        this.tenantStatusOptionList = response.data
      })
    },
    methods: {
      /** 查询租户列表 */
      getTenantList() {
        this.loading = true
        selectTenantPage(this.queryParam).then(response => {
          this.queryParam.pageTotal = response.data.pageTotal
          this.tenantList = response.data.recordList
          this.loading = false
        })
      },
      /** 租户状态字典翻译 */
      tenantStatusFormat(row, column) {
        return this.handleDictLabel(this.tenantStatusOptionList, row.tenantStatus)
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
          tenantStatus: '0',
          tenantDateRange: [],
          startDate: undefined,
          endDate: undefined,
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
          this.$set(this.form, 'tenantDateRange', [response.data.startDate, response.data.endDate])
          this.openDialog = true
          this.dialogTitle = '修改租户'
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            this.form.startDate = this.form.tenantDateRange[0]
            this.form.endDate = this.form.tenantDateRange[1]
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
