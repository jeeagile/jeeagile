<template>
  <div style="position: relative;">
    <el-scrollbar class="custom-scroll" :style="{height: pageConfig.height + 'px', 'min-height': '100px'}">
      <el-form ref="form" :model="formPageData" :rules="rules"
               :label-width="pageConfig.labelWidth + 'px'" size="mini"
               :label-position="pageConfig.labelPosition"
               @submit.native.prevent>
        <el-row :gutter="pageConfig.gutter" style="margin: 0px">
          <template v-for="widget in pageConfig.pageWidgetList">
            <custom-table-widget :ref="widget.variableName"
                                 v-if="widget.widgetType === OnlineWidgetType.Table" :key="widget.id"
                                 :widgetConfig="widget"
                                 :pageType="pageConfig.pageType"
                                 :primaryColumnName="widget.primaryColumnName"
                                 :isNew="checkAddRelationTable(widget)"
                                 :queryParam="getTableQueryParam"
                                 @operationClick="clickTableOperation"
            />
            <custom-base-widget
              v-else
              :ref="widget.variableName"
              :key="widget.id"
              :widgetConfig="widget"
              :pageConfig="pageConfig"
              :dropdownParam="getDropdownParam"
              v-model="formPageData[getWidgetFieldName(widget)]"
            >
              <template v-for="subWidget in widget.childWidgetList">
                <custom-table-widget :ref="subWidget.variableName"
                                     v-if="subWidget.widgetType === OnlineWidgetType.Table" :key="subWidget.id"
                                     :widgetConfig="subWidget"
                                     :pageType="pageConfig.pageType"
                                     :isNew="checkAddRelationTable(subWidget)"
                                     :queryParam="getTableQueryParam"
                                     @operationClick="clickTableOperation"
                />
                <custom-base-widget
                  v-else
                  :ref="subWidget.variableName"
                  :key="subWidget.id"
                  :widgetConfig="subWidget"
                  :pageConfig="pageConfig"
                  :dropdownParams="getDropdownParam"
                  v-model="formPageData[getWidgetFieldName(subWidget)]"
                />
              </template>
            </custom-base-widget>
          </template>
        </el-row>
      </el-form>
    </el-scrollbar>
    <el-row v-if="pageConfig.pageType === OnlinePageType.EDIT" type="flex" justify="end" style="margin-top: 15px;">
      <el-button type="primary" size="mini" :plain="true"
                 v-if="pageConfig.pageKind === OnlinePageKind.DIALOG"
                 @click="onCancel(false)">
        取消
      </el-button>
      <el-button type="primary" size="mini"
                 @click="onSave()">
        保存
      </el-button>
    </el-row>
  </div>
</template>

<script>
  import { OnlinePageMixins } from './onlinePageMixins.js'
  import CustomBaseWidget from '../designer/customBaseWidget'
  import CustomTableWidget from '../designer/customTableWidget'

  export default {
    name: 'OnlineEditForm',
    inject: ['preview'],
    mixins: [OnlinePageMixins],
    props: {
      operationType: {
        type: [Number, String],
        required: true
      },
      saveOnClose: {
        type: String,
        default: '1'
      },
      rowData: {
        type: Object
      },
      flowData: {
        type: Object
      }
    },
    components: {
      CustomBaseWidget,
      CustomTableWidget
    },
    methods: {
      getWidgetFieldName(widget) {
        if (widget && widget.tableType === this.OnlineTableType.MASTER) {
          return (widget.onlineColumn || {}).columnName
        } else {
          return widget.variableName + '__' + (widget.onlineColumn || {}).columnName
        }
      },
      checkAddRelationTable(tableWidget) {
        return this.operationType === this.OnlineOperationType.ADD && tableWidget.tableType != this.OnlineTableType.MASTER
      },
      onCancel(isSuccess, data) {
        this.$emit('close', isSuccess, data)
      },
      onSave() {
        if (this.preview()) return
      },
      getTableQueryParam(widget) {
        let queryParams = []
        if (Array.isArray(widget.queryParamList)) {
          queryParams = widget.queryParamList.map(item => {
            let paramValue = this.getParamValue(item.paramValueType, item.paramValue)
            if (paramValue == null || paramValue === '' || (Array.isArray(paramValue) && paramValue.length === 0)) return
            let temp = {
              tableName: item.onlineTable.tableName,
              columnName: item.onlineColumn.columnName,
              filterType: item.onlineColumn.filterType,
              columnValue: item.onlineColumn.filterType !== this.OnlineFilterType.RANFGE ? paramValue : undefined
            }
            if (item.onlineColumn.filterType === this.OnlineFilterType.RANFGE) {
              temp.columnValueStart = paramValue[0]
              temp.columnValueEnd = paramValue[1]
            }
            return temp
          }).filter(item => item != null)
        }

        return queryParams
      },
      getDropdownParam(widget) {
        if (Array.isArray(widget.dictParamList)) {
          let params = {}
          for (let i = 0; i < widget.dictParamList.length; i++) {
            let dictParam = widget.dictParamList[i]
            if (dictParam.dictValue == null || dictParam.dictValueType == null) continue
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
      }
    }
  }
</script>

<style lang='scss'>
  .custom-scroll {
    .el-scrollbar__wrap {
      overflow-x: hidden !important;
    }

    .el-scrollbar__view {
      overflow-x: hidden !important;
    }
  }
</style>
