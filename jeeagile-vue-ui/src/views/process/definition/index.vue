<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="流程模型" prop="processId">
        <el-select v-model="queryParam.queryCond.processId" placeholder="流程模型" clearable size="small"
                   @change="handleQuery">
          <el-option v-for="processModelOption in modelList"
                     :key="processModelOption.id"
                     :label="processModelOption.processName" :value="processModelOption.id"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getProcessModelList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="definitionList">
      <el-table-column label="流程编码" align="center" prop="processCode"/>
      <el-table-column label="流程名称" align="center" prop="processName" :show-overflow-tooltip="true"/>
      <el-table-column label="流程版本" align="center" prop="processVersion">
        <template slot-scope="scope">
          <span>v{{scope.row.processVersion}}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" align="center" prop="deploymentTime" width="150px"/>
      <el-table-column label="流程状态" align="center" prop="suspensionState">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.suspensionState === 1">激活</el-tag>
          <el-tag type="warning" v-if="scope.row.suspensionState === 2">挂起</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="主版本" align="center" prop="mainVersion">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.mainVersion === 1">主版本</el-tag>
          <el-tag type="warning" v-if="scope.row.mainVersion === 2">非主版本</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="350" align="center" class-name="small-padding">
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
          <el-button v-if="scope.row.mainVersion==2" size="mini" type="text" icon="el-icon-setting"
                     @click="handleMainVersion(scope.row)" v-hasPerm="['process:definition:main']">
            设置为主版本
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getProcessDefinitionList"/>
    <!-- 流程模型图的预览 -->
    <el-dialog title="流程图" :visible.sync="openProcessView" width="650px" append-to-body>
      <process-view key="designer" v-model="processXml" style="height: 350px"/>
    </el-dialog>
    <!-- 表单预览 -->
    <el-dialog title="表单预览" :visible.sync="openFormView" width="650px" append-to-body>
      <div class="test-form" v-if="processDefinition.formType === this.ProcessFormType.PROCESS_FORM">
        <process-form-parser :key="new Date().getTime()" :form-conf="parserForm"/>
      </div>
      <div class="test-form" v-if="processDefinition.formType === this.ProcessFormType.ONLINE_FORM">
        <online-form-parser :key="new Date().getTime()" :page-id="processDefinition.pageId"
                            :process-id="processDefinition.id" :page-type="OnlinePageType.FLOW"/>
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
    deleteProcessDefinition,
    updateMainVersion
  } from '@/api/process/definition'
  import {
    selectProcessList
  } from '@/api/process/designer'
  import ProcessFormParser from '@/components/FormDesigner/parser/Parser'
  import OnlineFormParser from '../../online/index'

  export default {
    name: 'ProcessDefinition',
    components: { ProcessFormParser, OnlineFormParser },
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
        processDefinition: {},
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
            processId: undefined
          }
        }
      }
    },
    created() {
      const processId = this.$route.params && this.$route.params.processId
      this.queryParam.queryCond.processId = processId
      this.getProcessModelList()
      this.getProcessDefinitionList()
    },
    methods: {
      /** 查询流程模型列表 */
      getProcessModelList() {
        selectProcessList().then(response => {
          this.modelList = response.data
        })
      },
      /** 查询流程定义列表 */
      getProcessDefinitionList() {
        this.loading = true
        selectProcessDefinitionPage(this.queryParam).then(response => {
          this.queryParam.pageTotal = response.data.pageTotal
          this.definitionList = response.data.records
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
              this.processXml = response.data.processXml
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
            this.processDefinition = response.data
            if (response.data.formType === this.ProcessFormType.PROCESS_FORM) {
              if (response.data.formType === '1') {
                if (response.data.formConf && response.data.formFields) {
                  this.parserForm = {
                    fields: JSON.parse(response.data.formFields),
                    ...JSON.parse(response.data.formConf)
                  }
                }
              }
            }
            this.openFormView = true
          })
        })
      },
      /** 流程定义激活 */
      handleProcessActive(row) {
        activeProcessDefinition(row.id).then(response => {
            this.getProcessDefinitionList()
            this.messageSuccess('流程定义激活成功')
          }
        )
      },
      /** 流程定义挂起 */
      handleProcessSuspend(row) {
        suspendProcessDefinition(row.id).then(response => {
            this.getProcessDefinitionList()
            this.messageSuccess('流程定义挂起成功')
          }
        )
      },
      /** 设置主版本 */
      handleMainVersion(row) {
        updateMainVersion(row.id).then(response => {
            this.getProcessDefinitionList()
            this.messageSuccess('流程定义主版本设置成功')
          }
        )
      },
      /** 删除按钮操作 */
      handleProcessDefinitionDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除流程名称为"' + row.processName + '"的数据项?', '警告', {
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
