import { mapMutations } from 'vuex'
import { formPageRender } from '@/api/online/form'
import * as SystemStaticDict from '@/components/AgileDict/system'

const OnlinePageMixins = {
  props: {
    pageId: {
      type: String,
      required: true
    },
    readOnly: {
      type: Boolean,
      default: false
    },
    closeVisible: {
      type: String,
      default: '0'
    },
    params: {
      type: Object
    }
  },
  data() {
    return {
      loading: true,
      formPageData: {},
      rules: {},
      pageConfig: {
        formType: undefined,
        formKind: undefined,
        gutter: 20,
        labelPosition: 'right',
        labelWidth: 120,
        width: undefined,
        height: undefined,
        pageWidgetList: [],
        pageQueryTable: undefined
      },
      masterTable: undefined,
      errorMessage: [],
      tableWidgetList: [],
      dropdownWidgetList: [],
      richEditWidgetList: [],
      pageTableList: [],
      onlineTableMap: new Map(),
      onlineColumnMap: new Map(),
      onlineDictMap: new Map(),
      linkageMap: new Map()
    }
  },
  methods: {
    /** 获取表单页面数据 */
    getFormPageData() {
      return this.formPageData
    },
    /** 获取操作按钮权限字符串 */
    getOperationPermCode(widget, operation) {
      const modelName = widget.onlineTable.modelName
      let operationStr = 'view'
      switch(operation.type){
        case this.OnlineOperationType.ADD:
          operationStr = 'add'
          break
        case this.OnlineOperationType.EDIT:
          operationStr = 'edit'
          break
        case this.OnlineOperationType.DELETE:
          operationStr = 'delete'
          break
        default:
          operationStr = 'view'
      }
      return 'online:' + modelName + ':' + operationStr
    },
    loadOnlinePageConfig() {
      return new Promise((resolve, reject) => {
        formPageRender({ pageId: this.pageId }).then(response => {
          let onlinePage = response.data.onlinePage
          let pageConfigData = JSON.parse(onlinePage.widgetJson)
          this.pageTableList = response.data.pageTableList
          let pageConfig = {
            pageName: onlinePage.pageName,
            pageType: onlinePage.pageType,
            pageKind: onlinePage.pageKind,
            tableId: onlinePage.tableId,
            labelWidth: pageConfigData.pageConfig.labelWidth,
            labelPosition: pageConfigData.pageConfig.labelPosition,
            gutter: pageConfigData.pageConfig.gutter,
            height: pageConfigData.pageConfig.height,
            width: pageConfigData.pageConfig.width,
            pageWidgetList: pageConfigData.widgetList,
            pageQueryTable: pageConfigData.pageConfig.tableWidget
          }
          if (Array.isArray(this.pageTableList)) {
            this.pageTableList.forEach(table => {
              this.onlineTableMap.set(table.tableId, table)
              table.tableColumnList.forEach(column => {
                this.onlineColumnMap.set(column.columnId, column)
              })
            })
          }
          this.masterTable = this.onlineTableMap.get(pageConfig.tableId)
          this.initFormPageData(pageConfig)
          this.initFormPageWidget(pageConfig)
          this.pageConfig = pageConfig
          resolve()
        }).catch(e => {
          reject(e)
        })
      })
    },
    initFormPageData(pageConfig) {
      function handlePageDataByColumn(retObj, column, table) {
        let fieldName = (table.tableType != '01' ? table.modelName + '__' : '') + column.columnName
        if (retObj == null) retObj = {}
        if (pageConfig.pageType === '01') {
          if (retObj.pageFilter == null) retObj.pageFilter = {}
          if (retObj.pageFilterCopy == null) retObj.pageFilterCopy = {}
          retObj.pageFilter[fieldName] = column.fieldType === 'Boolean' ? false : undefined
          retObj.pageFilterCopy[fieldName] = column.fieldType === 'Boolean' ? false : undefined
        } else {
          retObj[fieldName] = column.fieldType === 'Boolean' ? false : undefined
        }
        return retObj
      }

      // 设置数据表数据
      let formPageData = {}
      if (this.masterTable) {
        // 添加表单主表的数据
        this.masterTable.tableColumnList.forEach(column => {
          formPageData = handlePageDataByColumn(formPageData, column, this.masterTable)
        })
        // 如果表单主表是数据源主表，添加一对一关联从表数据
        if (this.masterTable.tableType == this.OnlineTableType.MASTER) {
          this.pageTableList.forEach(pageTable => {
            if (pageTable.tableType === this.OnlineTableType.ONE_TO_ONE) {
              pageTable.tableColumnList.forEach(column => {
                formPageData = handlePageDataByColumn(formPageData, column, pageTable)
              })
            }
          })
        }
      }
      this.$set(this, 'formPageData', formPageData)
    },
    /** 初始化表单页面组件 */
    initFormPageWidget(pageConfig) {
      this.errorMessage = []
      this.initWidget(pageConfig.pageQueryTable, pageConfig)
      if (Array.isArray(pageConfig.pageWidgetList)) {
        pageConfig.pageWidgetList.forEach(widget => {
          if (pageConfig.pageType === this.OnlinePageType.FLOW && this.pageReadOnly) {
            widget.readOnly = true
          }
          this.initWidget(widget, pageConfig)
        })
      }
      if (this.errorMessage.length > 0) {
        console.error(this.errorMessage)
      }
      this.initWidgetRule(pageConfig)
    },
    /** 初始化组件 */
    initWidget(widget, pageConfig) {
      if (widget != null) {
        if (widget.tableId) widget.onlineTable = this.onlineTableMap.get(widget.tableId)
        if (widget.columnId) widget.onlineColumn = this.onlineColumnMap.get(widget.columnId)
        if (widget.widgetType === this.OnlineWidgetType.RichEditor) {
          this.richEditWidgetList.push(widget)
        }
        // 初始化静态静态字典
        if (Array.isArray(widget.dictParamList)) {
          widget.dictParamList.forEach(param => {
            if (param.dictValueType === this.OnlineParamValueType.STATIC_DICT) {
              let errorItem = null
              if (Array.isArray(param.dictValue) && param.dictValue.length === 2) {
                let systemStaticDict = SystemStaticDict[param.dictValue[0]]
                if (systemStaticDict == null) {
                  errorItem = {
                    widget: widget,
                    message: '组件字典参数' + param.dictParamName + '绑定的静态字典 [' + param.dictValue[0] + '] 并不存在！'
                  }
                } else {
                  if (systemStaticDict.getValue(param.dictValue[1]) == null) {
                    errorItem = {
                      widget: widget,
                      message: '组件字典参数' + param.dictParamName + '绑定的静态字典值并不属于静态字段 [' + param.dictValue[0] + '] ！'
                    }
                  }
                }
              } else {
                errorItem = {
                  widget: widget,
                  message: '组件字典参数' + param.dictParamName + '绑定的静态字典错误！'
                }
              }
              if (errorItem != null) this.errorMessage.push(errorItem)
            }
          })
        }
        if (widget.onlineColumn?.onlineDict) {
          this.dropdownWidgetList.push(widget)
        }
        // 初始化表格组件
        if (widget.widgetType === this.OnlineWidgetType.Table) {
          widget.primaryColumnName = undefined
          if (widget.onlineTable && Array.isArray(widget.onlineTable.tableColumnList)) {
            for (let i = 0; i < widget.onlineTable.tableColumnList.length; i++) {
              // eslint-disable-next-line max-depth
              if (widget.onlineTable.tableColumnList[i].primaryFlag === this.SysYesNo.YES) {
                widget.primaryColumnName = widget.onlineTable.tableColumnList[i].columnName
                break
              }
            }
          }
          if (Array.isArray(widget.tableColumnList)) {
            widget.tableColumnList.forEach(tableColumn => {
              tableColumn.onlieTable = this.onlineTableMap.get(tableColumn.tableId)
              tableColumn.onlinColumn = this.onlineColumnMap.get(tableColumn.columnId)
              if (tableColumn.onlieTable == null || tableColumn.onlinColumn == null) {
                this.errorMessage.push({
                  widget: widget,
                  message: '表格列 [' + tableColumn.showName + '] 绑定的字段不存在！'
                })
              }
            })
          }
          if (Array.isArray(widget.queryParamList)) {
            widget.queryParamList.forEach(param => {
              param.onlieTable = this.onlineTableMap.get(param.tableId)
              param.onlinColumn = this.onlineColumnMap.get(param.columnId)
              if (param.onlieTable == null || param.onlinColumn == null) {
                this.errorMessage.push({
                  widget: widget,
                  message: '表格查询参数不存在！'
                })
              }
            })
          }

          this.tableWidgetList.push(widget)
        }

        // 校验组件 数据表是否存在
        if (widget.onlieTable) {
          let errorItem = {
            widget: widget,
            message: '组件绑定字段所属数据表不存在！'
          }
          this.errorMessage.push(errorItem)
        }
        // 校验组件 数据表字段是否存在
        if (widget.onlieColumn && widget.columnId) {
          let errorItem = {
            widget: widget,
            message: '组件绑定字段不存在！'
          }
          this.errorMessage.push(errorItem)
        }
        // 校验组件表格数据表绑定是否正确
        if (widget.onlineColumn) {
          let table = this.onlineTableMap.get(widget.tableId)
          if (table.tableId !== widget.onlineTable.tableId) {
            let errorItem = {
              widget: widget,
              message: '组件绑定字段不属于选张的数据表！'
            }
            this.errorMessage.push(errorItem)
          }
        }

        // 初始化子组件
        if (Array.isArray(widget.childWidgetList)) {
          widget.childWidgetList.forEach(subWidget => {
            if (pageConfig.pageType === this.OnlinePageType.FLOW && this.pageReadOnly) {
              subWidget.readOnly = true
            }
            this.initWidget(subWidget, pageConfig)
          })
        }

        // 初始化字典参数
        if (widget.onlineColumn && widget.onlineColumn.onlineDict) {
          if (Array.isArray(widget.dictParamList)) {
            widget.dictParamList.forEach(dictParam => {
              if (dictParam.dictValueType === this.OnlineParamValueType.TABLE_COLUMN) {
                let linkageItem = this.linkageMap.get(dictParam.dictValue)
                if (linkageItem == null) {
                  linkageItem = []
                  this.linkageMap.set(dictParam.dictValue, linkageItem)
                }
                linkageItem.push(widget)
              }
            })
          }
        }
      }
    },
    /** 初始化组件效验规则 */
    initWidgetRule(pageConfig) {
    },
    getParamValue(valueType, valueData) {
      switch(valueType){
        case this.OnlineParamValueType.FORM_PARAM:
          return this.params ? this.params[valueData] : undefined
        case this.OnlineParamValueType.TABLE_COLUMN:{
          let column = this.onlieColumnMap.get(valueData)
          let columnValue = null
          if (this.pageConfig.formType === this.OnlinePageType.QUERY) {
            columnValue = this.formData.formFilterCopy[column.columnName]
          } else {
            columnValue = this.formData[column.columnName]
          }
          if (column == null || columnValue == null || columnValue === '') {
            return null
          } else {
            return columnValue
          }
        }
        case this.OnlineParamValueType.STATIC_DICT:
          return Array.isArray(valueData) ? valueData[1] : undefined
        case this.OnlineParamValueType.INPUT_VALUE:
          return valueData
      }
    },
    getParamValueObj(paramName, valueType, valueData, retObj) {
      try {
        if (retObj == null) retObj = {}
        retObj[paramName] = this.getParamValue(valueType, valueData)
        if (retObj[paramName] == null) return null
        return retObj
      } catch (e) {
      }
    },
    /** 加载下拉数据 */
    loadDropdownData() {
      if (Array.isArray(this.dropdownWidgetList)) {
        this.dropdownWidgetList.forEach(dropdownWidget => {
          let dropdownWidgetImpl = this.$refs[dropdownWidget.variableName][0]
          if (dropdownWidgetImpl) {
            dropdownWidgetImpl.onVisibleChange()
          }
        })
      }
    },
    clean() {
      this.formPageData = null
      this.onlineTableMap = null
      this.onlineColumnMap = null
      this.onlineDictMap = null
      this.linkageMap = null
      this.tableWidgetList = []
      this.dropdownWidgetList = []
      this.richEditWidgetList = []
    },
    reload() {
      this.loadOnlinePageConfig().then(res => {
        this.loading = false
        if (this.pageConfig.pageType === this.OnlinePageType.EDIT) {
          if (this.operationType === this.OnlineOperationType.EDIT && this.saveOnClose === '1') {
            // 初始化编辑页面数据
          } else {
            if (this.rowData != null) {
              this.formPageData = {
                ...this.rowData
              }
            }
          }
          this.loadDropdownData()
        }
      })
    },
    getPrimaryKeyColumnParam(table, row) {
      if (table && Array.isArray(table.tableColumnList)) {
        return table.tableColumnList.reduce((retObj, column) => {
          let fieldName = (table.tableType !== this.OnlineTableType.MASTER ? table.relation.variableName + '__' : '') + column.columnName
          if (column.primaryKey) {
            retObj[column.columnName] = row ? row[fieldName] : undefined
          }
          return retObj
        }, {})
      }

      return null
    },
    buildSubFormParams(operation, subFormInfo, row) {
      let subFormMasterTable = this.onlineTableMap.get(subFormInfo.masterTableId)
      if (subFormMasterTable == null) return null
      if (subFormMasterTable.tableType == this.OnlineTableType.MASTER) {
        if (operation.type === this.OnlineOperationType.EDIT) {
          return this.getPrimaryKeyColumnParam(this.masterTable, row)
        } else {
          return null
        }
      } else {
        if (subFormInfo.PageType === this.OnlinePageType.QUERY) {
          return this.getPrimaryKeyColumnParam(this.masterTable, row)
        } else {
          if (operation.type === this.OnlineOperationType.EDIT) {
            // 从表的编辑页面
            if (this.pageConfig.PageType === this.OnlinePageType.EDIT && this.operationType === this.OnlineOperationType.ADD) {
              return {
                ...row
              }
            } else {
              return this.getPrimaryKeyColumnParam(subFormMasterTable, row)
            }
          } else {
            // 从表的添加页面
            return {
              ...this.params
            }
          }
        }
      }
    },
    handlerOperation(operation, row, widget) {
      if (this.preview()) return
    },
    getRelationTableData(tableWidget) {
      if (tableWidget.widgetType === this.OnlineWidgetType.Table) {
      }
      return null
    },
    ...mapMutations(['addOnlinePageCache'])
  },
  created() {
    this.reload()
  },
  destoryed() {
    this.clean()
  },
  watch: {
    pageId: {
      handler(newValue) {
        this.reload()
      }
    }
  }
}

export {
  OnlinePageMixins
}
