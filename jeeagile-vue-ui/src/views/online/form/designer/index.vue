<template>
  <div class="page-designer-container">
    <el-container style="height: 100%;">
      <el-aside v-if="onlinePage.pageType!==OnlinePageType.ORDER" class="page-designer-left" style="width: 250px">
        <el-scrollbar>
          <div class="page-designer-card" v-if="onlinePage.pageType!==OnlinePageType.QUERY">
            <div class="card-title">布局组件</div>
            <draggable
              class="page-designer-draggable"
              draggable=".draggable-card-item"
              :list="baseWidgetList"
              :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
              :clone="cloneBaseWidget"
              :sort="false"
            >
              <div v-for="baseWidget in baseWidgetList" :key="baseWidget.id"
                   class="draggable-card-item" @click="createBaseWidget(baseWidget)"
                   @dragend="dragendWidget(baseWidget.id)">
                <svg-icon :icon-class="baseWidget.icon"/>
                <span style="margin-left: 5px;" :title="baseWidget.label">{{baseWidget.label}}</span>
                <el-badge :value="getWidgetUseCount(baseWidget.id)" class="item"/>
              </div>
            </draggable>
          </div>
          <div class="page-designer-card" v-for="pageTable in pageTableList" :key="pageTable.tableName">
            <div class="card-title">
              <span :title="pageTable.tableName">
                {{pageTable.tableLabel}}
              </span>
              <el-tag v-if="getPageMasterTable !== pageTable" size="mini" style="margin-left: 5px;" effect="dark"
                      :type="OnlineTableType.getTag(pageTable.tableType)">
                {{OnlineTableType.getLabel(pageTable.tableType)}}
              </el-tag>
            </div>
            <draggable class="page-designer-draggable" draggable=".draggable-card-item"
                       :list="getPageTableColumnList(pageTable)"
                       :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
                       :clone="cloneColumnComponent"
                       :sort="false"
            >
              <div class="draggable-card-item"
                   v-if="pageTable.tableType === OnlineTableType.ONE_TO_MANY && getPageMasterTable!== pageTable "
                   style="width: 100%;" @click="createColumnWidget(getPageTableColumnList(pageTable)[0])"
                   @dragend="dragendWidget(getPageTableColumnList(pageTable)[0].id)">
                <i class="el-icon-bank-card"/>
                <span style="margin-left: 5px;" :title="pageTable.tableLabel + '关联数据'">关联数据</span>
                <el-badge :value="getWidgetUseCount(getPageTableColumnList(pageTable)[0].id)" class="badge-item"/>
              </div>
              <div v-else class="draggable-card-item" v-for="column in getPageTableColumnList(pageTable)"
                   :key="column.id" @click="createColumnWidget(column)" @dragend="dragendWidget(column.id)">
                <i class="el-icon-bank-card"/>
                <span style="margin-left: 5px;" :title="column.fieldLabel">{{column.fieldLabel}}</span>
                <el-badge v-if="onlinePage.pageType !== OnlinePageType.QUERY" :value="widgetUseCount[column.id]"
                          class="badge-item"/>
              </div>
            </draggable>
          </div>
        </el-scrollbar>
      </el-aside>
      <el-main class="page-designer-center">
        <el-row type="flex" justify="end" align="middle" class="title">
          <el-breadcrumb separator-class="el-icon-arrow-right" style="width: 100%; margin-left: 10px;">
            <el-breadcrumb-item>
              <i class="el-icon-setting" style="margin-right: 10px;"/>{{onlinePage.pageName}}
            </el-breadcrumb-item>
          </el-breadcrumb>
          <el-button class="table-btn delete" type="text" icon="el-icon-delete">重置</el-button>
          <el-button type="text" icon="el-icon-video-play">预览</el-button>
          <el-button class="table-btn warning" type="text" icon="el-icon-check" @click="savePageDesigner">保存</el-button>
          <el-button class="table-btn success" type="text" icon="el-icon-back" @click="handleBack">返回</el-button>
        </el-row>
        <el-row style="margin: 0px">
          <el-col :span="24">
            <el-scrollbar style="height:calc(100vh - 220px)">
              <div @click="clickPageDesigner">
                <template
                  v-if="onlinePage.pageType === OnlinePageType.QUERY || onlinePage.pageType === OnlinePageType.ORDER">
                  <div style="position: relative;">
                    <el-form size="mini" :label-width="pageConfig.labelWidth + 'px'"
                             :label-position="pageConfig.labelPosition" @submit.native.prevent>
                      <drag-widget-filter :list="pageWidgetList"
                                          :style="{'min-height': '50px'}"
                                          style="padding: 10px 10px 0px 10px; overflow: hidden; justify-content: space-between;"
                      >
                        <template v-if="pageConfig.pageType === OnlinePageType.QUERY">
                          <drag-widget-item v-for="pageWidget in pageWidgetList" :key="pageWidget.id"
                                            :class="{'active-widget': (pageWidget === currentWidget && !pageWidget.hasError)}"
                                            :widgetConfig="pageWidget"
                                            @click="clickDragWidget"
                                            @delete="deleteDragWidget"
                          />
                          <div slot="operator" style="padding: 13px 10px;" v-for="operation in getTableOperation(false)"
                               :key="operation.id">
                            <el-button size="mini" :icon="operation.icon" :plain="operation.plain"
                                       :type="operation.btnType"
                                       @click.stop="">
                              {{operation.name}}
                            </el-button>
                          </div>
                          <div slot="search" style="padding: 13px 10px;"
                               v-if="Array.isArray(pageWidgetList) && pageWidgetList.length > 0">
                            <el-button type="cyan" icon="el-icon-search" size="mini" :plain="true">搜索</el-button>
                            <el-button icon="el-icon-refresh" size="mini" :plain="true">重置</el-button>
                          </div>
                        </template>

                        <template v-if="onlinePage.pageType === OnlinePageType.ORDER">
                          <el-row>
                            <el-col :span="12">
                              <el-form-item label="工单状态">
                                <el-select class="filter-item" v-model="flowWorkStatus" :clearable="true"
                                           placeholder="工单状态"/>
                              </el-form-item>
                            </el-col>
                            <el-col :span="12">
                              <el-form-item label="创建日期">
                                <el-date-picker class="filter-item" style="width: 200px" :clearable="true"
                                                :allowTypes="['day']" align="left"
                                                format="yyyy-MM-dd" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                                                range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"/>
                              </el-form-item>
                            </el-col>
                          </el-row>
                          <div slot="operator" style="padding: 13px 10px;">
                            <el-button type="primary" icon="el-icon-plus" size="mini">新建</el-button>
                          </div>
                          <div slot="search" style="padding: 13px 10px;">
                            <el-button type="cyan" icon="el-icon-search" size="mini" :plain="true">搜索</el-button>
                            <el-button icon="el-icon-refresh" size="mini" :plain="true">重置</el-button>
                          </div>
                        </template>
                      </drag-widget-filter>
                    </el-form>
                    <el-row style="padding: 0px 10px 10px 10px;">
                      <drag-widget-item v-if="pageConfig.tableWidget"
                                        :class="{'active-widget': (pageConfig.tableWidget === currentWidget && !pageConfig.tableWidget.hasError)}"
                                        :widgetConfig="pageConfig.tableWidget"
                                        :canDelete="false"
                                        @click="clickDragWidget"
                                        @delete="deleteDragWidget"
                      />
                    </el-row>
                  </div>
                </template>
                <el-row v-else :gutter="pageConfig.gutter" style="margin: 0px;">
                  <el-form class="full-width-input" size="mini" :label-width="pageConfig.labelWidth + 'px'"
                           :label-position="pageConfig.labelPosition">
                    <draggable draggable=".draggable-item" :animation="340" :list="pageWidgetList"
                               group="componentsGroup"
                               style="padding: 10px; overflow: hidden;min-height:300px "
                    >
                      <drag-widget-item v-for="pageWidget in pageWidgetList" :key="pageWidget.id"
                                        :class="{'active-widget': (pageWidget === currentWidget && !pageWidget.hasError)}"
                                        :widgetConfig="pageWidget"
                                        @click="clickDragWidget"
                                        @delete="deleteDragWidget"
                      />
                    </draggable>
                  </el-form>
                </el-row>
              </div>
            </el-scrollbar>
          </el-col>
        </el-row>
      </el-main>
      <el-aside class="page-designer-right" style="width: 280px;overflow: hidden">
      </el-aside>
    </el-container>
  </div>
