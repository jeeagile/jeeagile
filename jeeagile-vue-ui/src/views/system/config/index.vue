<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="参数名称" prop="configName">
        <el-input v-model="queryParam.queryCond.configName" placeholder="请输入参数名称" clearable size="small"
                  style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="参数键名" prop="configKey">
        <el-input v-model="queryParam.queryCond.configKey" placeholder="请输入参数键名" clearable size="small"
                  style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="系统内置" prop="systemFlag">
        <el-select v-model="queryParam.queryCond.systemFlag" placeholder="系统内置" clearable size="small">
          <el-option v-for="item in SysYesNo.getList()" :key="item.value" :label="item.label" :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerm="['system:config:add']">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPerm="['system:config:update']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete"
                   v-hasPerm="['system:config:delete']">
          删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport"
                   v-hasPerm="['system:config:export']">
          导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-refresh" size="mini" @click="handleClearCache"
                   v-hasPerm="['system:config:clear']">
          清理缓存
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getConfigList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="参数名称" align="center" prop="configName" :show-overflow-tooltip="true"/>
      <el-table-column label="参数键名" align="center" prop="configKey" :show-overflow-tooltip="true"/>
      <el-table-column label="参数键值" align="center" prop="configValue"/>
      <el-table-column label="系统内置" align="center" prop="systemFlag">
        <template slot-scope="scope">
            {{SysYesNo.getLabel(scope.row.systemFlag)}}
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPerm="['system:config:update']">
            修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPerm="['system:config:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getConfigList"/>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="参数名称" prop="configName">
          <el-input v-model="form.configName" placeholder="请输入参数名称"/>
        </el-form-item>
        <el-form-item label="参数键名" prop="configKey">
          <el-input v-model="form.configKey" placeholder="请输入参数键名"/>
        </el-form-item>
        <el-form-item label="参数键值" prop="configValue">
          <el-input v-model="form.configValue" placeholder="请输入参数键值"/>
        </el-form-item>
        <el-form-item label="系统内置" prop="systemFlag">
          <el-radio-group v-model="form.systemFlag">
            <el-radio v-for="item in SysYesNo.getList()" :key="item.value" :label="item.value">
              {{ item.label }}
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
  import {
    selectConfigPage,
    detailConfig,
    deleteConfig,
    addConfig,
    updateConfig,
    clearConfigCache,
    exportConfig
  } from '@/api/system/config'

  export default {
    name: 'Config',
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
        // 参数表格数据
        configList: [],
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        openDialog: false,
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            configName: undefined,
            configKey: undefined,
            systemFlag: undefined,
            excelName: '参数配置'
          }
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          configName: [
            { required: true, message: '参数名称不能为空', trigger: 'blur' }
          ],
          configKey: [
            { required: true, message: '参数键名不能为空', trigger: 'blur' }
          ],
          configValue: [
            { required: true, message: '参数键值不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getConfigList()
    },
    methods: {
      /** 查询参数列表 */
      getConfigList() {
        this.loading = true
        selectConfigPage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.configList = response.data.records
            this.loading = false
          }
        )
      },
      // 取消按钮
      cancel() {
        this.openDialog = false
        this.reset()
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          configName: undefined,
          configKey: undefined,
          configValue: undefined,
          systemFlag: '0',
          remark: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getConfigList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.openDialog = true
        this.dialogTitle = '添加参数'
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailConfig(row.id).then(response => {
          this.form = response.data
          this.openDialog = true
          this.dialogTitle = '修改参数'
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateConfig(this.form).then(() => {
                this.messageSuccess('修改成功')
                this.openDialog = false
                this.getConfigList()
              })
            } else {
              addConfig(this.form).then(() => {
                this.messageSuccess('新增成功')
                this.openDialog = false
                this.getConfigList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除参数名称为"' + row.configName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteConfig(row.id)
        }).then(() => {
          this.getConfigList()
          this.messageSuccess('删除成功')
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        this.$confirm('请确认是否导出字段类型数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return exportConfig(this.queryParam.queryCond)
        })
      },
      /** 清理缓存按钮操作 */
      handleClearCache() {
        clearConfigCache().then(() => {
          this.messageSuccess('清理成功')
        })
      }
    }
  }
</script>
