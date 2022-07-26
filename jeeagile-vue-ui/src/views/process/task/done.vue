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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getProcessDoneList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="processList">
      <el-table-column label="模型编码" align="center" prop="modelCode"/>
      <el-table-column label="模型名称" align="center" prop="modelName" :show-overflow-tooltip="true"/>
      <el-table-column label="表单名称" align="center" prop="formName" :show-overflow-tooltip="true"/>
      <el-table-column label="发起人" width="150" align="center" prop="startUserName"/>
      <el-table-column label="提交时间" width="150" align="center" prop="startTime"/>
      <el-table-column label="结束时间" width="150" align="center" prop="endTime"/>
      <el-table-column label="操作" width="200px" align="center" class-name="small-padding">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetailInstance(scope.row)">
            查看详细
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getProcessDoneList"/>
  </div>
</template>

<script>
  import {
    selectDonePage
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
        processList: [],
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
      this.getProcessDoneList()
      this.getSysDictDataList('process_task_status').then(response => {
        this.taskStatusOptionList = response.data
      })
    },
    methods: {
      /** 查询流程表单列表 */
      getProcessDoneList() {
        selectDonePage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.processList = response.data.records
            this.loading = false
          }
        )
      },
      /** 流程查看 */
      handleDetailInstance(row) {
        this.$router.push({ path: '/process/instance/detail/' + row.instanceId })
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getProcessDoneList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      }
    }
  }
</script>
