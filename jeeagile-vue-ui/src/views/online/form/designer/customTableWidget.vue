<template>
  <el-col :span="widgetConfig.span">
    <el-row :style="{'margin-bottom': widgetConfig.supportBottom === 1 ? '20px' : undefined}">
      <el-col :span="24" style="margin-bottom: 10px;"
              v-if="pageType !== OnlinePageType.QUERY && pageType !== OnlinePageType.ORDER">
        <el-row type="flex" justify="space-between">
          <div class="table-title" :style="{'border-left': '3px solid ' + widgetConfig.titleColor}">
            {{widgetConfig.showName}}
          </div>
          <div>
            <el-button size="mini"
                       v-for="operation in getTableOperation(false)" :key="operation.id"
                       :icon="operation.icon"
                       :plain="operation.plain"
                       :type="operation.btnType"
                       @click.stop="onOperationClick(operation)">
              {{operation.name}}
            </el-button>
          </div>
        </el-row>
      </el-col>
      <el-col :span="24">
        <el-table size="mini" header-cell-class-name="table-header-gray" ref="tableImpl"
                  :style="{height: (widgetConfig.height != null && widgetConfig.height !== '') ? widgetConfig.height + 'px' : undefined}"
                  :height="(widgetConfig.height != null && widgetConfig.height !== '') ? widgetConfig.height + 'px' : undefined"
                  :data="tableWidget.dataList" :row-key="primaryFieldName"
                  @sort-change="tableWidget.onSortChange">
          <el-table-column label="序号" header-align="center" align="center" type="index" width="55px"
                           :index="tableWidget.getTableIndex"/>
          <template v-for="tableColumn in widgetConfig.tableColumnList">
            <!-- Boolean类型的字段，使用el-tag去显示 -->
            <el-table-column v-if="(tableColumn.onlineColumn || {}).fieldType === 'Boolean'"
                             :key="tableColumn.dataFieldName"
                             :label="tableColumn.showName" :width="tableColumn.columnWidth + 'px'"
                             :prop="tableColumn.onlineColumn.fieldName"
                             :sortable="tableColumn.sortable ? 'custom' : false"
                             :align="tableColumn.columnAlign"
            >
              <template slot-scope="scope">
                <el-tag size="mini" :type="scope.row[tableColumn.dataFieldName] ? 'success' : 'danger'">
                  {{scope.row[tableColumn.dataFieldName] ? '是' : '否'}}
                </el-tag>
              </template>
            </el-table-column>
            <!-- 普通非字典字段 -->
            <template v-else-if="(tableColumn.onlineColumn || {}).onlineDict == null">
              <el-table-column :label="tableColumn.showName"
                               v-if="tableColumn.onlineColumn &&
                  (tableColumn.onlineColumn.fieldKind === OnlineFieldKind.UPLOAD ||
                  tableColumn.onlineColumn.fieldKind === OnlineFieldKind.UPLOAD_IMAGE)"
                               :key="(tableColumn.onlineColumn || {}).fieldName"
                               :width="tableColumn.columnWidth + 'px'"
                               :align="tableColumn.columnAlign"
              >
                <template slot-scope="scope">
                  <template v-if="tableColumn.onlineColumn.fieldKind === OnlineFieldKind.UPLOAD_IMAGE">
                    <el-image
                      v-for="item in parseTableUploadData(tableColumn, scope.row)"
                      :preview-src-list="getTablePictureList(tableColumn, scope.row)"
                      class="table-cell-image" :key="item.url" :src="item.url" fit="fill">
                      <div slot="error" class="table-cell-image">
                        <i class="el-icon-picture-outline"></i>
                      </div>
                    </el-image>
                  </template>
                  <template v-else>
                    <a v-for="item in parseTableUploadData(tableColumn, scope.row)"
                       :key="item.url" href="javascript:void(0);" @click="downloadFile(item.url, item.name)">
                      {{item.name}}
                    </a>
                  </template>
                </template>
              </el-table-column>
              <el-table-column v-else :key="(tableColumn.onlineColumn || {}).fieldName"
                               :label="tableColumn.showName" :prop="tableColumn.dataFieldName"
                               :width="tableColumn.columnWidth + 'px'"
                               :sortable="tableColumn.sortable ? 'custom' : false"
                               :align="tableColumn.columnAlign"
              />
            </template>
            <!-- 字典字段 -->
            <el-table-column v-else :key="(tableColumn.onlineColumn || {}).fieldName"
                             :label="tableColumn.showName" :width="tableColumn.columnWidth + 'px'"
                             :prop="tableColumn.onlineColumn.fieldName"
                             :sortable="tableColumn.sortable ? 'custom' : false"
                             :align="tableColumn.columnAlign"
            >
              <template slot-scope="scope">
                <span>
                  {{getDictValue(tableColumn, scope.row)}}
                </span>
              </template>
            </el-table-column>
          </template>
          <el-table-column v-if="pageType === OnlinePageType.ORDER" label="当前任务"
                           prop="(runtimeTaskInfo || {}).taskName"/>
          <el-table-column v-if="pageType === OnlinePageType.ORDER" label="流程创建时间" width="180px" prop="createTime"/>
          <el-table-column v-if="pageType === OnlinePageType.ORDER" label="流程状态" width="100px" prop="flowStatus"/>
          <el-table-column
            v-if="getTableOperation(true).length > 0 || pageType === OnlinePageType.ORDER"
            label="操作" :width="(widgetConfig.operationWidth || 150) + 'px'" fixed="right" align="center"
          >
            <template slot-scope="scope">
              <el-button v-for="operation in getTableOperation(true)" :key="operation.id"
                         :class="operation.btnClass"
                         :icon="operation.icon"
                         type="text" size="mini"
                         @click.stop="onOperationClick(operation, scope.row)"
              >
                {{operation.name}}
              </el-button>
              <el-button type="text" size="mini"
                         v-if="pageType === OnlinePageType.ORDER && (scope.row.initTaskInfo || {}).taskKey !== (scope.row.runtimeTaskInfo || {}).taskKey"
                         @click.stop="onViewWorkOrder(scope.row)">
                详情
              </el-button>
              <el-button type="text" size="mini"
                         v-if="pageType === OnlinePageType.ORDER && (scope.row.initTaskInfo || {}).taskKey === (scope.row.runtimeTaskInfo || {}).taskKey"
                         @click.stop="onHandlerWorkOrder(scope.row)">
                办理
              </el-button>
              <el-button type="text" size="mini"
                         v-if="pageType === OnlinePageType.ORDER"
                         @click.stop="onHandlerRemindClick(scope.row)">
                催办
              </el-button>
              <el-button type="text" size="mini" class="table-btn error"
                         v-if="pageType === OnlinePageType.ORDER"
                         @click.stop="onCancelWorkOrder(scope.row)">
                撤销
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span="24" v-if="widgetConfig.pageFlag && !this.isNew">
        <el-row type="flex" justify="end" style="margin-top: 10px;">
          <el-pagination
            :background="true"
            :total="tableWidget.totalCount"
            :current-page="tableWidget.currentPage"
            :page-size="tableWidget.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @current-change="tableWidget.onCurrentPageChange"
            @size-change="tableWidget.onPageSizeChange">
          </el-pagination>
        </el-row>
      </el-col>
    </el-row>
  </el-col>
