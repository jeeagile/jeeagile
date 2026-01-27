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
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getProcessInstanceList"></right-toolbar>
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
      <el-table-column label="提交时间" width="150" align="center" prop="startTime"/>
      <el-table-column label="结束时间" width="150" align="center" prop="endTime"/>
      <el-table-column label="流程状态" align="center" :formatter="instanceStatusFormat"/>
      <el-table-column label="操作" width="200px" align="center" class-name="small-padding">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetailInstance(scope.row)">
            查看详细
          </el-button>
          <el-button v-if="scope.row.instanceStatus == 1" size="mini" type="text" icon="el-icon-delete" @click="handleCancelInstance(scope.row)">
            撤销流程
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getProcessInstanceList"/>
  </div>
</template>

<script>
  import {
    selectApplyProcessPage,
    cancelProcessInstance
  } from '@/api/process/instance'
  export default {
    name: 'Apply',
    data() {
      return {
        // 遮罩层
        loading: true,
        // 显示搜索条件
        showSearch: true,
        instanceStatusOptionList: [],
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
      this.getProcessInstanceList()
      this.getSysDictDataList('process_instance_status').then(response => {
        this.instanceStatusOptionList = response.data
      })
    },
    methods: {
      /** 查询流程表单列表 */
      getProcessInstanceList() {
        selectApplyProcessPage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.processList = response.data.records
            this.loading = false
          }
        )
      },
      /** 参数系统内置字典翻译 */
      instanceStatusFormat(row, column) {
        return this.handleDictLabel(this.instanceStatusOptionList, row.instanceStatus)
      },
      /** 流程查看 */
      handleDetailInstance(row) {
        this.$router.push({ path: '/process/instance/detail/' + row.id })
      },
      /** 流程撤销 */
      handleCancelInstance(row) {
        cancelProcessInstance(row.id).then(response => {
            this.messageSuccess('流程撤销成功！')
            this.handleQuery()
          }
        )
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getProcessInstanceList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      }
    }
  }
</script>
