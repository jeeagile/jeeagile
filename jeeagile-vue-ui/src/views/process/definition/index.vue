<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程模型" prop="modelId">
        <el-select v-model="queryParam.queryCond.modelId" placeholder="流程模型" clearable size="small"
                   @change="handleQuery">
          <el-option v-for="processModelOption in modelList"
                     :key="processModelOption.id"
                     :label="processModelOption.modelName" :value="processModelOption.id"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!--      <el-col :span="1.5">-->
      <!--        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleProcessAdd"-->
      <!--                   v-hasPerm="['process:model:add']">-->
      <!--          新增-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <!--      <el-col :span="1.5">-->
      <!--        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleProcessUpdate"-->
      <!--                   v-hasPerm="['process:model:update']">-->
      <!--          修改-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <!--      <el-col :span="1.5">-->
      <!--        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleProcessDelete"-->
      <!--                   v-hasPerm="['process:model:delete']">-->
      <!--          删除-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getProcessModelList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="definitionList">
      <el-table-column label="模型编码" align="center" prop="modelCode"/>
      <el-table-column label="模型名称" align="center" prop="modelName"/>
      <el-table-column label="流程版本" align="center" prop="modelVersion">
        <template slot-scope="scope">
          <span>v{{scope.row.modelVersion}}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" align="center" prop="deploymentTime" width="150px"/>
      <el-table-column label="流程状态" align="center" prop="suspensionState">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.suspensionState === 1">激活</el-tag>
          <el-tag type="warning" v-if="scope.row.suspensionState === 2">挂起</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300" align="center" class-name="small-padding">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleProcessView(scope.row)">
            流程
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleFormView(scope.row)">
            表单
          </el-button>
          <el-button v-if="scope.row.suspensionState==1" size="mini" type="text" icon="el-icon-s-custom"
                     @click="handleProcessSuspend (scope.row)" v-hasPerm="['process:definition:suspend']">
            挂起
          </el-button>
          <el-button v-if="scope.row.suspensionState==2" size="mini" type="text" icon="el-icon-s-custom"
                     @click="handleProcessActive(scope.row)" v-hasPerm="['process:definition:active']">
            激活
          </el-button>

          <el-button size="mini" type="text" icon="el-icon-delete"
                     @click="handleProcessDefinitionDelete(scope.row)" v-hasPerm="['process:definition:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getProcessDefinitionList"/>
    <!-- 流程模型图的预览 -->
    <el-dialog title="流程图" :visible.sync="openProcessView" width="600px" append-to-body>
      <process-view key="designer" v-model="processXml"/>
    </el-dialog>
    <el-dialog title="表单预览" :visible.sync="openFormView" width="50%" append-to-body>
      <div class="test-form">
        <form-parser :key="new Date().getTime()" :form-conf="parserForm" @submit="submitFormData"/>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    selectProcessDefinitionPage,
    detailProcessDefinition,
    activeProcessDefinition,
    suspendProcessDefinition,
    deleteProcessDefinition
  } from '@/api/process/definition'
  import {
    selectProcessModelList
  } from '@/api/process/model'
  import FormParser from '@/components/FormDesigner/parser/Parser'

  export default {
    name: 'ProcessDefinition',
    components: { FormParser },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 显示搜索条件
        showSearch: true,
        // 流程表格数据
        modelList: [],
        // 流程表格数据
        definitionList: [],
        openProcessView: false,
        processXml: undefined,
        openFormView: false,
        parserForm: {
          fields: []
        },
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            modelId: undefined
          }
        }
      }
    },
    created() {
      const modelId = this.$route.params && this.$route.params.modelId
      this.queryParam.queryCond.modelId = modelId
      this.getProcessModelList()
      this.getProcessDefinitionList()
    },
    methods: {
      /** 查询流程模型列表 */
      getProcessModelList() {
        selectProcessModelList().then(response => {
          this.modelList = response.data
        })
      },
      /** 查询流程定义列表 */
      getProcessDefinitionList() {
        this.loading = true
        selectProcessDefinitionPage(this.queryParam).then(response => {
          this.queryParam.pageTotal = response.data.pageTotal
          this.definitionList = response.data.recordList
          this.loading = false
        })
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getProcessDefinitionList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 流程查看 */
      handleProcessView(row) {
        this.openProcessView = false
        detailProcessDefinition(row.id).then(response => {
            this.$nextTick(() => {
              this.processXml = response.data.modelXml
              this.openProcessView = true
            })
          }
        )
      },
      /** 表单查看 */
      handleFormView(row) {
        this.openFormView = false
        detailProcessDefinition(row.id).then(response => {
          this.$nextTick(() => {
            if (response.data.formType === '1') {
              if (response.data.formConf && response.data.formFields) {
                this.parserForm = {
                  fields: JSON.parse(response.data.formFields),
                  ...JSON.parse(response.data.formConf)
                }
              }
            }
            this.openFormView = true
          })
        })
      },
      handleProcessActive(row) {
        activeProcessDefinition(row.id).then(response => {
            this.getProcessDefinitionList()
            this.messageSuccess('流程定义激活成功')
          }
        )
      },
      handleProcessSuspend(row) {
        suspendProcessDefinition(row.id).then(response => {
            this.getProcessDefinitionList()
            this.messageSuccess('流程定义挂起成功')
          }
        )
      },
      /** 删除按钮操作 */
      handleProcessDefinitionDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除流程名称为"' + row.modelName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteProcessDefinition(row.id)
        }).then(() => {
          this.getProcessDefinitionList()
          this.messageSuccess('删除成功')
        })
      },
      /** 表单数据模拟提交 */
      submitFormData(data) {
        alert(JSON.stringify(data))
      }
    }
  }
</script>
