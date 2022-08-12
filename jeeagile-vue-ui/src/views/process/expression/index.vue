<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="表达式编码" prop="expressionCode">
        <el-input v-model="queryParam.queryCond.expressionCode" placeholder="请输入表达式编码" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="表达式名称" prop="expressionName">
        <el-input v-model="queryParam.queryCond.expressionName" placeholder="请输入表达式名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="expressionStatus">
        <el-select v-model="queryParam.queryCond.expressionStatus" placeholder="表达式状态" clearable size="small">
          <el-option v-for="expressionStatusOption in expressionStatusOptionList"
                     :key="expressionStatusOption.dictValue"
                     :label="expressionStatusOption.dictLabel" :value="expressionStatusOption.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd"
                   v-hasPerm="['system:expression:add']">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPerm="['system:expression:update']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete"
                   v-hasPerm="['system:expression:delete']">
          删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport"
                   v-hasPerm="['system:expression:export']">
          导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getExpressionList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="expressionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="表达式编码" align="center" prop="expressionCode"/>
      <el-table-column label="表达式名称" align="center" prop="expressionName"/>
      <el-table-column label="状态" align="center" prop="expressionStatus" :formatter="expressionStatusFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPerm="['system:expression:update']">
            修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPerm="['system:expression:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getExpressionList"/>

    <!-- 添加或修改表达式对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="表达式编码" prop="expressionCode">
          <el-input v-model="form.expressionCode" placeholder="请输入编码名称"/>
        </el-form-item>
        <el-form-item label="表达式名称" prop="expressionName">
          <el-input v-model="form.expressionName" placeholder="请输入表达式名称"/>
        </el-form-item>
        <el-form-item label="表达式" prop="expressionName">
          <el-input v-model="form.expressionValue" placeholder="请输入表达式"/>
        </el-form-item>
        <el-form-item label="表达式状态" prop="expressionStatus">
          <el-radio-group v-model="form.expressionStatus">
            <el-radio v-for="expressionStatusOption in expressionStatusOptionList"
                      :key="expressionStatusOption.dictValue"
                      :label="expressionStatusOption.dictValue">
              {{ expressionStatusOption.dictLabel }}
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
    selectProcessExpressionPage,
    detailProcessExpression,
    deleteProcessExpression,
    addProcessExpression,
    updateProcessExpression,
    exportProcessExpression
  } from '@/api/process/expression'

  export default {
    name: 'Expression',
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
        // 表达式表格数据
        expressionList: [],
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        openDialog: false,
        // 状态数据字典
        expressionStatusOptionList: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            expressionCode: undefined,
            expressionName: undefined,
            expressionStatus: undefined,
            excelName: '表达式数据'
          }
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          expressionName: [
            { required: true, message: '表达式名称不能为空', trigger: 'blur' }
          ],
          expressionCode: [
            { required: true, message: '表达式编码不能为空', trigger: 'blur' }
          ],
          expressionValue: [
            { required: true, message: '表达式不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getExpressionList()
      this.getSysDictDataList('sys_normal_disable').then(response => {
        this.expressionStatusOptionList = response.data
      })
    },
    methods: {
      /** 查询表达式列表 */
      getExpressionList() {
        this.loading = true
        selectProcessExpressionPage(this.queryParam).then(response => {
          this.queryParam.pageTotal = response.data.pageTotal
          this.expressionList = response.data.records
          this.loading = false
        })
      },
      /** 表达式状态字典翻译 */
      expressionStatusFormat(row, column) {
        return this.handleDictLabel(this.expressionStatusOptionList, row.expressionStatus)
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
          expressionCode: undefined,
          expressionName: undefined,
          expressionValue: undefined,
          expressionStatus: '0',
          remark: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getExpressionList()
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
        this.dialogTitle = '添加表达式'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailProcessExpression(row.id).then(response => {
          this.form = response.data
          this.openDialog = true
          this.dialogTitle = '修改表达式'
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateProcessExpression(this.form).then(() => {
                this.messageSuccess('修改成功')
                this.openDialog = false
                this.getExpressionList()
              })
            } else {
              addProcessExpression(this.form).then(() => {
                this.messageSuccess('新增成功')
                this.openDialog = false
                this.getExpressionList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除表达式编号为"' + row.expressionName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteProcessExpression(row.id)
        }).then(() => {
          this.getExpressionList()
          this.messageSuccess('删除成功')
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        this.$confirm('请确认是否导出表达式数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return exportProcessExpression(this.queryParam.queryCond)
        })
      }
    }
  }
</script>
