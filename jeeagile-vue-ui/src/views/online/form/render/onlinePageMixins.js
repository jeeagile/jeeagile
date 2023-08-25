import { mapMutations } from 'vuex'
import { formPageRender } from '@/api/online/form'
import * as SystemStaticDict from '@/components/AgileDict'
import OnlinePagePreview from '../../index'
import { selectOneData, deleteTableData } from '@/api/online/operation'
import { pattern } from '@/components/AgileUtil/validate'

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
      const that = this

      function addFormPageData(retObj, onlineTable, onlineColumn) {
        let fieldName = that.getColumnFieldName(onlineTable, onlineColumn)
        if (retObj == null) retObj = {}
        if (pageConfig.pageType === that.OnlinePageType.QUERY) {
          if (retObj.pageFilter == null) retObj.pageFilter = {}
          if (retObj.pageFilterCopy == null) retObj.pageFilterCopy = {}
          retObj.pageFilter[fieldName] = onlineColumn.fieldType === 'Boolean' ? false : undefined
          retObj.pageFilterCopy[fieldName] = onlineColumn.fieldType === 'Boolean' ? false : undefined
        } else {
          retObj[fieldName] = onlineColumn.fieldType === 'Boolean' ? false : undefined
        }
        return retObj
      }

      // 设置数据表数据
      let formPageData = {}
      if (this.masterTable) {
        // 添加表单主表的数据
        this.masterTable.tableColumnList.forEach(column => {
          formPageData = addFormPageData(formPageData, this.masterTable, column)
        })
        // 如果表单主表是数据源主表，添加一对一关联从表数据
        if (this.masterTable.tableType == this.OnlineTableType.MASTER) {
          this.pageTableList.forEach(pageTable => {
            if (pageTable.tableType === this.OnlineTableType.ONE_TO_ONE) {
              pageTable.tableColumnList.forEach(column => {
                formPageData = addFormPageData(formPageData, pageTable, column)
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
          widget.primaryColumnName = widget.onlineTable.primaryColumnName
          if (Array.isArray(widget.tableColumnList)) {
            widget.tableColumnList.forEach(tableColumn => {
              tableColumn.onlineTable = this.onlineTableMap.get(tableColumn.tableId)
              tableColumn.onlineColumn = this.onlineColumnMap.get(tableColumn.columnId)
              if (tableColumn.onlineTable == null || tableColumn.onlineColumn == null) {
                this.errorMessage.push({
                  widget: widget,
                  message: '表格列 [' + tableColumn.showName + '] 绑定的字段不存在！'
                })
              }
            })
          }
          if (Array.isArray(widget.queryParamList)) {
            widget.queryParamList.forEach(param => {
              param.onlineTable = this.onlineTableMap.get(param.tableId)
              param.onlineColumn = this.onlineColumnMap.get(param.columnId)
              if (param.onlineTable == null || param.onlineColumn == null) {
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
        if (widget.onlineTable == null) {
          let errorItem = {
            widget: widget,
            message: '组件绑定字段所属数据表不存在！'
          }
          this.errorMessage.push(errorItem)
        }
        // 校验组件 数据表字段是否存在
        if (widget.widgetType != this.OnlineWidgetType.Table && widget.onlineColumn == null && widget.columnId == null) {
          let errorItem = {
            widget: widget,
            message: '组件绑定字段不存在！'
          }
          this.errorMessage.push(errorItem)
        }
        // 校验组件表格数据表绑定是否正确
        if (widget.onlineColumn == null) {
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
      if (Array.isArray(pageConfig.pageWidgetList)) {
        let rules = {}
        pageConfig.pageWidgetList.forEach(widget => {
          this.buildWidgetRule(widget, rules)
        })
        this.$set(this, 'rules', rules)
        this.$nextTick(() => {
          if (this.$refs.form) this.$refs.form.clearValidate()
        })
      }
    },
    buildWidgetRule(widget, rules) {
      if (widget != null && widget.onlineTable != null && widget.onlineColumn != null) {
        let widgetRuleKey = this.getColumnFieldName(widget.onlineTable, widget.onlineColumn)
        // 必填字段以及设置了验证规则的字段
        if (widget.onlineColumn.columnNullable === this.AgileYesNo.NO || Array.isArray(widget.onlineColumn.ruleList)) {
          rules[widgetRuleKey] = []
          // 必填验证
          if (widget.onlineColumn.columnNullable === this.AgileYesNo.NO) {
            rules[widgetRuleKey].push(
              { required: true, message: widget.showName + '不能为空！', trigger: 'blur' }
            )
          }
          // 其他验证
          if (Array.isArray(widget.onlineColumn.ruleList)) {
            widget.onlineColumn.ruleList.forEach(columnRule => {
              let ruleItem = this.buildRuleItem(widget, columnRule)
              if (ruleItem) rules[widgetRuleKey].push(ruleItem)
            })
          }
        }
      }
    },
    buildRuleItem(widget, rule) {
      if (rule.ruleConfig) rule.data = JSON.parse(rule.ruleConfig)
      if (widget != null && rule != null) {
        switch(rule.ruleType){
          case this.OnlineRuleType.INTEGER: // 只允许整数
            return {
              type: 'integer',
              message: rule.data.message,
              trigger: 'blur',
              transform: (value) => Number(value)
            }
          case this.OnlineRuleType.DIGITAL: // 只允许数字
            return {
              type: 'number',
              message: rule.data.message,
              trigger: 'blur',
              transform: (value) => Number(value)
            }
          case this.OnlineRuleType.LETTER: // 只允许英文字符
            return {
              type: 'string',
              pattern: pattern.english,
              message: rule.data.message,
              trigger: 'blur'
            }
          case this.OnlineRuleType.RANGE: // 范围验证
            if (widget.column) {
              let isNumber = ['Boolean', 'Date', 'String'].indexOf(widget.column.objectFieldType) === -1
              return {
                type: isNumber ? 'number' : 'string',
                min: rule.data.min,
                max: rule.data.max,
                message: rule.data.message,
                trigger: 'blur'
              }
            }
            break
          case this.OnlineRuleType.EMAIL: // 邮箱格式验证
            return {
              type: 'email',
              message: rule.data.message,
              trigger: 'blur'
            }
          case  this.OnlineRuleType.MOBILE: // 手机格式验证
            return {
              type: 'string',
              pattern: pattern.mobile,
              message: rule.data.message,
              trigger: 'blur'
            }
          case this.OnlineRuleType.CUSTOM: // 自定义验证
            return {
              type: 'string',
              pattern: new RegExp(rule.data.pattern),
              message: rule.data.message,
              trigger: 'blur'
            }
        }
      }
    },
    getParamValue(valueType, valueData) {
      switch(valueType){
        case this.OnlineParamValueType.FORM_PARAM:
          return this.params ? this.params[valueData] : undefined
        case this.OnlineParamValueType.TABLE_COLUMN:{
          const onlineColumn = this.onlineColumnMap.get(valueData)
          const onlineTable = this.onlineTableMap.get(onlineColumn.tableId)
          const columnValue = this.formPageData[this.getColumnFieldName(onlineTable, onlineColumn)]
          if (onlineColumn == null || columnValue == null || columnValue === '') {
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
      this.formPageData = {}
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
            let params = {
              tableId: this.masterTable.tableId,
              dataId: this.params[this.masterTable.primaryColumnName]
            }
            selectOneData(params).then(response => {
              this.formPageData = {
                ...this.formPageData,
                ...response.data
              }
              this.loadDropdownData()
            })

            return
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
    /** 获取主键数据 */
    getPrimaryColumnParam(onlineTable, row) {
      if (onlineTable) {
        let retObj = {}
        retObj[onlineTable.primaryColumnName] = row ? row[this.getPrimaryFieldName(onlineTable)] : undefined
        return retObj
      }
      return null
    },
    /** 组装子页面 */
    buildSubFormParams(operation, subFormInfo, row) {
      let subFormMasterTable = this.onlineTableMap.get(subFormInfo.tableId)
      if (subFormMasterTable == null) return null
      if (subFormMasterTable.tableType == this.OnlineTableType.MASTER) {
        if (operation.type === this.OnlineOperationType.EDIT) {
          return this.getPrimaryColumnParam(this.masterTable, row)
        } else {
          return null
        }
      } else {
        if (subFormInfo.pageType === this.OnlinePageType.QUERY) {
          return this.getPrimaryColumnParam(this.masterTable, row)
        } else {
          if (operation.type === this.OnlineOperationType.EDIT) {
            if (this.pageConfig.pageType === this.OnlinePageType.EDIT && this.operationType === this.OnlineOperationType.ADD) {
              return {
                ...row
              }
            } else {
              return this.getPrimaryColumnParam(subFormMasterTable, row)
            }
          } else {
            if (this.params === null) {
              this.params = {}
            }
            const masterOnlineTable = this.onlineTableMap.get(subFormMasterTable.masterTableId)
            const masterOnlineColumn = this.onlineColumnMap.get(subFormMasterTable.masterColumnId)
            const fieldName = this.getColumnFieldName(masterOnlineTable, masterOnlineColumn)
            this.params[subFormMasterTable.slaveColumnName] = this.formPageData[fieldName]
            return {
              ...this.params
            }
          }
        }
      }
    },
    /** 处理操作 */
    handlerOperation(operation, row, widget) {
      if (this.preview()) return
      if (operation.pageId != null) {
        formPageRender({ pageId: operation.pageId }).then(response => {
          let onlinePage = response.data.onlinePage
          if (onlinePage != null) {
            let params = this.buildSubFormParams(operation, onlinePage, row)
            if (onlinePage.pageKind === this.OnlinePageKind.JUMP) {
              let pageJsonData = JSON.parse(onlinePage.widgetJson)
              let area = (onlinePage.height != null) ? [(pageJsonData.pageConfig.width || 800) + 'px', pageJsonData.pageConfig.height + 'px'] : (pageJsonData.pageConfig.width || 800) + 'px'
              this.$dialog.show(operation.name, OnlinePagePreview, {
                area: area,
                offset: '100px'
              }, {
                flowData: this.flowData,
                pageId: onlinePage.id,
                pageType: onlinePage.pageType,
                operationType: operation.type,
                params,
                saveOnClose: (
                  onlinePage.pageType === this.OnlinePageType.FLOW ||
                  this.pageConfig.pageType === this.OnlinePageType.FLOW ||
                  (onlinePage.pageType === this.OnlinePageType.EDIT && this.operationType === this.OnlineOperationType.ADD)
                ) ? '0' : '1',
                rowData: row
              }).then(res => {
                let widgetObj = this.$refs[widget.variableName]
                if (Array.isArray(widgetObj)) {
                  widgetObj.forEach(item => {
                    item.refresh(res, operation.type)
                  })
                } else {
                  widgetObj.refresh(res, operation.type)
                }
              })
            } else {
              if (this.pageConfig.pageType === this.OnlinePageType.QUERY) {
                let tableWidget = this.$refs[this.pageConfig.pageQueryTable.variableName].getTableWidget()
                this.addOnlinePageCache({
                  key: this.$route.fullPath,
                  value: {
                    pageFilter: { ...this.formPageData.pageFilter },
                    pageFilterCopy: { ...this.formPageData.pageFilterCopy },
                    tableImpl: {
                      totalCount: tableWidget.totalCount,
                      currentPage: tableWidget.currentPage,
                      pageSize: tableWidget.pageSize
                    }
                  }
                })
              }
              this.$router.push({
                name: 'onlinePage',
                query: {
                  flowData: this.flowData,
                  pageId: onlinePage.pageId,
                  pageType: onlinePage.pageType,
                  closeVisible: '1',
                  operationType: onlinePage.type,
                  params,
                  saveOnClose: (
                    onlinePage.pageType === this.OnlinePageType.FLOW ||
                    this.pageConfig.pageType === this.OnlinePageType.FLOW ||
                    (onlinePage.pageType === this.OnlinePageType.EDIT && this.operationType === this.OnlineOperationType.ADD)
                  ) ? '0' : '1',
                  rowData: row
                }
              })
            }
          }
        })
      } else {
        if (operation.type === 'DELETE') {
          this.$confirm('是否删除当前数据？').then(res => {
            if (this.pageConfig.pageType !== this.OnlinePageType.FLOW &&
              (this.pageConfig.pageType !== this.OnlinePageType.EDIT || this.operationType !== this.OnlineOperationType.ADD)) {
              let params = {
                tableId: (widget.onlineTable || {}).tableId
              }
              params.dataId = row[this.getPrimaryFieldName(widget.onlineTable)]
              deleteTableData(params).then(response => {
                this.messageSuccess('删除成功！')
                let widgetObj = this.$refs[widget.variableName]
                if (Array.isArray(widgetObj)) {
                  widgetObj.forEach(item => {
                    item.refresh(res, operation.type)
                  })
                } else {
                  widgetObj.refresh(res, operation.type)
                }
              })
            } else {
              let widgetObj = this.$refs[widget.variableName]
              if (Array.isArray(widgetObj)) {
                widgetObj.forEach(item => {
                  item.refresh(row, operation.type)
                })
              } else {
                widgetObj.refresh(row, operation.type)
              }
            }
          })
        } else if (operation.type === this.OnlineOperationType.EXPORT) {
          this.messageWarning('导出操作正在码砖中，请耐心等待！')
        }
      }
    },
    /** 获取关联表数据 */
    getRelationTableData(tableWidget) {
      if (tableWidget.widgetType === 'Table') {
        let onlineTable = tableWidget.onlineTable
        let temp = this.$refs[tableWidget.variableName]
        if (onlineTable != null && Array.isArray(onlineTable.tableColumnList) && temp != null) {
          let tableWidgetImpl = temp[0] || temp
          return tableWidgetImpl.getTableWidget().dataList.map(data => {
            return onlineTable.tableColumnList.reduce((retObj, onlineColumn) => {
              let fieldName = this.getColumnFieldName(onlineTable, onlineColumn)
              retObj[onlineColumn.columnName] = data[fieldName]
              return retObj
            }, {})
          })
        }
      }
      return null
    },
    /** 获取主键字段名  */
    getPrimaryFieldName(onlineTable) {
      if (onlineTable && Array.isArray(onlineTable.tableColumnList)) {
        const primaryOnlineColumn = onlineTable.tableColumnList.find(item => item.columnId === onlineTable.primaryColumnId)
        return this.getColumnFieldName(onlineTable, primaryOnlineColumn)
      }
      return null
    },
    /** 获取字段名  */
    getColumnFieldName(onlineTable, onlineColumn) {
      return (onlineTable.tableType !== this.OnlineTableType.MASTER ? onlineTable.modelName + '__' : '') + onlineColumn.fieldName
    },
    ...mapMutations(['addOnlinePageCache'])
  },
  created() {
    this.reload()
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
