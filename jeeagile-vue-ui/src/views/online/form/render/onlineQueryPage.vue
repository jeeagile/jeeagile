<template>
  <div style="position: relative;">
    <el-form :label-width="pageConfig.labelWidth + 'px'" size="mini" :label-position="pageConfig.labelPosition"
             @submit.native.prevent>
      <drag-widget-filter v-if="!loading">
        <el-col v-for="widget in pageConfig.pageWidgetList"
                :span="widget.span" :key="widget.id"
                :style="{width: widget.labelWidth + 200 + 'px'}">
          <custom-filter-widget :key="widget.id"
                                :ref="widget.variableName"
                                :widgetConfig="widget"
                                :pageConfig="pageConfig"
                                :dropdownParam="getDropdownParam"
                                v-model="formPageData.pageFilter[widget.onlineColumn.columnName]"
          />
        </el-col>
        <div slot="operator" style="padding: 13px 10px;" v-for="operation in getTableOperation(false)"
             :key="operation.id">
          <el-button size="mini" :icon="operation.icon" :plain="operation.plain"
                     :key="operation.id"
                     :type="operation.btnType"
                     :v-hasPerm="getOperationPermCode(pageConfig.pageQueryTable, operation)"
                     @click.stop="clickTableOperation(operation, null, pageConfig.pageQueryTable)">
            {{operation.name}}
          </el-button>
        </div>
        <div slot="search" style="padding: 13px 10px;"
             v-if="Array.isArray(pageConfig.pageWidgetList) &&  pageConfig.pageWidgetList.length > 0">
          <el-button type="cyan" icon="el-icon-search" size="mini" :plain="true" @click="onSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" :plain="true">重置</el-button>
        </div>
      </drag-widget-filter>
    </el-form>
    <el-row>
      <el-col :span="24" v-if="pageConfig.pageQueryTable">
        <custom-table-widget :ref="pageConfig.pageQueryTable.variableName"
                             :widgetConfig="pageConfig.pageQueryTable"
                             :pageType="pageConfig.pageType"
                             :queryParam="getTableQueryParam"
                             @operationClick="clickTableOperation"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { mapGetters, mapMutations } from 'vuex'
  import { OnlinePageMixins } from './onlinePageMixins'
  import DragWidgetFilter from '../designer/dragWidgetFilter'
  import CustomFilterWidget from '../designer/customFilterWidget'
  import CustomTableWidget from '../designer/customTableWidget'

  export default {
    name: 'OnlineQueryPage',
    mixins: [OnlinePageMixins],
    props: {
      pageId: {
        type: String,
        required: true
      },
      closeVisible: {
        type: String,
        default: '0'
      }
    },
    components: {
      DragWidgetFilter,
      CustomFilterWidget,
      CustomTableWidget
    },
    inject: ['preview'],
    methods: {
      getTableOperation(rowOperation) {
        if (this.pageConfig.pageQueryTable == null || !Array.isArray(this.pageConfig.pageQueryTable.operationList)) return []
        return this.pageConfig.pageQueryTable.operationList.filter(operation => {
          return operation.rowOperation === rowOperation && operation.enabled
        })
      },
      getTableQueryParam(widget) {
        let queryParams = []
        if (Array.isArray(widget.queryParamList)) {
          queryParams = widget.queryParamList.map(item => {
            let paramValue = this.getParamValue(item.paramValueType, item.paramValue)
            if (!paramValue || (Array.isArray(paramValue) && paramValue.length === 0)) return
            let temp = {
              tableName: item.onlineTable.tableName,
              columnName: item.onlineColumn.columnName,
              filterType: item.onlineColumn.filterType,
              columnValue: item.onlineColumn.filterType !== this.OnlineFilterType.RANFGE ? paramValue : undefined
            }

            if (item.column.filterType === this.OnlineFilterType.RANFGE) {
              temp.columnValueStart = paramValue[0]
              temp.columnValueEnd = paramValue[1]
            }

            return temp
          }).filter(item => item != null)
        }

        return queryParams
      },
      onSearch() {
        this.formPageData.pageFilterCopy = {
          ...this.formPageData.pageFilter
        }
        this.$refs[this.pageConfig.pageQueryTable.variableName].refresh()
      },
      getDropdownParam(widget) {
        if (Array.isArray(widget.dictParamList)) {
          let params = {}
          for (let i = 0; i < widget.dictParamList.length; i++) {
            let dictParam = widget.dictParamList[i]
            params = this.getParamValueObj(dictParam.dictParamName, dictParam.dictValueType, dictParam.dictValue, params)
            if (params == null) return null
          }
          return params
        } else {
          return {}
        }
      },
      clickTableOperation(operation, row, widget) {
        this.handlerOperation(operation, row, widget)
      },
      onResume() {
        let key = this.$route.fullPath
        let cachePageFormData = this.getOnlineFormCache[key]
        if (cachePageFormData) {
          this.$nextTick(() => {
            if (Array.isArray(this.dropdownWidgetList)) {
              this.dropdownWidgetList.forEach(dropdownWidget => {
                let dropdownWidgetImpl = this.$refs[dropdownWidget.variableName][0]
                if (dropdownWidgetImpl) {
                  dropdownWidgetImpl.onVisibleChange()
                }
              })
            }
            this.formPageData.pageFilter = cachePageFormData.pageFilter
            this.formPageData.pageFilterCopy = cachePageFormData.pageFilterCopy
            this.$refs[this.pageConfig.pageQueryTable.variableName].setTableWidget(cachePageFormData.tableImpl)
            this.deleteOnlinePageCache(key)
          })
        }
      },
      ...mapMutations(['deleteOnlinePageCache'])
    },
    computed: {
      ...mapGetters(['onlinePageCache'])
    }
  }
</script>
