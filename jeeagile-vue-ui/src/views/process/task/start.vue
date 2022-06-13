<template>
  <div class="app-container">
    <div v-if="selectProcess">
      <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="模型编码" prop="modelCode">
          <el-input v-model="queryParam.queryCond.modelCode" placeholder="请输入模型编码" clearable size="small"
                    @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item label="模型名称" prop="modelName">
          <el-input v-model="queryParam.queryCond.modelName" placeholder="请输入模型名称" clearable size="small"
                    @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item label="表单名称" prop="modelName">
          <el-input v-model="queryParam.queryCond.formName" placeholder="请输入表单名称" clearable size="small"
                    @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getProcessDefinitionList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="processList">
        <el-table-column label="模型编码" align="center" prop="modelCode"/>
        <el-table-column label="模型名称" align="center" prop="modelName" :show-overflow-tooltip="true"/>
        <el-table-column label="表单名称" align="center" prop="formName" :show-overflow-tooltip="true"/>
        <el-table-column label="流程版本" align="center" prop="modelVersion">
          <template slot-scope="scope">
            <span>v{{scope.row.modelVersion}}</span>
          </template>
        </el-table-column>
        <el-table-column label="发布时间" align="center" prop="deploymentTime" width="150px"/>
        <el-table-column label="操作" width="350px" align="center" class-name="small-padding">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-view" @click="handleFormView(scope.row)">
              表单预览
            </el-button>
            <el-button size="mini" type="text" icon="el-icon-view" @click="handleProcessView(scope.row)">
              流程预览
            </el-button>
            <el-button size="mini" type="text" icon="el-icon-plus" @click="handleStartProcess(scope.row)">
              发起流程
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
      <!-- 表单预览 -->
      <el-dialog title="表单预览" :visible.sync="openFormView" width="500px" append-to-body>
        <div class="test-form">
          <form-parser :key="new Date().getTime()" :form-conf="parserForm"/>
        </div>
      </el-dialog>

    </div>
    <div v-else>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span class="el-icon-document">  {{ processDefinition.formName }}</span>
          <el-button style="float: right;" type="primary" size="mini" @click="selectProcess = true">选择其它流程</el-button>
        </div>
        <el-col :span="16" :offset="6">
          <div>
            <form-parser :key="new Date().getTime()" :form-conf="parserForm" @submit="handleSubmitProcess"/>
          </div>
        </el-col>
      </el-card>
      <el-card class="box-card" style="margin-top: 10px">
        <div slot="header" class="clearfix">
          <span class="el-icon-picture-outline">  {{ processDefinition.modelName }} </span>
        </div>
        <process-view key="designer" v-model="processXml"/>
      </el-card>
    </div>
  </div>
</template>

<script>
  import {
    selectProcessDefinitionPage,
    detailProcessDefinition
  } from '@/api/process/task'
  import FormParser from '@/components/FormDesigner/parser/Parser'

  export default {
    name: 'Start',
    components: { FormParser },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选择流程
        selectProcess: true,
        // 显示搜索条件
        showSearch: true,
        // 流程列表
        processList: [],
        openProcessView: false,
        processXml: undefined,
        openFormView: false,
        parserForm: {
          fields: []
        },
        processDefinition: undefined,
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            modelCode: undefined,
            modelName: undefined,
            formName: undefined
          }
        }
      }
    },
    created() {
      this.getProcessDefinitionList()
    },
    methods: {
      /** 查询流程表单列表 */
      getProcessDefinitionList() {
        selectProcessDefinitionPage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.processList = response.data.records
            this.loading = false
          }
        )
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
                let formConf = JSON.parse(response.data.formConf)
                formConf.formBtns = false
                this.parserForm = {
                  fields: JSON.parse(response.data.formFields),
                  ...formConf
                }
              }
            }
            this.openFormView = true
          })
        })
      },
      /** 流程发起 */
      handleStartProcess(row) {
        detailProcessDefinition(row.id).then(response => {
          this.processDefinition = response.data
          this.$nextTick(() => {
            if (response.data.formType === '1') {
              if (response.data.formConf && response.data.formFields) {
                this.parserForm = {
                  fields: JSON.parse(response.data.formFields),
                  ...JSON.parse(response.data.formConf)
                }
              }
              this.processXml = response.data.modelXml
            }
          })
        })
        this.selectProcess = false
      },
      handleSubmitProcess(formData) {
        alert(JSON.stringify(formData))
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
      }
    }
  }
</script>
