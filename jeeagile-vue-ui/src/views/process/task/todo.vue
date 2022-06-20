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
      <el-form-item label="表单名称" prop="modelName">
        <el-input v-model="queryParam.queryCond.formName" placeholder="请输入表单名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="发起人" prop="modelName">
        <el-input v-model="queryParam.queryCond.startUserName" placeholder="请输入流程发起人名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getProcessTodoList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="processTodoList">
      <el-table-column label="模型编码" align="center" prop="modelCode"/>
      <el-table-column label="模型名称" align="center" prop="modelName" :show-overflow-tooltip="true"/>
      <el-table-column label="表单名称" align="center" prop="formName" :show-overflow-tooltip="true"/>
      <el-table-column label="发起人" width="150" align="center" prop="startUserName"/>
      <el-table-column label="创建时间" width="150" align="center" prop="startTime"/>
      <el-table-column label="流程状态" align="center" :formatter="taskStatusFormat"/>
      <el-table-column label="操作" width="200px" align="center" class-name="small-padding">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleProcessTask(scope.row)">
            办理
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleProcessView(scope.row)">
            进度
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getProcessTodoList"/>

    <el-dialog title="流程进度" :visible.sync="openView" width="700px" append-to-body>
      <process-view key="designer" v-model="processXml" :high-line-data="highLineData"/>
    </el-dialog>
  </div>
</template>

<script>
  import {
    selectTodoPage
  } from '@/api/process/task'

  export default {
    name: 'Apply',
    data() {
      return {
        // 遮罩层
        loading: true,
        // 显示搜索条件
        showSearch: true,
        taskStatusOptionList: [],
        // 流程列表
        processTodoList: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            modelCode: undefined,
            modelName: undefined,
            formName: undefined,
            startUserName: undefined
          }
        },
        processXml: undefined,
        highLineData: []
      }
    },
    created() {
      this.getProcessTodoList()
      this.getSysDictDataList('process_task_status').then(response => {
        this.taskStatusOptionList = response.data
      })
    },
    methods: {
      /** 查询流程表单列表 */
      getProcessTodoList() {
        selectTodoPage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.processTodoList = response.data.records
            this.loading = false
          }
        )
      },
      // 参数系统内置字典翻译
      taskStatusFormat(row, column) {
        return this.handleDictLabel(this.instanceStatusOptionList, row.instanceStatus)
      },
      /** 流程办理 */
      handleProcessTask(row) {

      },
      /** 流程进度 */
      handleProcessView(row) {
        detailProcessInstance(row.id).then(response => {
          this.$nextTick(() => {
            this.processXml = response.data().modelXml
            this.highLineData = response.data().highLineData
          })
        })
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getProcessTodoList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      }
    }
  }
</script>
