<template>
  <div style="position: relative;">
    <el-form :model="queryParam" ref="queryForm" label-width="80px" size="mini" :inline="true" v-show="showSearch">
      <drag-widget-filter>
        <el-form-item label="流程状态">
          <el-select class="filter-item" v-model="processStatus" :clearable="true" placeholder="流程状态"
                     style="width: 250px">
            <el-option v-for="item in ProcessOrderStatus.getList()" :key="item.value" :label="item.label"
                       :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="创建日期">
          <el-date-picker class="filter-item" style="width: 250px" :clearable="true"
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
                   @click="onStartProcess()">新建
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
                             @viewWOrkOrder="onView"
                             @handlerWOrkOrder="onSubmit"
                             @cancelWOrkOrder="onCancelWorkOrder"
                             @handlerRemind="onRemindClick"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import { OnlinePageMixins } from './onlinePageMixins.js'
  import CustomTableWidget from '../designer/customTableWidget'
  import DragWidgetFilter from '../designer/dragWidgetFilter'

  import { selectMainProcessDefinition } from '@/api/process/definition'

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
      CustomTableWidget, DragWidgetFilter
    },
    data() {
      return {
        processDefinitionId: undefined,
        processName: undefined,
        createTime: [],
        processStatus: undefined,
        showSearch: true,
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {}
        }
      }
    },
    methods: {
      getTableQueryParam(widget) {

      },
      loadProcessOrderList(params) {

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
      /** 获取工单列表 **/
      getOrderList() {
      },
      /** 启动流程 **/
      onStartProcess() {
        if (this.isPreview || this.processDefinitionId === null) return
      },
      onSubmit(row) {

      },
      onView(row) {

      },
      onRemindClick(row) {

      },
      onCancelWorkOrder(row) {
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
          this.processDefinitionId = response.data.id
          this.processName = response.data.processName
        }
      )
    }
  }
</script>

<style>
</style>
