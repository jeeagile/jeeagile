<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="流程编码" prop="processCode">
        <el-input v-model="queryParam.queryCond.processCode" placeholder="请输入流程编码" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="流程名称" prop="processName">
        <el-input v-model="queryParam.queryCond.processName" placeholder="请输入流程名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="表单名称" prop="processName">
        <el-input v-model="queryParam.queryCond.formName" placeholder="请输入表单名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getProcessOrderList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="processList">
      <el-table-column label="流程编码" align="center" prop="processCode"/>
      <el-table-column label="流程名称" align="center" prop="processName" :show-overflow-tooltip="true"/>
      <el-table-column label="表单名称" align="center" prop="formName" :show-overflow-tooltip="true"/>
      <el-table-column label="流程版本" align="center" prop="processVersion">
        <template slot-scope="scope">
          <span>v{{scope.row.processVersion}}</span>
        </template>
      </el-table-column>
      <el-table-column label="提交时间" width="150" align="center" prop="startTime"/>
      <el-table-column label="结束时间" width="150" align="center" prop="endTime"/>
      <el-table-column label="流程状态" align="center"  prop="orderStatus">
        <template slot-scope="scope">
          {{ ProcessOrderStatus.getLabel(scope.row.orderStatus) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200px" align="center" class-name="small-padding">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetailOrder(scope.row)">
            查看详细
          </el-button>
          <el-button v-if="scope.row.orderStatus === ProcessOrderStatus.SUBMITTED" size="mini" type="text" icon="el-icon-delete" @click="handleCancelOrder(scope.row)">
            撤销流程
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getProcessOrderList"/>
  </div>
</template>

<script>
  import {
    selectApplyProcessPage,
    cancelProcessOrder
  } from '@/api/process/order'
  export default {
    name: 'Apply',
    data() {
      return {
        // 遮罩层
        loading: true,
        // 显示搜索条件
        showSearch: true,
        // 流程列表
        processList: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            processCode: undefined,
            processName: undefined,
            formName: undefined
          }
        }
      }
    },
    created() {
      this.getProcessOrderList()
    },
    methods: {
      /** 查询流程表单列表 */
      getProcessOrderList() {
        selectApplyProcessPage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.processList = response.data.records
            this.loading = false
          }
        )
      },
      /** 流程查看 */
      handleDetailOrder(row) {
        this.$router.push({ path: '/process/order/detail/' + row.id })
      },
      /** 流程撤销 */
      handleCancelOrder(row) {
        cancelProcessOrder(row.id).then(response => {
            this.messageSuccess('流程撤销成功！')
            this.handleQuery()
          }
        )
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getProcessOrderList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      }
    }
  }
</script>