</template>

<script>
  import Vue from 'vue'
  import Draggable from 'vuedraggable'
  import { OnlineBaseWidgetList, DefaultWidgetAttributes } from '../config'
  import { selectPageTableList } from '@/api/online/table'
  import { selectOnlinePageList, savePageDesigner } from '@/api/online/page'
  import DragWidgetItem from './dragWidgetItem'
  import DragWidgetFilter from './dragWidgetFilter'
  import { findItemFromList } from '../util'

  Vue.component('drag-widget-item', DragWidgetItem)
  export default {
    name: 'PageDesigner',
    components: { Draggable, DragWidgetFilter, DragWidgetItem },
    props: {
      onlineForm: {    // 表单信息
        type: Object,
        required: true
      },
      onlinePage: {    // 页面信息
        type: Object,
        required: true
      }
    },
    data() {
      return {
        isLocked: false,
        flowWorkStatus: undefined,
        pageTableList: [],
        onlinePageList: [],
        pageWidgetList: [],
        pageConfig: {},
        widgetUseCount: {},
        currentWidget: undefined,
        currentWidgetDict: undefined,
        widgetVariableNameSet: new Set(),
        baseWidgetList: OnlineBaseWidgetList,
        activeTabName: undefined
      }
    },
    provide() {
      return {
        current: () => this.currentWidget,
        pageConfig: () => this.pageConfig,
        isLocked: () => this.isLocked,
        preview: () => true
      }
    },
    created() {
      this.getOnlinePageList()
      this.getPageTableList()
    },
    computed: {
      /** 获取在线表单页面主表 */
      getPageMasterTable() {
        return this.pageTableList.find(item => item.tableId === this.onlinePage.tableId)
      },
      /** 获取组件使用次数 */
      getWidgetUseCount() {
        return (id) => this.widgetUseCount[id]
      },
      /** 获取组件使用次数 */
      getPageParameterList() {
        if (this.getPageMasterTable != null) {
          return this.getPageMasterTable.tableColumnList.filter(item => {
            if (this.pageConfig.pageType === this.OnlinePageType.QUERY) {
              // 查询页面表单参数为主表的查询字段
              return item.filterType !== this.OnlineFilterType.NONE
            } else {
              // 编辑页面表单参数为主表的主键字段
              return item.columnPrimary === this.SysYesNo.YES
            }
          })
        } else {
          return []
        }
      }
    },
    methods: {
      /** 获取在线表单页面列表 */
      getOnlinePageList() {
        selectOnlinePageList({ formId: this.onlinePage.formId }).then(response => {
            this.onlinePageList = response.data
          }
        )
      },
      /** 获取在线表单页面数据表列表 */
      getPageTableList() {
        selectPageTableList({ pageId: this.onlinePage.id }).then(response => {
            this.pageTableList = response.data
            this.initPageConfig()
          }
        )
      },
      /** 初始化表单页面配置 */
      initPageConfig() {
        let pageJsonData = JSON.parse(this.onlinePage.widgetJson)
        this.pageWidgetList = pageJsonData.widgetList || []
        this.pageWidgetList.map(item => {
          if (!this.widgetUseCount[item.columnId]) {
            this.widgetUseCount[item.columnId] = 0
          }
          this.widgetUseCount[item.columnId] = this.widgetUseCount[item.columnId] + 1
        })
        this.pageConfig = {
          ...pageJsonData.pageConfig,
          pageType: this.onlinePage.pageType,
          pageKind: this.onlinePage.pageKind
        }
        // 初始化表单的参数
        this.pageConfig.paramList = []
        if (this.getPageMasterTable.tableType !== this.OnlineTableType.MASTER || this.pageConfig.pageType !== this.OnlinePageType.QUERY) {
          // 编辑表单必须包含主表主键id
          if (this.pageConfig.pageType !== this.OnlinePageType.QUERY) {
            let primaryKeyColumn = findItemFromList(this.getPageMasterTable.tableColumnList, this.SysYesNo.YES, 'columnPrimary')
            if (primaryKeyColumn !== null) {
              this.pageConfig.paramList.unshift({
                columnName: primaryKeyColumn.columnName,
                columnPrimary: true,
                slaveColumn: false,
                builtin: true
              })
            }
          }
          // 一对多从表查询页面必须包含从表关联字段
          if (this.pageConfig.pageType === this.OnlinePageType.QUERY && this.getPageMasterTable.tableType === this.OnlineTableType.ONE_TO_MANY) {
            let slaveColumn = findItemFromList(this.getPageMasterTable.tableColumnList, this.getPageMasterTable.slaveColumnId, 'columnId')
            if (slaveColumn !== null) {
              this.pageConfig.paramList.unshift({
                columnName: slaveColumn.columnName,
                columnPrimary: false,
                slaveColumn: true,
                builtin: true
              })
            }
          }
        }
        if (this.pageConfig.pageType === this.OnlinePageType.QUERY) {
          this.buildTableWidgetInfo(this.pageConfig.tableWidget)
          this.widgetVariableNameSet.add(this.pageConfig.tableWidget.variableName)
        }
        this.checkPageWidgetList()
      },
      /** 校验表单页面组件 */
      checkPageWidgetList() {
        let that = this

        function checkPageWidget(widget) {
          if (widget && widget.variableName) {
            widget.hasError = false
            // 判断组件绑定字段所属数据源或者关联是否存在
            let tableId = widget.tableId
            if (tableId != null) {
              widget.onlineTable = findItemFromList(that.pageTableList, widget.tableId, 'tableId')
              if (widget.onlineTable == null) {
                widget.hasError = true
                widget.errorMessage = '组件 [' + widget.showName + '] 绑定数据表错误！'
              } else {
                if (widget.columnId != null) {
                  widget.onlineColumn = findItemFromList(widget.onlineTable.tableColumnList, widget.columnId, 'columnId')
                  if (widget.onlineColumn == null) {
                    widget.hasError = true
                    widget.errorMessage = '组件 [' + widget.showName + ' ]绑定数据表字段并不属于数据表 [' + widget.onlineTable.tableName + ' ]'
                  }
                }
              }
            } else {
              const defaultWidget = OnlineBaseWidgetList.find(item => item.widgetType === widget.widgetType)
              if (!defaultWidget) {
                widget.hasError = true
                widget.errorMessage = '组件 [' + widget.showName + ' ]绑定字段所属数据表不存在！'
              }
            }

            if (widget.hasError) {
              console.error(widget.errorMessage)
            }

            that.widgetVariableNameSet.add(widget.variableName)
            if (Array.isArray(widget.childWidgetList)) {
              widget.childWidgetList.forEach(subWidget => {
                checkPageWidget(subWidget)
              })
            }
          }
        }

        this.pageWidgetList.forEach(widget => {
          checkPageWidget(widget)
        })
        checkPageWidget(this.pageConfig.tableWidget)
      },
      /** 创建基础组件 */
      createBaseWidget(widget) {
        if (this.currentWidget && this.currentWidget.widgetType === this.OnlineWidgetType.Card) {
          this.currentWidget.childWidgetList.push(this.cloneBaseWidget(widget))
        } else {
          this.pageWidgetList.push(this.cloneBaseWidget(widget))
        }
        if (!this.widgetUseCount[widget.id]) {
          this.widgetUseCount[widget.id] = 0
        }
        this.widgetUseCount[widget.id] = this.widgetUseCount[widget.id] + 1
      },
      /** 组件拖拽 */
      dragendWidget(id) {
        if (!this.widgetUseCount[id]) {
          this.widgetUseCount[id] = 0
        }
        this.widgetUseCount[id] = this.widgetUseCount[id] + 1
      },
      /** 克隆基础组件 */
      cloneBaseWidget(widget) {
        let attrs = DefaultWidgetAttributes[widget.type]
        return {
          ...attrs,
          id: new Date().getTime(),
          columnId: widget.id,
          columnName: widget.name,
          showName: widget.label,
          variableName: this.handleWidgetVariableName(widget.name),
          widgetCategory: this.onlinePage.pageType === this.OnlinePageType.QUERY ? this.OnlineWidgetKind.Filter : attrs.widgetKind
        }
      },
      /** 组装组件名称 */
      handleWidgetVariableName(variableName) {
        let index = 0
        let name = variableName
        do {
          if (!this.widgetVariableNameSet.has(name)) {
            this.widgetVariableNameSet.add(name)
            break
          }
          index++
          name = variableName + index
          // eslint-disable-next-line no-constant-condition
        } while (true)
        return name
      },
      /** 获取组件基础属性 */
      getColumnDefaultAttributes(column) {
        if (column === null) return {}
        if (column.filterType === this.OnlineFilterType.RANGE && this.onlinePage.pageType === this.OnlinePageType.QUERY) {
          if (column.fieldType === 'Date') {
            return { ...DefaultWidgetAttributes.DateRange }
          } else {
            return { ...DefaultWidgetAttributes.NumberRange }
          }
        }
        // eslint-disable-next-line default-case
        switch (column.fieldKind) {
        case this.OnlineFieldKind.UPLOAD_FILE:
        case this.OnlineFieldKind.UPLOAD_IMAGE: {
          return {
            ...DefaultWidgetAttributes.Upload,
            isImage: column.fieldKind === this.OnlineFieldKind.UPLOAD_IMAGE,
            ...this.buildUploadWidgetUrlInfo(column)
          }
        }
        case this.OnlineFieldKind.RICH_TEXT:
          return { ...DefaultWidgetAttributes.RichEditor }
        case this.OnlineFieldKind.CREATE_TIME:
        case this.OnlineFieldKind.UPDATE_TIME:
          return { ...DefaultWidgetAttributes.Date }
        case this.OnlineFieldKind.CREATE_USER:
        case this.OnlineFieldKind.UPDATE_USER:
        case this.OnlineFieldKind.LOGIC_DELETE:
          return { ...DefaultWidgetAttributes.Label }
        }
        if (column.dictId && column.dictId !== '' && column.onlineDict) {
          return column.onlineDict.treeFlag ? { ...DefaultWidgetAttributes.Cascader } : { ...DefaultWidgetAttributes.Select }
        }
        if (column.fieldType === 'Boolean') {
          return { ...DefaultWidgetAttributes.Switch }
        } else if (column.fieldType === 'Date') {
          return { ...DefaultWidgetAttributes.Date }
        } else if (column.fieldType === 'String') {
          return { ...DefaultWidgetAttributes.Input }
        } else {
          return { ...DefaultWidgetAttributes.NumberInput }
        }
      },
      /** 克隆组件 */
      cloneColumnComponent(column) {
        let attrs = null
        if (column.onlineTable) {
          attrs = {
            ...DefaultWidgetAttributes.Table,
            tableColumnList: [],
            operationList: [...DefaultWidgetAttributes.Table.operationList],
            queryParamList: [],
            tableInfo: { ...DefaultWidgetAttributes.Table.tableInfo }
          }
        } else {
          attrs = this.getColumnDefaultAttributes(column)
        }

        return {
          ...attrs,
          id: new Date().getTime(),
          tableId: column.tableId,
          columnId: column.columnId,
          fieldName: column.fieldName,
          showName: column.fieldLabel,
          variableName: this.handleWidgetVariableName(column.fieldName),
          widgetKind: this.onlinePage.pageType === this.OnlinePageType.QUERY ? this.OnlineWidgetKind.Filter : attrs.widgetKind,
          onlineColumn: column
        }
      },
      /** 获取数据表字段 */
      getPageTableColumnList(pageTable) {
        if (pageTable && pageTable.tableColumnList) {
          let temp = [
            ...pageTable.tableColumnList.filter(item => {
              let usedWidget = findItemFromList(this.pageWidgetList, item.columnId, 'columnId')
              return (
                this.onlinePage.pageType !== this.OnlinePageType.QUERY || (item.filterType !== this.OnlineFilterType.NONE && !usedWidget)
              )
            }).map(item => {
              return {
                ...item,
                tableId: pageTable.tableId,
                tableType: pageTable.tableType
              }
            })
          ]
          if (pageTable && pageTable.tableType === this.OnlineTableType.ONE_TO_MANY && this.getPageMasterTable !== pageTable) {
            temp.unshift({
              tableId: pageTable.tableId,
              columnName: pageTable.modelName,
              fieldName: pageTable.modelName,
              fieldLabel: pageTable.tableLabel,
              widgetType: this.OnlineWidgetType.Table,
              tableColumnList: pageTable.tableColumnList,
              onlineTable: pageTable
            })
          }
          return temp
        } else {
          return []
        }
      },
      /** 创建字段组件 */
      createColumnWidget(column) {
        if (this.currentWidget && this.currentWidget.widgetType === this.OnlineWidgetType.Card) {
          this.currentWidget.childWidgetList.push(this.cloneColumnComponent(column))
        } else {
          this.pageWidgetList.push(this.cloneColumnComponent(column))
        }
        if (!this.widgetUseCount[column.id]) {
          this.widgetUseCount[column.id] = 0
        }
        this.widgetUseCount[column.id] = this.widgetUseCount[column.id] + 1
      },
      /** 点击页面设计器 */
      clickPageDesigner() {
        if (this.currentWidget) this.activeTabName = 'widget'
        this.currentWidget = null
      },
      /** 点击画布组件 */
      clickDragWidget(widget) {
        if (widget === this.currentWidget) return
        this.currentWidgetDict = undefined
        if (widget) {
          if (widget.onlincColumn && widget.column.onlineDict && widget.onlincColumn.onlineDict.paramList === null) {
            if (widget.dictParamList) {
              widget.onlincColumn.onlineDict.paramList = widget.dictParamList
            } else {
              if (widget.onlincColumn.onlineDict.dictParamJson) {
                widget.onlincColumn.onlineDict.paramList = JSON.parse(widget.onlincColumn.onlineDict.dictParamJson)
              }
            }
          }
          if (widget.onlincColumn) this.currentWidgetDict = widget.onlincColumn.onlineDict
        }
        this.currentWidget = widget
        if (this.currentWidget.widgetType === 'Table') {
          this.buildTableWidgetInfo(this.currentWidget)
        }
        this.activeTabName = 'widget'
      },
      /** 组装表格组件 */
      buildTableWidgetInfo(tableWidget) {
        if (tableWidget != null) {
          let onlineTable = findItemFromList(this.pageTableList, tableWidget.tableId, 'tableId')
          if (onlineTable != null) {
            tableWidget.onlineTable = onlineTable
            if (Array.isArray(onlineTable.tableColumnList) && Array.isArray(tableWidget.tableColumnList)) {
              tableWidget.tableColumnList.forEach(item => {
                let columnTable = findItemFromList(this.pageTableList, item.tableId, 'tableId')
                item.onlineTable = columnTable
                item.onlineColumn = findItemFromList(columnTable.tableColumnList, item.columnId, 'columnId')
              })
            }
            if (Array.isArray(onlineTable.tableColumnList) && Array.isArray(tableWidget.queryParamList)) {
              tableWidget.queryParamList.forEach(item => {
                let columnTable = findItemFromList(this.pageTableList, item.tableId, 'tableId')
                item.onlineTable = columnTable
                item.onlineColumn = findItemFromList(columnTable.tableColumnList, item.columnId, 'columnId')
              })
            }
          }
        }
      },
      /** 删除画布组件 */
      deleteDragWidget(widget, onlyDeleteName = false) {
        if (this.widgetUseCount[widget.columnId] - 1 === 0) {
          this.widgetUseCount[widget.columnId] = undefined
        } else {
          this.widgetUseCount[widget.columnId] = this.widgetUseCount[widget.columnId] - 1
        }
        this.widgetVariableNameSet.delete(widget.variableName)
        if (!onlyDeleteName) {
          this.pageWidgetList = this.pageWidgetList.filter(item => {
            return widget.id !== item.id
          })
        }
        if (this.currentWidget === widget) {
          this.currentWidget = null
          this.activeTabName = 'widget'
        }
      },
      /** 获取table操作按钮 */
      getTableOperation(rowOperation) {
        return this.pageConfig.tableWidget.operationList.filter(operation => {
          return operation.rowOperation === rowOperation && operation.enabled
        })
      },
      /** 保存表单设计 */
      savePageDesigner() {
        let pageConfig = {
          pageKind: this.pageConfig.pageKind,
          pageType: this.pageConfig.pageType,
          gutter: this.pageConfig.gutter,
          labelWidth: this.pageConfig.labelWidth,
          labelPosition: this.pageConfig.labelPosition,
          width: this.pageConfig.width,
          height: this.pageConfig.height,
          paramList: this.pageConfig.paramList
        }
        if (this.pageConfig.tableWidget != null) {
          pageConfig.tableWidget = {
            ...this.pageConfig.tableWidget,
            tableId: this.pageConfig.tableWidget.tableId,
            onlineTable: undefined,
            // eslint-disable-next-line multiline-ternary
            tableColumnList: Array.isArray(this.pageConfig.tableWidget.tableColumnList) ? this.pageConfig.tableWidget.tableColumnList.map(tableColumn => {
              return {
                ...tableColumn,
                onlineColumn: undefined,
                onlineTable: undefined
              }
            }) : [],
            // eslint-disable-next-line multiline-ternary
            queryParamList: Array.isArray(this.pageConfig.tableWidget.queryParamList) ? this.pageConfig.tableWidget.queryParamList.map(param => {
              return {
                ...param,
                onlineColumn: undefined,
                onlineTable: undefined
              }
            }) : []
          }
        }

        let widgetList = this.pageWidgetList.map(widget => {
          let dictParamList = null
          if (widget.onlineColumn && widget.onlineColumn.onlineDict) {
            dictParamList = widget.onlineColumn.onlineDict.paramList || widget.dictParamList
          }
          return {
            ...widget,
            dictParamList,
            // eslint-disable-next-line multiline-ternary
            queryParamList: Array.isArray(widget.queryParamList) ? widget.queryParamList.map(param => {
              return {
                ...param,
                onlineColumn: undefined,
                onlineTable: undefined
              }
            }) : [],
            // eslint-disable-next-line multiline-ternary
            tableColumnList: Array.isArray(widget.tableColumnList) ? widget.tableColumnList.map(tableColumn => {
              return {
                ...tableColumn,
                onlineColumn: undefined,
                onlineTable: undefined
              }
            }) : [],
            onlineColumn: undefined,
            onlineTable: undefined
          }
        })

        const widgetJson = JSON.stringify({
          pageConfig,
          widgetList
        })
        const paramsJson = JSON.stringify(this.getPageParameterList)

        const onlinePage = {
          id: this.onlinePage.id,
          widgetJson: widgetJson,
          paramsJson: paramsJson
        }

        savePageDesigner(onlinePage).then(response => {
          this.messageSuccess('表单页面设计配置信息保存成功！')
        })
      },
      /** 退出表单页面设计 */
      handleBack() {
        this.$emit('close')
      }
    }
  }
