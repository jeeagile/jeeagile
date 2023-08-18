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
  import { saveTableData, updateTableData } from '@/api/online/operation'

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
      /** 取消 */
      onCancel(isSuccess, data) {
        this.$emit('close', isSuccess, data)
      },
      /** 保存数据 */
      onSave() {
        if (this.preview()) return
        setTimeout(() => {
          this.$refs.form.validate(valid => {
            if (!valid) return
            let tableId = this.masterTable?.tableId
            let params = { tableId }
            if (this.masterTable.tableType === this.OnlineTableType.MASTER) {
              let masterData = {}
              this.masterTable.tableColumnList.forEach(onlineColumn => {
                masterData[onlineColumn.columnName] = this.formPageData[onlineColumn.fieldName]
              })
              params.masterData = masterData
            }

            // 添加调用按钮接口代码
            if (this.saveOnClose === '1') {
              if (tableId != null && tableId !== '') {
                // 从表数据
                let slaveData = {}
                if (this.masterTable.tableType === this.OnlineTableType.MASTER) {
                  if (Array.isArray(this.tableWidgetList) && this.tableWidgetList.length > 0 && this.operationType === this.OnlineOperationType.ADD) {
                    this.tableWidgetList.forEach(tableWidget => {
                      let tableData = this.getRelationTableData(tableWidget)
                      if (tableData != null) {
                        slaveData[tableWidget.tableId] = tableData
                      }
                    })
                  }
                } else {
                  slaveData = this.masterTable.tableColumnList.reduce((retObj, column) => {
                    const fieldName = this.getColumnFieldName(this.masterTable, column)
                    retObj[column.columnName] = this.formPageData[fieldName]
                    return retObj
                  }, {})
                  slaveData = {
                    ...slaveData,
                    ...this.params
                  }
                }
                params.slaveData = slaveData
                if (this.operationType === 'ADD') {
                  saveTableData(params).then(res => {
                    this.onCancel(true, this.formPageData)
                  })
                } else {
                  if (this.masterTable.tableType === this.OnlineTableType.MASTER) {
                    params.tableData = params.masterData
                  } else {
                    params.tableData = params.slaveData
                  }
                  params.masterData = undefined
                  params.slaveData = undefined
                  updateTableData(params).then(res => {
                    this.onCancel(true, this.formPageData)
                  })
                }
              }
            } else {
              let masterData = {
                // 级联添加数据唯一标识
                __cascade_add_id__: this.formPageData.__cascade_add_id__ || new Date().getTime()
              }
              if (this.masterTable && Array.isArray(this.masterTable.tableColumnList)) {
                this.masterTable.tableColumnList.forEach(onlineColumn => {
                  let fieldName = this.getColumnFieldName(this.masterTable, onlineColumn)
                  masterData[fieldName] = this.formPageData[fieldName]
                })

                if (Array.isArray(this.dropdownWidgetList)) {
                  this.dropdownWidgetList.forEach(dropdownWidget => {
                    let fieldName = this.getColumnFieldName(this.masterTable, dropdownWidget.onlineColumn)
                    let tempWidget = this.$refs[dropdownWidget.variableName]
                    if (Array.isArray(tempWidget)) {
                      tempWidget.forEach(item => {
                        masterData[dropdownWidget.onlineColumn.columnName + '__DictMap'] = {
                          dictValue: this.formPageData[fieldName],
                          dictLabel: item.getDictLabel(this.formPageData[fieldName])
                        }
                      })
                    }
                  })
                }
              }
              this.onCancel(true, masterData)
            }
          })
        }, 30)
      },
      /** 获取表格查询参数 */
      getTableQueryParam(widget) {
        let queryParams = []
        if (Array.isArray(widget.queryParamList)) {
          queryParams = widget.queryParamList.map(item => {
            let paramValue = this.getParamValue(item.paramValueType, item.paramValue)
            if (paramValue == null || paramValue === '' || (Array.isArray(paramValue) && paramValue.length === 0)) return
            let temp = {
              tableId: item.onlineTable.tableId,
              tableName: item.onlineTable.tableName,
              columnId: item.onlineColumn.columnId,
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
        // 从表添加关联字段参数 后端校验关联参数是否存在，避免全表查询
        if (widget.onlineTable.tableType === this.OnlineTableType.ONE_TO_MANY) {
          const masterOnlineTable = this.onlineTableMap.get(widget.onlineTable.masterTableId)
          const masterOnlineColumn = this.onlineColumnMap.get(widget.onlineTable.masterColumnId)
          const fieldName = this.getColumnFieldName(masterOnlineTable, masterOnlineColumn)
          const temp = {
            tableId: widget.onlineTable.tableId,
            tableName: widget.onlineTable.tableName,
            columnId: widget.onlineTable.slaveColumnId,
            columnName: widget.onlineTable.slaveColumnName,
            filterType: this.OnlineFilterType.EQUAL,
            columnValue: this.rowData[fieldName]
          }
          queryParams.push(temp)
        }
        return queryParams
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
      /** 点击表格按钮 */
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
