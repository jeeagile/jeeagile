<template>
  <div class="app-container">
    <el-form :model="queryParam.queryCond" ref="queryForm" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="场景编码" prop="sceneCode">
        <el-input v-model="queryParam.queryCond.sceneCode" placeholder="请输入场景编码" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="场景名称" prop="sceneName">
        <el-input v-model="queryParam.queryCond.sceneName" placeholder="请输入场景名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="执行时间">
        <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd"
                        type="daterange" range-separator="-" start-placeholder="开始日期"
                        end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>

    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getLoggerList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="loggerList">
      <el-table-column label="场景编码" align="center" prop="sceneCode"/>
      <el-table-column label="场景名称" align="center" prop="sceneName"/>
      <el-table-column label="执行状态" align="center" prop="executeStatus">
        <template slot-scope="scope">
          <el-tag size="mini" :type="AgileSuccessFail.getTag(scope.row.executeStatus)">
            {{ AgileSuccessFail.getLabel(scope.row.executeStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" align="center" prop="startTime"/>
      <el-table-column label="结束时间" align="center" prop="endTime"/>
      <el-table-column label="执行时间" align="center" prop="executeTime">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.executeTime < 3000" type="success" size="mini">{{
            scope.row.executeTime
            }}ms
          </el-tag>
          <el-tag v-else-if="scope.row.executeTime < 9000" type="warning" size="mini">{{
            scope.row.executeTime
            }}ms
          </el-tag>
          <el-tag v-else size="mini" type="danger">{{ scope.row.executeTime }}ms</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleViewLogger(scope.row)"
                     v-hasPerm="['system:log:detail']">
            详细
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getDroolsLoggerList"/>

    <!-- 操作日志详细 -->
    <el-dialog title="操作日志详细" :visible.sync="openLogger" width="750px" append-to-body>
      <el-form ref="loggerForm" :model="loggerForm" label-width="100px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="场景编码：">{{ loggerForm.sceneName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="场景名称：">{{ loggerForm.sceneName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行状态：">
              <el-tag size="mini" :type="AgileSuccessFail.getTag(loggerForm.executeStatus)">
                {{ AgileSuccessFail.getLabel(loggerForm.executeStatus) }}
              </el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行时间：">{{ loggerForm.executeTime }}ms</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始时间：">{{ loggerForm.startTime }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间：">{{ loggerForm.endTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行规则：" v-if="loggerForm.droolsLoggerRuleList">
              <el-row style="padding-bottom: 10px">
                共执行规则 <span style="color: #13ce66"> {{ loggerForm.ruleCount }} </span>条
              </el-row>
              <el-row>
                <el-table :data="loggerForm.droolsLoggerRuleList" style="border: 1px solid #dfe6ec">
                  <el-table-column label="规则名称" align="center" prop="ruleName"/>
                  <el-table-column label="开始时间" align="center" prop="startTime"/>
                  <el-table-column label="结束时间" align="center" prop="endTime"/>
                  <el-table-column label="执行时间" align="center" prop="executeTime">
                    <template slot-scope="scope">
                      <el-tag v-if="scope.row.executeTime < 3000" type="success" size="mini">
                        {{ scope.row.executeTime }}ms
                      </el-tag>
                      <el-tag v-else-if="scope.row.executeTime < 9000" type="warning" size="mini">
                        {{ scope.row.executeTime }}ms
                      </el-tag>
                      <el-tag v-else size="mini" type="danger">{{ scope.row.executeTime }}ms</el-tag>
                    </template>
                  </el-table-column>
                </el-table>
              </el-row>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="loggerForm.executeParam">
            <el-form-item label="执行参数：">{{ loggerForm.executeParam }}</el-form-item>
          </el-col>
          <el-col :span="24" v-if="loggerForm.executeResult">
            <el-form-item label="执行结果：">{{ loggerForm.executeResult }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item v-if="loggerForm.executeStatus != '1'" label="异常信息：">
              {{ loggerForm.errorMsg }}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openLogger = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { detailDroolsLogger, selectDroolsLoggerPage } from '@/api/drools/logger';

  export default {
    name: 'Log',
    data() {
      return {
        // 遮罩层
        loading: true,
        // 显示搜索条件
        showSearch: true,
        // 参数表格数据
        loggerList: [],
        // 是否显示弹出层
        openLogger: false,
        // 表单参数
        loggerForm: {},
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            sceneCode: undefined,
            sceneName: undefined
          }
        }, dateRange: []
      }
    },
    created() {
      this.getDroolsLoggerList()
    },
    methods: {
      /** 查询参数列表 */
      getDroolsLoggerList() {
        this.loading = true
        if (this.dateRange?.length > 0) {
          this.queryParam.queryCond.startTime = this.dateRange[0]
          this.queryParam.queryCond.endTime = this.dateRange[1]
        }
        selectDroolsLoggerPage(this.queryParam).then(response => {
            this.loggerList = (response.data && response.data.records) || []
            this.queryParam.pageTotal = (response.data && response.data.pageTotal) || 0
            this.loading = false
          }
        )
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getDroolsLoggerList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = []
        this.resetForm('queryForm')
        this.handleQuery()
      },
      handleViewLogger(row) {
        detailDroolsLogger(row.id).then(response => {
          this.loggerForm = response.data
          this.openLogger = true
        })
      }
    }
  }
</script>
