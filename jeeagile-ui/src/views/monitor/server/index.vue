<template>
  <div v-loading="!show" element-loading-text="数据加载中..." :style="!show ? 'height: 500px' : 'height: 100%'" class="app-container">
    <div v-if="show">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span style="font-weight: bold;color: #666;font-size: 15px">服务器状态</span>
        </div>
        <div>
          <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6" style="margin-bottom: 10px">
            <div class="title">CPU使用率</div>
            <el-tooltip placement="top-end">
              <div slot="content" style="font-size: 12px;">
                <div style="padding: 3px;">
                  {{ data.cpuInfo.cpuName }}
                </div>
                <div style="padding: 3px">
                  {{ data.cpuInfo.packageNum }}个物理CPU
                </div>
                <div style="padding: 3px">
                  {{ data.cpuInfo.coreNum }}个核心CPU
                </div>
                <div style="padding: 3px">
                  {{ data.cpuInfo.logicNum }}个逻辑CPU
                </div>
              </div>
              <div class="content">
                <el-progress type="circle" :percentage="parseFloat(data.cpuInfo.used)"/>
              </div>
            </el-tooltip>
            <div class="footer">{{ data.cpuInfo.coreNum }} 核心</div>
          </el-col>
          <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6" style="margin-bottom: 10px">
            <div class="title">内存使用率</div>
            <el-tooltip placement="top-end">
              <div slot="content" style="font-size: 12px;">
                <div style="padding: 3px;">
                  总量：{{ data.memoryInfo.total }}
                </div>
                <div style="padding: 3px">
                  已使用：{{ data.memoryInfo.used }}
                </div>
                <div style="padding: 3px">
                  空闲：{{ data.memoryInfo.available }}
                </div>
              </div>
              <div class="content">
                <el-progress type="circle" :percentage="parseFloat(data.memoryInfo.usageRate)"/>
              </div>
            </el-tooltip>
            <div class="footer">{{ data.memoryInfo.used }} / {{ data.memoryInfo.total }}</div>
          </el-col>
          <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6" style="margin-bottom: 10px">
            <div class="title">交换区使用率</div>
            <el-tooltip placement="top-end">
              <div slot="content" style="font-size: 12px;">
                <div style="padding: 3px;">
                  总量：{{ data.swapInfo.total }}
                </div>
                <div style="padding: 3px">
                  已使用：{{ data.swapInfo.used }}
                </div>
                <div style="padding: 3px">
                  空闲：{{ data.swapInfo.available }}
                </div>
              </div>
              <div class="content">
                <el-progress type="circle" :percentage="parseFloat(data.swapInfo.usageRate)"/>
              </div>
            </el-tooltip>
            <div class="footer">{{ data.swapInfo.used }} / {{ data.swapInfo.total }}</div>
          </el-col>
          <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6" style="margin-bottom: 10px">
            <div class="title">磁盘使用率</div>
            <div class="content">
              <el-tooltip placement="top-end">
                <div slot="content" style="font-size: 12px;">
                  <div style="padding: 3px">
                    总量：{{ data.diskInfo.total }}
                  </div>
                  <div style="padding: 3px">
                    已使用：{{ data.diskInfo.used }}
                  </div>
                  <div style="padding: 3px">
                    空闲：{{ data.diskInfo.available }}
                  </div>
                </div>
                <div class="content">
                  <el-progress type="circle" :percentage="parseFloat(data.diskInfo.usageRate)"/>
                </div>
              </el-tooltip>
            </div>
            <div class="footer">{{ data.diskInfo.used }} / {{ data.diskInfo.total }}</div>
          </el-col>
        </div>
      </el-card>
      <el-card class="box-card">
        <div slot="header">
          <span>服务器信息</span>
        </div>
        <div class="el-table el-table--enable-row-hover el-table--medium">
          <table cellspacing="0" style="width: 100%;">
            <tbody>
            <tr>
              <td>
                <div class="cell">服务器名称</div>
              </td>
              <td>
                <div class="cell" v-if="data.hostInfo">{{ data.hostInfo.name }}</div>
              </td>
              <td>
                <div class="cell">操作系统</div>
              </td>
              <td>
                <div class="cell" v-if="data.osInfo">{{ data.osInfo.name }}</div>
              </td>
            </tr>
            <tr>
              <td>
                <div class="cell">服务器IP</div>
              </td>
              <td>
                <div class="cell" v-if="data.hostInfo">{{ data.hostInfo.address }}</div>
              </td>
              <td>
                <div class="cell">系统架构</div>
              </td>
              <td>
                <div class="cell" v-if="data.osInfo">{{ data.osInfo.arch }}</div>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </el-card>
      <el-card class="box-card">
        <div slot="header">
          <span>Java虚拟机信息</span>
        </div>
        <div class="el-table el-table--enable-row-hover el-table--medium">
          <table cellspacing="0" style="width: 100%;">
            <tbody>
            <tr>
              <td><div class="cell">Java名称</div></td>
              <td><div class="cell" v-if="data.jvmInfo">{{ data.jvmInfo.name }}</div></td>
              <td><div class="cell">Java版本</div></td>
              <td><div class="cell" v-if="data.jvmInfo">{{ data.jvmInfo.version }}</div></td>
            </tr>
            <tr>
              <td><div class="cell">启动时间</div></td>
              <td><div class="cell" v-if="data.jvmInfo">{{ data.jvmInfo.startTime }}</div></td>
              <td><div class="cell">运行时长</div></td>
              <td><div class="cell" v-if="data.jvmInfo">{{ data.jvmInfo.runTime }}</div></td>
            </tr>
            <tr>
              <td><div class="cell">安装路径</div></td>
              <td><div class="cell" v-if="data.javaInfo">{{ data.javaInfo.homeDir }}</div></td>
              <td><div class="cell">项目路径</div></td>
              <td><div class="cell" v-if="data.userInfo">{{ data.userInfo.userDir }}</div></td>
            </tr>
            <tr>
              <td><div class="cell">启动参数</div></td>
              <td colspan="3"><div class="cell" v-for="(item,index) in jvmStartArgs" :key="index">{{ item }}</div></td>
            </tr>
            </tbody>
          </table>
        </div>
      </el-card>
      <el-card class="box-card">
        <div slot="header">
          <span>磁盘状态</span>
        </div>
        <div class="el-table el-table--enable-row-hover el-table--medium">
          <table cellspacing="0" style="width: 100%;" >
            <thead>
            <tr >
              <th class="is-center"><div class="cell">盘符路径</div></th>
              <th class="is-center"><div class="cell">盘符描述</div></th>
              <th class="is-center"><div class="cell">盘符类型</div></th>
              <th class="is-center"><div class="cell">总大小</div></th>
              <th class="is-center"><div class="cell">可用大小</div></th>
              <th class="is-center"><div class="cell">已用大小</div></th>
              <th class="is-center"><div class="cell">已用百分比</div></th>
            </tr>
            </thead>
            <tbody v-if="data.diskInfoList">
              <tr v-for="diskInfo in data.diskInfoList" >
                <td class="is-center"><div class="cell">{{ diskInfo.fileMount }}</div></td>
                <td class="is-center"><div class="cell">{{ diskInfo.fileDesc }}</div></td>
                <td class="is-center"><div class="cell">{{ diskInfo.fileType }}</div></td>
                <td class="is-center"><div class="cell">{{ diskInfo.total }}</div></td>
                <td class="is-center"><div class="cell">{{ diskInfo.available }}</div></td>
                <td class="is-center"><div class="cell">{{ diskInfo.used }}</div></td>
                <td class="is-center"><div class="cell" :class="{'text-danger': diskInfo.usageRate > 80}">{{ diskInfo.usageRate }}%</div></td>
              </tr>
            </tbody>
          </table>
        </div>
      </el-card>

      <div>
        <el-row :gutter="6">
          <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" style="margin-bottom: 10px">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span style="font-weight: bold;color: #666;font-size: 15px">CPU使用率监控</span>
              </div>
              <div>
                <v-chart :options="cpuInfo" style="width:100%;height:400px" autoresize/>
              </div>
            </el-card>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" style="margin-bottom: 10px">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span style="font-weight: bold;color: #666;font-size: 15px">内存使用率监控</span>
              </div>
              <div>
                <v-chart :options="memoryInfo" style="width:100%;height:400px" autoresize/>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
  import ECharts from 'vue-echarts'
  import 'echarts/lib/chart/line'
  import 'echarts/lib/component/polar'
  import { getServerInfo } from '@/api/monitor/server'

  export default {
    name: 'ServerMonitor',
    components: {
      'v-chart': ECharts
    },
    data() {
      return {
        show: false,
        monitor: null,
        url: 'api/monitor',
        data: {},
        jvmStartArgs: [],
        cpuInfo: {
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: []
          },
          yAxis: {
            type: 'value',
            min: 0,
            max: 100,
            interval: 20
          },
          series: [{
            data: [],
            type: 'line',
            areaStyle: {
              normal: {
                color: 'rgb(32, 160, 255)' // 改变区域颜色
              }
            },
            itemStyle: {
              normal: {
                color: '#6fbae1',
                lineStyle: {
                  color: '#6fbae1' // 改变折线颜色
                }
              }
            }
          }]
        },
        memoryInfo: {
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: []
          },
          yAxis: {
            type: 'value',
            min: 0,
            max: 100,
            interval: 20
          },
          series: [{
            data: [],
            type: 'line',
            areaStyle: {
              normal: {
                color: 'rgb(32, 160, 255)' // 改变区域颜色
              }
            },
            itemStyle: {
              normal: {
                color: '#6fbae1',
                lineStyle: {
                  color: '#6fbae1' // 改变折线颜色
                }
              }
            }
          }]
        }
      }
    },
    created() {
      this.init()
      this.monitor = window.setInterval(() => {
        setTimeout(() => {
          this.init()
        }, 2)
      }, 5000)
    },
    destroyed() {
      clearInterval(this.monitor)
    },
    methods: {
      init() {
        getServerInfo().then(res => {
          this.data = res.data
          let jvmStartArgs = res.data.jvmInfo.startArgs
          jvmStartArgs = jvmStartArgs.slice(1).slice(0, -1)
          this.jvmStartArgs = jvmStartArgs.split(', ')
          this.show = true
          if (this.cpuInfo.xAxis.data.length >= 40) {
            this.cpuInfo.xAxis.data.shift()
            this.memoryInfo.xAxis.data.shift()
            this.cpuInfo.series[0].data.shift()
            this.memoryInfo.series[0].data.shift()
          }
          this.cpuInfo.xAxis.data.push(res.data.time)
          this.memoryInfo.xAxis.data.push(res.data.time)
          this.cpuInfo.series[0].data.push(parseFloat(res.data.cpuInfo.used))
          this.memoryInfo.series[0].data.push(parseFloat(res.data.memoryInfo.usageRate))
        })
      }
    }
  }
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  ::v-deep .box-card {
    margin-bottom: 5px;

    span {
      margin-right: 10px;
    }

    .el-icon-refresh {
      margin-right: 10px;
      float: right;
      cursor: pointer;
    }
  }

  .cpu, .memory, .swap, .disk {
    width: 20%;
    float: left;
    padding-bottom: 20px;
    margin-right: 5%;
  }

  .title {
    text-align: center;
    font-size: 15px;
    font-weight: 500;
    color: #999;
    margin-bottom: 16px;
  }

  .footer {
    text-align: center;
    font-size: 15px;
    font-weight: 500;
    color: #999;
    margin-top: -5px;
    margin-bottom: 10px;
  }

  .content {
    text-align: center;
    margin-top: 5px;
    margin-bottom: 5px;
  }
</style>
