<template>
  <div class="scene-execute-container">
    <el-form ref="baseForm" :model="sceneInfo" label-width="100px" size="mini">
      <el-row>
        <el-col :span="12">
          <el-form-item label="场景编码：">{{ sceneInfo.sceneCode }}</el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="场景名称：">{{ sceneInfo.sceneName }}</el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-tabs v-model="activeName" @tab-click="handleClickTab">
      <el-tab-pane label="基本信息" name="base">
        <el-form ref="baseForm" :model="sceneInfo" label-width="100px" size="mini">
          <el-row>
            <el-col :span="12">
              <el-form-item label="场景编码：">{{ sceneInfo.sceneCode }}</el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="场景名称：">{{ sceneInfo.sceneName }}</el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="绑定规则：">
                <div v-for="item in sceneInfo.droolsRuleList">
                  <router-link :key="item.id"
                               :to="'/drools/rule/designer/' + item.id" class="link-type"
                  >
                    <span>{{ item.ruleName }}</span>
                  </router-link>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="关联对象：">
                <div v-for="item in sceneInfo.droolsModelList">
                  <router-link :key="item.id"
                               :to="'/drools/model/field/' + item.id" class="link-type"
                  >
                    <span>{{ item.modelName }}</span>
                  </router-link>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="场景描述：">{{ sceneInfo.sceneDesc }}</el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="调用示例" name="test">
        <el-form ref="testForm" :model="sceneInfo" label-width="100px" size="mini">
          <el-row>
            <el-col :span="24">
              <el-form-item label="接口地址：">{{ address }}</el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="请求方式：">post</el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="返回类型：">json</el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="接口描述：">{{ sceneInfo.sceneDesc }}</el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="接口调试：">
                <el-button type="text" size="mini" @click="handleOpenExecute">在线测试</el-button>
              </el-form-item>
            </el-col>
          </el-row>
          <el-dialog title="规则执行" :visible.sync="openExecute" width="750px" append-to-body>
            <el-divider content-position="left">执行参数</el-divider>
            <el-scrollbar style="height:230px;" wrap-style="overflow-x:hidden;">
              <param-data v-for="item in sceneInfo.droolsModelList.filter(item=>item.inputFlag===AgileYesNo.YES)"
                          :key="item.id" :drools-model="item"
                          :model-data="paramData[item.modelName]"
              />
              <el-card class="box-card">
                <div slot="header" style="justify-content:center;padding: 0 0 0 10px">
                  <span style="line-height: 38px">Map对象出入参</span>
                </div>
                <el-form :ref="paramData" :model="paramData" label-width="80px">
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="入参" :prop="paramData.inputParam">
                        <el-input v-model="paramData.inputParam" type="textarea"
                                  placeholder="仅支持json格式数据，最终将转为Map对象使用！"/>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="出参" :prop="paramData.outputParam">
                        <el-input v-model="paramData.outputParam" type="textarea"
                                  placeholder="仅支持json格式数据，最终将转为Map对象使用！"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>
                </el-form>
              </el-card>
            </el-scrollbar>

            <el-divider content-position="left">执行结果</el-divider>
            <el-form ref="executeResults" label-width="80px">
              <el-input v-model="executeResults" type="textarea" placeholder="规则执行结果，仅展示出参对象"/>
            </el-form>

            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="handleSubmitExecute">执 行</el-button>
              <el-button @click="openExecute=false">取 消</el-button>
            </div>
          </el-dialog>

        </el-form>
      </el-tab-pane>
      <el-tab-pane label="执行日志" name="logger" style="height:calc(100vh - 140px) ">
        <div>
          <el-scrollbar style="height:310px;" wrap-style="overflow-x:hidden;">
            <el-table v-loading="loading" :data="loggerList">
              <el-table-column label="执行场景" align="center" prop="sceneName"/>
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
                  <el-button v-hasPerm="['system:log:detail']" size="mini" type="text" icon="el-icon-view"
                             @click="handleViewLogger(scope.row)"
                  >
                    详细
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-scrollbar>

          <!-- 分页 -->
          <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                      :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                      @pagination="getDroolsLoggerList"
          />

          <!-- 操作日志详细 -->
          <el-dialog title="操作日志详细" :visible.sync="openLogger" width="750px" append-to-body>
            <el-form ref="loggerForm" :model="loggerForm" label-width="100px" size="mini">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="场景编码：">{{ loggerForm.sceneCode }}</el-form-item>
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
                  <el-form-item v-if="loggerForm.droolsLoggerRuleList" label="执行规则：">
                    <el-row style="padding-bottom: 10px">共执行规则
                      <span style="color: #13ce66">{{ loggerForm.ruleCount }}</span> 条
                    </el-row>
                    <el-row>
                      <el-table :data="loggerForm.droolsLoggerRuleList" style="border: 1px solid #dfe6ec">
                        <el-table-column label="规则名称" align="center" prop="ruleName"/>
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
                      </el-table>
                    </el-row>
                  </el-form-item>
                </el-col>
                <el-col v-if="loggerForm.executeParam" :span="24">
                  <el-form-item label="执行参数：">{{ loggerForm.executeParam }}</el-form-item>
                </el-col>
                <el-col v-if="loggerForm.executeResult" :span="24">
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
      </el-tab-pane>
      <el-tab-pane label="统计指标" name="statistic">
        <el-row>
          <el-col :span="4">
            <el-statistic title="执行总次数" :value="statisticInfo.executeCount"/>
          </el-col>
          <el-col :span="4">
            <el-statistic title="执行成功次数" :value="statisticInfo.successCount"/>
          </el-col>
          <el-col :span="4">
            <el-statistic title="执行失败次数" :value="statisticInfo.errorCount"/>
          </el-col>
          <el-col :span="4">
            <el-statistic title="最长执行时间（ms）" :value="statisticInfo.maxTime"/>
          </el-col>
          <el-col :span="4">
            <el-statistic title="平均执行时间（ms）" :value="statisticInfo.averageTime"/>
          </el-col>
          <el-col :span="4">
            <el-statistic title="最短执行时间（ms）" :value="statisticInfo.minTime"/>
          </el-col>
        </el-row>
        <el-row style="padding-top: 10px">
          <el-form :inline="true" label-width="80px">
            <el-form-item label="执行时间">
              <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd"
                              type="daterange" range-separator="-" start-placeholder="开始日期"
                              end-placeholder="结束日期"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleStatistic">统计</el-button>
            </el-form-item>
          </el-form>
        </el-row>
        <el-row>
          <el-scrollbar style="height:310px;" wrap-style="overflow-x:hidden;">
            <el-col :span="8">
              <el-card class="box-card">
                <div slot="header" class="clearfix">
                  <span style="font-weight: bold;color: #666;font-size: 15px">执行总次数/日</span>
                </div>
                <div class="statisticChart">
                  <ve-line ref="statisticExecuteCount" height="100%" :data="statisticExecuteCount"
                           :extend="extend"
                  />
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="box-card">
                <div slot="header" class="clearfix">
                  <span style="font-weight: bold;color: #666;font-size: 15px">执行成功次数/日</span>
                </div>
                <div class="statisticChart">
                  <ve-line ref="statisticSuccessCount" height="100%" :data="statisticSuccessCount"
                           :extend="extend"
                  />
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="box-card">
                <div slot="header" class="clearfix">
                  <span style="font-weight: bold;color: #666;font-size: 15px">执行失败次数/日</span>
                </div>
                <div class="statisticChart">
                  <ve-line ref="statisticErrorCount" height="100%" :data="statisticErrorCount" :extend="extend"/>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="box-card">
                <div slot="header" class="clearfix">
                  <span style="font-weight: bold;color: #666;font-size: 15px">最长执行时间(ms)/日</span>
                </div>
                <div class="statisticChart">
                  <ve-line ref="statisticMaxTime" height="100%" :data="statisticMaxTime" :extend="extend"/>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="box-card">
                <div slot="header" class="clearfix">
                  <span style="font-weight: bold;color: #666;font-size: 15px">平均执行时间(ms)/日</span>
                </div>
                <div class="statisticChart">
                  <ve-line ref="statisticAverageTime" height="100%" :data="statisticAverageTime"
                           :extend="extend"
                  />
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="box-card">
                <div slot="header" class="clearfix">
                  <span style="font-weight: bold;color: #666;font-size: 15px">最短执行时间(ms)/日</span>
                </div>
                <div class="statisticChart">
                  <ve-line ref="statisticMinTime" height="100%" :data="statisticMinTime" :extend="extend"/>
                </div>
              </el-card>
            </el-col>
          </el-scrollbar>
        </el-row>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import { detailDroolsSceneInfo, executeDroolsScene } from '@/api/drools/scene'
  import { selectDroolsLoggerPage, detailDroolsLogger, droolsStatistic } from '@/api/drools/logger'
  import ParamData from '../param/index'

  export default {
    name: 'SceneExecute',
    components: {
      ParamData
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 默认
        activeName: 'base',
        // 场景ID
        sceneId: undefined,
        address: undefined,
        // 场景信息
        sceneInfo: {
          droolsModelList: []
        },
        // 场景表格数据
        sceneList: [],
        openExecute: false,
        paramData: { inputParam: undefined, outputParam: undefined },
        executeResults: '',
        loggerList: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            sceneId: this.sceneId
          }
        },
        openLogger: false,
        loggerForm: { droolsLoggerRuleList: [] },
        statisticInfo: {
          executeCount: 0,
          successCount: 0,
          errorCount: 0,
          maxTime: 0,
          averageTime: 0,
          minTime: 0
        },
        extend: {
          series: {
            smooth: false,
            color: '#0443db',
            label: {
              show: true,
              color: '#050505'
            }
          },
          legend: {
            show: false
          }
        },
        statisticExecuteCount: {
          columns: ['executeDate', 'executeCount'],
          rows: []
        },
        statisticSuccessCount: {
          columns: ['executeDate', 'successCount'],
          rows: []
        },
        statisticErrorCount: {
          columns: ['executeDate', 'errorCount'],
          rows: []
        },
        statisticMaxTime: {
          columns: ['executeDate', 'maxTime'],
          rows: []
        },
        statisticAverageTime: {
          columns: ['executeDate', 'averageTime'],
          rows: []
        },
        statisticMinTime: {
          columns: ['executeDate', 'minTime'],
          rows: []
        },
        dateRange: []
      }
    },
    created() {
      this.sceneId = this.$route.params && this.$route.params.sceneId
      this.detailDroolsScene()
      this.getDroolsLoggerList()
    },
    methods: {
      detailDroolsScene() {
        detailDroolsSceneInfo(this.sceneId).then(response => {
          this.sceneId = response.data.id
          this.sceneInfo = response.data
          this.address = window.location.origin + '/drools/scene/execute/'
          this.queryParam.queryCond.sceneId = this.sceneId
          this.loading = true
        })
      },
      handleOpenExecute() {
        this.executeResults = ''

        const that = this

        function addModelData(modelData, droolsModeInfo) {
          if (!modelData) {
            modelData = {}
          }
          droolsModeInfo.droolsModelFieldList.forEach(field => {
            if (field.fieldType === that.DroolsFieldType.Object) {
              if (field.listFlag === that.AgileYesNo.YES) {
                const tempData = addModelData({}, field.droolsModelInfo)
                modelData[field.fieldName] = [tempData]
              } else {
                modelData[field.fieldName] = addModelData({}, field.droolsModelInfo)
              }
            } else {
              if (field.listFlag === that.AgileYesNo.YES) {
                modelData[field.fieldName] = [field.fieldType === that.DroolsFieldType.Boolean ? false : undefined]
              } else {
                modelData[field.fieldName] = field.fieldType === that.DroolsFieldType.Boolean ? false : undefined
              }
            }
          })
          return modelData
        }

        this.sceneInfo.droolsModelList.forEach(droolsModeInfo => {
          let modelData = this.paramData[droolsModeInfo.modelName]
          this.paramData[droolsModeInfo.modelName] = addModelData(modelData, droolsModeInfo)
        })


        this.openExecute = true
      },
      handleSubmitExecute() {
        this.executeResults = ''
        executeDroolsScene(this.sceneInfo.sceneCode, this.paramData).then(response => {
          this.executeResults = JSON.stringify(response.data, null, '\t')
        })
      },
      handleClickTab() {
        if (this.activeName === 'logger') {
          this.getDroolsLoggerList()
        }
        if (this.activeName === 'statistic') {
          this.dateRange = []
          this.getDroolsStatistic()
        }
      },
      getDroolsLoggerList() {
        this.loading = true
        selectDroolsLoggerPage(this.queryParam).then(response => {
            this.loggerList = response.data.records
            this.queryParam.pageTotal = response.data.pageTotal
            this.loading = false
          }
        )
      },
      handleViewLogger(row) {
        detailDroolsLogger(row.id).then(response => {
          this.loggerForm = response.data
          this.openLogger = true
        })
      },
      getDroolsStatistic() {
        let queryParam = { sceneId: this.sceneId }
        droolsStatistic(queryParam).then(response => {
          this.statisticInfo = response.data
          this.statisticExecuteCount.rows = response.data.statisticExecuteCount
          this.statisticSuccessCount.rows = response.data.statisticSuccessCount
          this.statisticErrorCount.rows = response.data.statisticErrorCount
          this.statisticMaxTime.rows = response.data.statisticMaxTime
          this.statisticAverageTime.rows = response.data.statisticAverageTime
          this.statisticMinTime.rows = response.data.statisticMinTime
          this.$refs.statisticExecuteCount.echarts.resize()
          this.$refs.statisticSuccessCount.echarts.resize()
          this.$refs.statisticErrorCount.echarts.resize()
          this.$refs.statisticMaxTime.echarts.resize()
          this.$refs.statisticAverageTime.echarts.resize()
          this.$refs.statisticMinTime.echarts.resize()
        })
      },
      handleStatistic() {
        let queryParam = { sceneId: this.sceneId }
        if (this.dateRange?.length > 0) {
          queryParam.startTime = this.dateRange[0]
          queryParam.endTime = this.dateRange[1]
        }
        droolsStatistic(queryParam).then(response => {
          this.statisticExecuteCount.rows = response.data.statisticExecuteCount
          this.statisticSuccessCount.rows = response.data.statisticSuccessCount
          this.statisticErrorCount.rows = response.data.statisticErrorCount
          this.statisticMaxTime.rows = response.data.statisticMaxTime
          this.statisticAverageTime.rows = response.data.statisticAverageTime
          this.statisticMinTime.rows = response.data.statisticMinTime
          this.$refs.statisticExecuteCount.echarts.resize()
          this.$refs.statisticSuccessCount.echarts.resize()
          this.$refs.statisticErrorCount.echarts.resize()
          this.$refs.statisticMaxTime.echarts.resize()
          this.$refs.statisticAverageTime.echarts.resize()
          this.$refs.statisticMinTime.echarts.resize()
        })
      }
    }
  }
</script>

<style lang='scss'>
  .scene-execute-container {
    padding: 20px;
    height: calc(100vh - 120px);

    .scene-execute-left {
      overflow: hidden;
      background: white;
      border-right: 4px solid #cad1d2;
    }

    .scene-execute-main {
      overflow: hidden;
      background: white;
      padding-left: 20px;
    }

    .box-card {
      margin: 5px;

      .el-card__body {
        margin: 5px;
        padding: 5px;
      }

      span {
        margin-right: 28px;
      }

      .statisticChart {
        height: 220px;
        width: 100%;
      }
    }
  }
</style>
