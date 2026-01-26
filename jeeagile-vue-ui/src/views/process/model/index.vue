<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模型编码" prop="modelCode">
        <el-input v-model="queryParam.queryCond.modelCode" placeholder="请输入模型编码" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="模型名称" prop="modelName">
        <el-input v-model="queryParam.queryCond.modelName" placeholder="请输入模型名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="发布状态" prop="deploymentStatus">
        <el-select v-model="queryParam.queryCond.deploymentStatus" placeholder="发布状态" clearable size="small">
          <el-option v-for="item in ProcessDeploymentStatus.getList()" :key="item.value" :label="item.label"
                     :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleProcessAdd"
                   v-hasPerm="['process:model:add']">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleProcessUpdate"
                   v-hasPerm="['process:model:update']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleProcessDelete"
                   v-hasPerm="['process:model:delete']">
          删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getProcessModelList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="modelList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="模型编码" align="center" prop="modelCode"/>
      <el-table-column label="模型名称" align="center" prop="modelName" :show-overflow-tooltip="true"/>
      <el-table-column label="流程版本" align="center" prop="modelVersion">
        <template slot-scope="scope">
          <span>v{{scope.row.modelVersion}}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布状态" align="center" prop="deploymentStatus">
        <template slot-scope="scope">
          {{ProcessDeploymentStatus.getLabel(scope.row.deploymentStatus)}}
        </template>
      </el-table-column>
      <el-table-column label="发布时间" align="center" prop="deploymentTime" width="150px"/>
      <el-table-column label="操作" width="450px" align="center" class-name="small-padding">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-s-custom" @click="handleProcessView(scope.row)">
            流程预览
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" @click="handleProcessDesigner(scope.row)"
                     v-hasPerm="['process:model:designer']">
            流程设计
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-thumb" @click="handleProcessDeployment(scope.row)"
                     v-hasPerm="['process:model:deployment']">
            流程发布
          </el-button>

          <el-dropdown size="mini" @command="(command) => handleProcessCommand(command, scope.row)">
            <span class="el-dropdown-link">
              <i class="el-icon-d-arrow-right el-icon--right"></i>更多操作
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="handleProcessUpdate" icon="el-icon-edit"
                                v-hasPerm="['process:model:edit']">
                流程编辑
              </el-dropdown-item>
              <el-dropdown-item command="handleProcessDefinition" icon="el-icon-ice-cream-round"
                                v-hasPerm="['process:model:definition']">
                流程定义
              </el-dropdown-item>
              <el-dropdown-item command="handleProcessDelete" icon="el-icon-delete"
                                v-hasPerm="['process:model:remove']">
                流程删除
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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
        <el-form-item label="模型编码" prop="modelCode">
          <el-input v-model="form.modelCode" placeholder="请输入模型编码"/>
        </el-form-item>
        <el-form-item label="模型名称" prop="modelName">
          <el-input v-model="form.modelName" placeholder="请输入模型名称"/>
        </el-form-item>
        <el-form-item label="表单类型" prop="formType">
          <el-radio-group v-model="form.formType" @change="formTypeChange">
            <el-radio v-for="item in ProcessFormType.getList()" :key="item.value" :label="item.value">
              {{ item.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>


        <el-form-item label="流程表单" prop="formId" v-if="form.formType === ProcessFormType.PROCESS_FORM">
          <el-select v-model="form.formId" placeholder="请选择">
            <el-option v-for="processFormOption in processFormList" :key="processFormOption.id"
                       :label="processFormOption.formName" :value="processFormOption.id"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="业务表单" prop="formUrl" v-if="form.formType === ProcessFormType.BUSINESS_FORM">
          <el-input v-model="form.formUrl" placeholder="请输入地址"/>
        </el-form-item>

        <el-form-item label="在线表单" prop="formId" v-if="form.formType === ProcessFormType.ONLINE_FORM">
          <el-select v-model="form.formId" placeholder="请选择" @change="onOnlineFormChange">
            <el-option v-for="item in onlineFormList" :key="item.id" :label="item.formName" :value="item.id"/>
          </el-select>
        </el-form-item>

        <el-form-item label="默认页面" prop="pageId" v-if="form.formType === ProcessFormType.ONLINE_FORM">
          <el-select v-model="form.pageId" placeholder="请选择">
            <el-option v-for="item in defOnlinePageList" :key="item.id" :label="item.pageName" :value="item.id"/>
          </el-select>
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
    <!-- 流程模型图的预览 -->
    <el-dialog title="流程图" :visible.sync="openProcessView" width="600px" append-to-body>
      <process-view key="designer" v-model="processXml"/>
    </el-dialog>
  </div>
</template>

<script>
  import {
    selectProcessModelPage,
    detailProcessModel,
    deleteProcessModel,
    addProcessModel,
    updateProcessModel,
    processDeployment
  } from '@/api/process/model'
  import { selectProcessFormList } from '@/api/process/form'
  import { selectOnlineProcessFormList } from '@/api/online/form'

  export default {
    name: 'ProcessModel',
    data() {
      return {
        // 遮罩层
        loading: true,
        // 已选择的列表
        selectRowList: [],
        // 在线表单列表
        onlineFormList: [],
        // 在线表单页面
        onlinePageList: [],
        // 在线表单默认页面
        defOnlinePageList: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 流程表格数据
        modelList: [],
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        openDialog: false,
        // 流程表单列表
        processFormList: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            modelCode: undefined,
            modelName: undefined,
            deploymentStatus: undefined
          }
        },
        openProcessView: false,
        processXml: undefined,
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          modelCode: [
            { required: true, message: '流程编码不能为空', trigger: 'blur' },
            { pattern: /[a-zA-Z_][\-_.0-9_a-zA-Z$]*/, message: '请以字母或下划线开头' }
          ],
          modelName: [
            { required: true, message: '流程名称不能为空', trigger: 'blur' }
          ],
          formType: [
            { required: true, message: '流程表单类型不能为空', trigger: 'blur' }
          ],
          formId: [
            { required: true, message: '流程表单不能为空', trigger: 'blur' }
          ],
          formName: [
            { required: true, message: '表单名称不能为空', trigger: 'blur' }
          ],
          formUrl: [
            { required: true, message: '表单地址不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getProcessFormList()
      this.getProcessModelList()
      this.getOnlineProcessFormList()
    },
    methods: {
      /** 查询流程表单列表 */
      getProcessFormList() {
        selectProcessFormList().then(response => {
            this.processFormList = response.data
          }
        )
      },
      /** 查询在线表单列表 */
      getOnlineProcessFormList() {
        selectOnlineProcessFormList().then(response => {
            this.onlineFormList = response.data.onlineFormList
            this.onlinePageList = response.data.onlinePageList
          }
        )
      },
      /** 表单类型修改事件 */
      formTypeChange() {
        this.form.formId = ''
        this.form.formName = ''
        this.form.pageId = ''
        this.defOnlinePageList = []
      },
      onOnlineFormChange() {
        const selectedForm = this.onlineFormList.find(item => item.id === this.form.formId)
        this.defOnlinePageList = this.onlinePageList.filter(item => item.formId = this.form.formId)
        if (selectedForm) {
          this.form.formName = selectedForm.formName
        } else {
          this.form.pageId = undefined
          this.defOnlinePageList = []
        }
      },
      /** 查询流程列表 */
      getProcessModelList() {
        this.loading = true
        selectProcessModelPage(this.queryParam).then(response => {
          this.queryParam.pageTotal = response.data.pageTotal
          this.modelList = response.data.records
          this.loading = false
        })
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
          formType: '01',
          formId: undefined,
          formName: undefined,
          formUrl: undefined,
          pageId: undefined,
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
      /** 流程查看 */
      handleProcessView(row) {
        detailProcessModel(row.id).then(response => {
            this.processXml = response.data.modelXml
            this.openProcessView = true
          }
        )
      },
      /** 新增按钮操作 */
      handleProcessAdd() {
        this.reset()
        this.openDialog = true
        this.dialogTitle = '添加流程'
      },
      // 更多操作触发
      handleProcessCommand(command, row) {
        switch (command) {
          case 'handleProcessUpdate':
            this.handleProcessUpdate(row)
            break
          case 'handleProcessDefinition':
            this.handleProcessDefinition(row)
            break
          case 'handleProcessDelete':
            this.handleProcessDelete(row)
            break
          default:
            break
        }
      },
      /** 修改按钮操作 */
      handleProcessUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailProcessModel(row.id).then(response => {
          this.form = response.data
          this.openDialog = true
          this.dialogTitle = '修改流程'
        })
      },
      /** 流程设计操作 */
      handleProcessDesigner(row) {
        this.$router.push('/process/designer/' + row.id)
      },
      /** 流程定义 */
      handleProcessDefinition(row) {
        this.$router.push('/process/definition/' + row.id)
      },
      handleProcessDeployment(row) {
        processDeployment(row.id).then(response => {
          this.messageSuccess('流程发布成功')
          this.getProcessModelList()
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
      handleProcessDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除流程名称为"' + row.modelName + '"的数据项?', '警告', {
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
