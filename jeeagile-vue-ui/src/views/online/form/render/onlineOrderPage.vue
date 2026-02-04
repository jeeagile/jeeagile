<template>
  <div style="position: relative;">
    <el-form ref="queryForm" label-width="80px" size="mini" :inline="true" v-show="showSearch">
      <drag-widget-filter>
        <el-form-item label="流程状态">
          <el-select class="filter-item" v-model="processOrderStatus" :clearable="true" placeholder="流程状态"
                     style="width: 250px">
            <el-option v-for="item in ProcessOrderStatus.getList()" :key="item.value" :label="item.label"
                       :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="创建日期">
          <el-date-picker class="filter-item" v-model="createTime" style="width: 250px" :clearable="true"
                          :allowTypes="['day']" align="left"
                          format="yyyy-MM-dd" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                          range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"/>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" :plain="true"
                     :disabled="processDefinitionId == null" @click="onSearch()">搜索
          </el-button>
          <el-button icon="el-icon-refresh" size="mini" :plain="true" :disabled="processDefinitionId == null">重置
          </el-button>
        </el-form-item>
      </drag-widget-filter>
    </el-form>


    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" :disabled="processDefinitionId == null"
                   @click="createOrder()">新建
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="onSearch"></right-toolbar>
    </el-row>

    <el-row>
      <el-col :span="24" v-if="pageConfig.pageQueryTable">
        <custom-table-widget :ref="pageConfig.pageQueryTable.variableName"
                             :widgetConfig="pageConfig.pageQueryTable"
                             :pageType="pageConfig.pageType"
                             :tableQueryParam="getTableQueryParam"
                             :loadTableDataFunc="loadProcessOrderList"
                             @viewProcessOrder="onView"
                             @handlerProcessOrder="onHandler"
                             @cancelProcessOrder="onCancelProcessOrder"
                             @handlerRemind="onRemindClick"
        />
      </el-col>
    </el-row>


    <el-dialog :title="orderDialogTitle" :visible.sync="openOrderDialog" width="650px" append-to-body>
      <online-flow-page v-if="openOrderDialog" :key="new Date().getTime()" ref="onlineOrder"
                        :page-id="processDefinition.pageId"/>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitOrder">提 交</el-button>
        <el-button @click="openOrderDialog = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import { OnlinePageMixins } from './onlinePageMixins.js'
  import CustomTableWidget from '../designer/customTableWidget'
  import DragWidgetFilter from '../designer/dragWidgetFilter'
  import OnlineFlowPage from './onlineFlowPage'
  import { selectMainProcessDefinition } from '@/api/process/definition'
  import { selectOnlineOrderList, startProcess, cancelProcessOrder } from '@/api/process/order'

  export default {
    name: 'OnlineOrderPage',
    props: {
      pageId: {
        type: String,
        required: true
      },
      processId: {
        type: String,
        required: true
      },
      isPreview: {
        type: Boolean,
        default: false
      }
    },
    mixins: [OnlinePageMixins],
    components: {
      CustomTableWidget, DragWidgetFilter, OnlineFlowPage
    },
    data() {
      return {
        processDefinitionId: undefined,
        processDefinition: {},
        processName: undefined,
        createTime: [],
        processOrderStatus: undefined,
        showSearch: true,
        orderDialogTitle: undefined,
        openOrderDialog: false
      }
    },
    methods: {
      getTableQueryParam(widget) {

      },

      loadProcessOrderList(params) {
        if (this.isPreview || this.processDefinitionId == null) return Promise.reject()
        return new Promise((resolve, reject) => {
          params.queryCond = {
            ...params.queryCond,
            processId: this.processId,
            orderPageId: this.pageId,
            instanceStatus: this.processOrderStatus,
            createTimeStart: Array.isArray(this.createTime) ? this.createTime[0] : undefined,
            createTimeEnd: Array.isArray(this.createTime) ? this.createTime[1] : undefined
          }
          selectOnlineOrderList(params).then(response => {
            resolve({
              dataList: response.data.records,
              pageTotal: response.data.pageTotal
            })
          })
        })
      },
      onSearch() {
        this.formPageData.pageFilterCopy = {
          ...this.formPageData.pageFilter
        }
        this.$refs[this.pageConfig.pageQueryTable.variableName].refresh()
      },
      onResume() {
        let key = this.$route.fullPath
        let cachePageData = this.onlinePageCache[key]
        if (cachePageData) {
          this.$nextTick(() => {
            if (Array.isArray(this.dropdownWidgetList)) {
              this.dropdownWidgetList.forEach(dropdownWidget => {
                let dropdownWidgetImpl = this.$refs[dropdownWidget.variableName][0]
                if (dropdownWidgetImpl) {
                  dropdownWidgetImpl.onVisibleChange()
                }
              })
            }
            this.formPageData.formFilter = cachePageData.pageFilter
            this.formPageData.formFilterCopy = cachePageData.pageFilterCopy
            this.$refs[this.pageConfig.pageQueryTable.variableName].setTableWidget(cachePageData.tableImpl)
            this.deleteOnlinePageCache(key)
          })
        }
      },
      /** 创建工单 **/
      createOrder() {
        if (this.isPreview || this.processDefinitionId === null) return
        this.orderDialogTitle = this.processDefinition.processName
        this.openOrderDialog = true
      },
      submitOrder() {
        this.$refs.onlineOrder.getFormPageData().then(orderData => {
          startProcess({ processDefinitionId: this.processDefinition.id, orderData: orderData }).then(response => {
              this.messageSuccess('流程工单发起成功')
              this.onSearch()
              this.openOrderDialog = false
            }
          )
        })
      },
      onView(row) {
        this.$router.push({ path: '/process/order/detail/' + row.orderId })
      },
      onHandler(row) {

      },
      onRemindClick(row) {

      },
      onCancelProcessOrder(row) {
        cancelProcessOrder(row.orderId).then(response => {
            this.messageSuccess('流程撤销成功！')
            this.onSearch()
          }
        )
      }
    },
    provide() {
      return {
        preview: () => this.isPreview
      }
    },
    computed: {
      ...mapGetters(['onlinePageCache'])
    },
    mounted() {
      selectMainProcessDefinition(this.processId).then(response => {
          this.processDefinition = response.data
          this.processDefinitionId = response.data.id
          this.processName = response.data.processName
        }
      )
    }
  }
</script>

<style>
</style>