</template>

<script>
  import { TableWidget } from '../util/widget'

  export default {
    props: {
      pageType: {
        type: String,
        required: true
      },
      widgetConfig: {
        type: Object,
        required: true
      },
      getTableQueryParams: {
        type: Function
      },
      loadTableDataFunc: {
        type: Function
      },
      flowData: {
        type: Object
      },
      isNew: {
        type: Boolean,
        default: false
      }
    },
    inject: ['preview'],
    data() {
      return {
        // 表格用到的字典数据
        tableDictValueListMap: new Map(),
        tableWidget: new TableWidget(
          this.loadTableData,
          this.loadTableVerify,
          this.widgetConfig.pageFlag && !this.isNew,
          false,
          this.widgetConfig.orderFieldName,
          this.widgetConfig.ascending
        )
      }
    },
    methods: {
      onViewWorkOrder(row) {
        this.$emit('viewWOrkOrder', row, this.widgetConfig)
      },
      onHandlerWorkOrder(row) {
        this.$emit('handlerWOrkOrder', row, this.widgetConfig)
      },
      onHandlerRemindClick(row) {
        this.$emit('handlerRemind', row, this.widgetConfig)
      },
      onCancelWorkOrder(row) {
        this.$emit('cancelWOrkOrder', row, this.widgetConfig)
      },
      getTableWidget() {
        return this.tableWidget
      },
      setTableWidget(tableWidget) {
        let timer = setInterval(() => {
          if (!this.tableWidget.loading) {
            this.tableWidget.totalCount = tableWidget.totalCount
            this.tableWidget.currentPage = tableWidget.currentPage
            this.tableWidget.pageSize = tableWidget.pageSize
            this.tableWidget.refreshTable()
            clearInterval(timer)
          }
        }, 30)
      },
      /** 点击操作按钮 */
      onOperationClick(operation, row) {
        this.$emit('operationClick', operation, row, this.widgetConfig)
      },
      /** 加载表数据 */
      loadTableData(params) {
      },
      loadTableVerify() {
        return true
      },
      /** 刷新表格数据 */
      refresh(row, operatorType) {
      },
      /** 获取表操作按钮 */
      getTableOperation(rowOperation) {
        if (this.widgetConfig.readOnly) return []
        let tempList = this.widgetConfig.operationList.filter(operation => {
          return operation.rowOperation === rowOperation && operation.enabled
        })
        if (rowOperation) {
          let customOperation = []
          return customOperation.concat(tempList.filter(item => {
            if (item.type === this.OnlineOperationType.CUSTOM) customOperation.push(item)
            return item.type !== this.OnlineOperationType.CUSTOM
          }))
        }
        return tempList
      }
    },
    mounted() {

    },
    computed: {
      primaryFieldName() {
        if (this.widgetConfig && this.widgetConfig.onlineTable && Array.isArray(this.widgetConfig.onlineTable.tableColumnList)) {
          for (let i = 0; i < this.widgetConfig.onlineTable.tableColumnList.length; i++) {
            let onlineColumn = this.widgetConfig.onlineTable.tableColumnList[i]
            if (onlineColumn.primaryFlag === this.SysYesNo.YES) {
              return (this.widgetConfig.tableType === this.OnlineTableType.MASTER ? this.widgetConfig.variableName + '__' : '') + onlineColumn.fieldName
            }
          }
        }
        return null
      }
    }
  }
</script>

<style scoped>
  .table-title {
    padding: 0px 5px;
    height: 22px;
    line-height: 22px;
    margin: 3px 0px;
  }
</style>
