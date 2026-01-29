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
                                 :tableQueryParam="getTableQueryParam"
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
                                     :tableQueryParam="getTableQueryParam"
                                     @operationClick="clickTableOperation"
                />
                <custom-base-widget
                  v-else
                  :ref="subWidget.variableName"
                  :key="subWidget.id"
                  :widgetConfig="subWidget"
                  :pageConfig="pageConfig"
                  :dropdownParam="getDropdownParam"
                  v-model="formPageData[getWidgetFieldName(subWidget)]"
                />
              </template>
            </custom-base-widget>
          </template>
        </el-row>
      </el-form>
    </el-scrollbar>
  </div>
</template>

<script>
  import { OnlinePageMixins } from './onlinePageMixins.js'
  import CustomBaseWidget from '../designer/customBaseWidget'
  import CustomTableWidget from '../designer/customTableWidget'

  export default {
    name: 'OnlineFlowForm',
    inject: ['preview'],
    mixins: [OnlinePageMixins],
    props: {
      pageData: {
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
      /** 获取组件字段名 */
      getWidgetFieldName(widget) {
        if (widget && widget.onlineTable && widget.onlineColumn) {
          return this.getColumnFieldName(widget.onlineTable, widget.onlineColumn)
        }
      },
      /** 是否为添加从表数据 */
      checkAddRelationTable(tableWidget) {
        return this.operationType === this.OnlineOperationType.ADD && tableWidget.tableType != this.OnlineTableType.MASTER
      },
      /** 获取下拉参数 */
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
      /** 获取表单数据 */
      getFormPageData() {
        return new Promise((resolve, reject) => {
          this.$refs.form.validate(valid => {
            if (!valid) return
            let tableId = this.masterTable?.tableId
            let formData = { tableId }
            if (this.masterTable.tableType === this.OnlineTableType.MASTER) {
              let masterData = {}
              this.masterTable.tableColumnList.forEach(onlineColumn => {
                masterData[onlineColumn.columnName] = this.formPageData[onlineColumn.fieldName]
              })
              // 获取一对一表数据
              let slaveData = {}
              this.pageTableList.forEach(onlineTable => {
                if (onlineTable.tableType === this.OnlineTableType.ONE_TO_ONE) {
                  let tableData = {}
                  onlineTable.tableColumnList.forEach(onlineColumn => {
                    const fieldName = this.getColumnFieldName(onlineTable, onlineColumn)
                    tableData[onlineColumn.columnName] = this.formPageData[fieldName]
                  })
                  slaveData[onlineTable.tableId] = tableData
                }
              })
              formData.slaveData = slaveData
              formData.masterData = masterData
            }
            // 从表数据
            let slaveData = {}
            if (this.masterTable.tableType === this.OnlineTableType.MASTER) {
              if (Array.isArray(this.tableWidgetList) && this.tableWidgetList.length > 0) {
                this.tableWidgetList.forEach(tableWidget => {
                  let tableData = this.getRelationTableData(tableWidget)
                  if (tableData != null) {
                    slaveData[tableWidget.tableId] = tableData
                  }
                })
              }
            } else {
              formData = this.masterTable.tableColumnList.reduce((retObj, column) => {
                const fieldName = this.getColumnFieldName(this.masterTable, column)
                retObj[column.columnName] = this.formPageData[fieldName]
                return retObj
              }, {})
              formData = {
                ...slaveData,
                ...this.formData
              }
            }
            formData.slaveData = {
              ...slaveData,
              ...formData.slaveData
            }
            resolve(formData)
          })
        })
      },
      /** 点击表格按钮 */
      clickTableOperation(operation, row, widget) {
        this.handlerOperation(operation, row, widget)
      }
    },
    computed: {
      pageReadOnly() {
        return this.readOnly === 'true' || this.readOnly
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
