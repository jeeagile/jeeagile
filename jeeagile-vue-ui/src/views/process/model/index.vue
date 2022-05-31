<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程编码" prop="processCode">
        <el-input v-model="queryParam.queryCond.processCode" placeholder="请输入流程编码" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="流程名称" prop="processName">
        <el-input v-model="queryParam.queryCond.processName" placeholder="请输入流程名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="部署状态" prop="processDeploymentStatus">
        <el-select v-model="queryParam.queryCond.processDeploymentStatus" placeholder="流程状态" clearable size="small">
          <el-option v-for="processDeploymentStatusOption in processDeploymentStatusOptionList"
                     :key="processDeploymentStatusOption.dictValue"
                     :label="processDeploymentStatusOption.dictLabel" :value="processDeploymentStatusOption.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerm="['process:model:add']">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPerm="['process:model:update']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete"
                   v-hasPerm="['process:model:delete']">
          删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getProcessModelList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="modelList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="流程编码" align="center" prop="processCode"/>
      <el-table-column label="流程名称" align="center" prop="processName"/>
      <el-table-column label="流程版本" align="center" prop="processVersion">
        <template slot-scope="scope">
          <span>v{{scope.row.processVersion}}</span>
        </template>
      </el-table-column>
      <el-table-column label="部署状态" align="center" prop="processDeploymentStatus"
                       :formatter="processDeploymentStatusFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPerm="['process:model:update']">
            修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPerm="['process:model:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getProcessModelList"/>

    <!-- 添加或修改流程对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="流程名称" prop="processName">
          <el-input v-model="form.processName" placeholder="请输入流程名称"/>
        </el-form-item>
        <el-form-item label="流程编码" prop="processCode">
          <el-input v-model="form.processCode" placeholder="请输入编码名称"/>
        </el-form-item>
        <el-form-item label="表单类型" prop="processFormType">
          <el-radio-group v-model="form.processFormType">
            <el-radio v-for="processFormTypeOption in processFormTypeOptionList" :key="processFormTypeOption.dictValue"
                      :label="processFormTypeOption.dictValue">
              {{ processFormTypeOption.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="流程表单" prop="processFormId" v-if="form.processFormType=='1'">
          <el-select v-model="form.processFormId" placeholder="请选择" style="width: 100%">
            <el-option
              v-for="processFormOption in processFormList"
              :key="processFormOption.id"
              :label="processFormOption.formName"
              :value="processFormOption.id"
              :disabled="processFormOption.formStatus==1"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="表单地址" prop="processFormUrl" v-if="form.processFormType=='2'">
          <el-input v-model="form.processFormUrl" placeholder="请输入表单地址"/>
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
    selectProcessModelPage, detailProcessModel, deleteProcessModel, addProcessModel, updateProcessModel
  } from '@/api/process/model'
  import { selectProcessFormList } from '@/api/process/form'

  export default {
    name: 'Model',
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
        // 流程表格数据
        modelList: [],
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        openDialog: false,
        // 流程部署数据字典
        processDeploymentStatusOptionList: [],
        // 流程表单类型
        processFormTypeOptionList: [],
        // 流程表单列表
        processFormList: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            processCode: undefined,
            processName: undefined,
            processDeploymentStatus: undefined
          }
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          processName: [
            { required: true, message: '流程名称不能为空', trigger: 'blur' }
          ],
          processCode: [
            { required: true, message: '流程编码不能为空', trigger: 'blur' }
          ],
          processFormType: [
            { required: true, message: '流程表单类型不能为空', trigger: 'blur' }
          ],
          processFormId: [
            { required: true, message: '流程表单不能为空', trigger: 'blur' }
          ],
          processFormUrl: [
            { required: true, message: '表单地址不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getProcessFormList()
      this.getProcessModelList()
      this.getSysDictDataList('process_deployment_status').then(response => {
        this.processDeploymentStatusOptionList = response.data
      })
      this.getSysDictDataList('process_form_type').then(response => {
        this.processFormTypeOptionList = response.data
      })
    },
    methods: {
      /** 查询流程表单列表 */
      getProcessFormList() {
        selectProcessFormList().then(response => {
            this.processFormList = response.data
          }
        )
      },
      /** 查询流程列表 */
      getProcessModelList() {
        this.loading = true
        selectProcessModelPage(this.queryParam).then(response => {
          this.queryParam.pageTotal = response.data.pageTotal
          this.modelList = response.data.recordList
          this.loading = false
        })
      },
      /** 流程状态字典翻译 */
      processDeploymentStatusFormat(row, column) {
        return this.handleDictLabel(this.processDeploymentStatusOptionList, row.processDeploymentStatus)
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
          processCode: undefined,
          processName: undefined,
          processFormType: '1',
          remark: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getProcessModelList()
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
        this.dialogTitle = '添加流程'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailProcessModel(row.id).then(response => {
          this.form = response.data
          this.openDialog = true
          this.dialogTitle = '修改流程'
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateProcessModel(this.form).then(() => {
                this.messageSuccess('修改成功')
                this.openDialog = false
                this.getProcessModelList()
              })
            } else {
              addProcessModel(this.form).then(() => {
                this.messageSuccess('新增成功')
                this.openDialog = false
                this.getProcessModelList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除流程编号为"' + row.processName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteProcessModel(row.id)
        }).then(() => {
          this.getProcessModelList()
          this.messageSuccess('删除成功')
        })
      }
    }
  }
</script>