</script>

<style lang='scss'>
  .page-designer-container {
    height: 100%;

    .page-designer-left {
      overflow: hidden;
      background: white;

      .el-scrollbar {
        height: calc(100vh - 170px);
      }

      .el-scrollbar__wrap {
        overflow-x: hidden;
      }

      .page-designer-card {
        background: white;
        margin-bottom: 10px;
        padding: 0px 10px;

        .card-title {
          color: #043254;
          line-height: 38px;
          font-size: 14px;
          font-weight: 500;
          display: flex;
          align-items: center;
          border-bottom: 2px solid #DFE4ED
        }

        .card-title span {
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          max-width: 200px;
          flex-shrink: 0;
        }


      }
    }

    .page-designer-draggable {
      display: flex;
      justify-content: space-between;
      flex-wrap: wrap;
      margin-top: 8px;
    }

    .draggable-card-item {
      display: flex;
      position: relative;
      align-items: center;
      padding: 0px 8px;
      margin-bottom: 10px;
      background: #f3f9ff;
      border: 1px dashed #f3f9ff;
      font-size: 12px;
      color: #043254;
      cursor: move;
      border-radius: 3px;
      height: 35px;
      line-height: 35px;
      width: 110px;
    }

    .draggable-card-item:hover {
      border: 1px dashed #40a0ffb0;
      color: #40a0ffb0;
    }

    .draggable-card-item span {
      display: inline-block;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .page-designer-center {
      border-right: 8px solid #EBEEF5;
      border-left: 8px solid #EBEEF5;
      overflow: hidden;

      .title {
        margin-bottom: 0px;
        padding-right: 10px;
        padding-bottom: 1px;
        border-bottom: 2px solid #DFE4ED
      }

      .el-scrollbar__wrap {
        overflow-x: hidden;
      }
    }

    .page-designer-right {
      background: white;

      .el-tabs__item {
        width: 140px;
        text-align: center;
      }

      .el-tabs__active-bar {
        width: 140px !important;
      }

      .el-tabs__header {
        margin: 0px;
      }

      .el-tabs__content {
        padding: 0px;
      }

      .el-form-item--small.el-form-item {
        margin-bottom: 15px;
      }

      .scroll-box {
        overflow-x: hidden;
        overflow-y: auto;
        padding: 10px;
      }

      .full-line-btn {
        width: 100%;
        margin-top: 10px;
        border: 1px dashed #EBEEF5;
      }
    }
  }
</style>
