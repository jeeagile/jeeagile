<template>
  <div class="page-designer-container">
    <el-container style="height: 100%;">
      <el-aside v-if="onlinePage.pageType!==OnlinePageType.ORDER" class="page-designer-left" style="width: 250px">
        <el-scrollbar>
          <div class="data-model-card" v-if="onlinePage.pageType!==OnlinePageType.QUERY">
            <div class="card-title">布局组件</div>
            <Draggable
              class="draggable-card-box"
              draggable=".draggable-card-item"
              :list="baseWidgetList"
              :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
              :clone="cloneBaseWidget"
              :sort="false"
            >
              <div v-for="baseWidget in baseWidgetList" :key="baseWidget.id"
                   class="draggable-card-item" @click="createBaseWidget(baseWidget)"
                   @dragend="dragendWidget(baseWidget)">
                <svg-icon :icon-class="baseWidget.icon"/>
                <span style="margin-left: 5px;" :title="baseWidget.label">{{baseWidget.label}}</span>
                <el-badge :value="widgetUseCount[baseWidget.id]" class="item"/>
              </div>
            </Draggable>
          </div>
          <div class="data-model-card" v-for="pageTable in pageTableList" :key="pageTable.tableName">
            <div class="card-title">
              <span :title="pageTable.tableName">
                {{pageTable.tableLabel}}
              </span>
              <el-tag v-if="getPageMasterTable !== pageTable" size="mini" style="margin-left: 5px;" effect="dark"
                      :type="OnlineTableType.getTag(pageTable.tableType)">
                {{OnlineTableType.getLabel(pageTable.tableType)}}
              </el-tag>
            </div>
            <Draggable class="draggable-card-box" draggable=".draggable-card-item"
                       :list="getPageTableColumnList(pageTable)"
                       :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
                       :clone="cloneColumnComponent"
                       :sort="false"
            >
              <div class="draggable-card-item"
                   v-if="pageTable.tableType === OnlineTableType.ONE_TO_MANY && getPageMasterTable!== pageTable "
                   style="width: 100%;" @click="createColumnWidget(getPageTableColumnList(pageTable)[0])"
                   @dragend="dragendWidget(getPageTableColumnList(pageTable)[0])">
                <i class="el-icon-bank-card"/>
                <span style="margin-left: 5px;" :title="pageTable.tableLabel + '关联数据'">关联数据</span>
                <el-badge :value="widgetUseCount[getPageTableColumnList(pageTable)[0].id]" class="badge-item"/>
              </div>
              <div v-else class="draggable-card-item" v-for="column in getPageTableColumnList(pageTable)"
                   :key="column.id" @click="createColumnWidget(column)" @dragend="dragendWidget(column)">
                <i class="el-icon-bank-card"/>
                <span style="margin-left: 5px;" :title="column.fieldLabel">{{column.fieldLabel}}</span>
                <el-badge v-if="onlinePage.pageType !== OnlinePageType.QUERY" :value="widgetUseCount[column.id]"
                          class="badge-item"/>
              </div>
            </Draggable>
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
          <el-button class="table-btn warning" type="text" icon="el-icon-check">保存</el-button>
          <el-button class="table-btn success" type="text" icon="el-icon-back" @click="handleBack">返回</el-button>
        </el-row>
      </el-main>
      <el-aside class="page-designer-right" style="width: 280px;overflow: hidden">
      </el-aside>
    </el-container>
  </div>
</template>

<script>
  import Draggable from 'vuedraggable'
  import { OnlineBaseWidgetList, DefaultWidgetAttributes } from '../config'
  import { selectPageTableList } from '@/api/online/table'
  import { selectOnlinePageList } from '@/api/online/page'
  import {
    OnlineFieldClassify,
    OnlineFilterType,
    OnlineTableType,
    OnlineWidgetType
  } from '../../../../components/AgileDict/online'

  export default {
    name: 'PageDesigner',
    components: { Draggable },
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
        pageTableList: [],
        onlinePageList: [],
        pageWidgetList: [],
        widgetUseCount: {},
        currentWidget: null,
        widgetVariableNameSet: new Set(),
        baseWidgetList: OnlineBaseWidgetList
      }
    },
    provide() {
      return {
        current: () => this.currentWidget,
        pageConfig: () => this.pageConfig,
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
        return this.pageTableList.find(item => item.tableId == this.onlinePage.tableId)
      }
    },
    mounted() {
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
            // this.initPageConfig()
          }
        )
      },
      /** 创建基础组件 */
      createBaseWidget(widget) {
        if (this.currentWidget != null && this.currentWidget.widgetType === this.OnlineWidgetType.Card) {
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
      dragendWidget(widget) {
        if (!this.widgetUseCount[widget.id]) {
          this.widgetUseCount[widget.id] = 0
        }
        this.$nextTick(() => {
          this.widgetUseCount[widget.id] = this.widgetUseCount[widget.id] + 1
        })
      },
      /** 克隆基础组件 */
      cloneBaseWidget(widget) {
        let attrs = DefaultWidgetAttributes[widget.type]
        return {
          ...attrs,
          id: new Date().getTime(),
          columnId: widget.value,
          columnName: widget.name,
          columnLabel: widget.label,
          variableName: this.handleWidgetVariableName(widget.name),
          widgetCategory: this.onlinePage.pageType === this.OnlinePageType.QUERY ? 'Filter' : attrs.widgetKind
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
      getColumnDefaultAttributes(column) {
        if (column == null) return {}
        // 查询页面的范围查询
        if (column.filterType === this.OnlineFilterType.RANGE && this.onlinePage.pageType === this.OnlinePageType.QUERY) {
          if (column.fieldType === 'Date') {
            return { ...DefaultWidgetAttributes.DateRange }
          } else {
            return { ...DefaultWidgetAttributes.NumberRange }
          }
        }
        // eslint-disable-next-line default-case
        switch (column.fieldClassify) {
        case this.OnlineFieldClassify.UPLOAD_FILE:
        case this.OnlineFieldClassify.UPLOAD_IMAGE: {
          return {
            ...DefaultWidgetAttributes.Upload,
            isImage: column.fieldClassify === this.OnlineFieldClassify.UPLOAD_IMAGE,
            ...this.buildUploadWidgetUrlInfo(column)
          }
        }
        case this.OnlineFieldClassify.RICH_TEXT:
          return { ...DefaultWidgetAttributes.RichEditor }
        case this.OnlineFieldClassify.CREATE_TIME:
        case this.OnlineFieldClassify.UPDATE_TIME:
          return { ...DefaultWidgetAttributes.Date }
        case this.OnlineFieldClassify.CREATE_USER:
        case this.OnlineFieldClassify.UPDATE_USER:
        case this.OnlineFieldClassify.LOGIC_DELETE:
          return { ...DefaultWidgetAttributes.Label }
        }
        if (column.dictId != null && column.dictId !== '' && column.onlineDict != null) {
          return column.dictInfo.treeFlag ? { ...DefaultWidgetAttributes.Cascader } : { ...DefaultWidgetAttributes.Select }
        }
        // 如果是虚拟字段
        if (column.isVirtualColumn) {
          return {
            ...DefaultWidgetAttributes.Label
          }
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
        if (column.table != null) {
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
          columnId: column.id,
          columnName: column.fieldName,
          columnLabel: column.fieldLabel,
          variableName: this.handleWidgetVariableName(column.fieldName),
          widgetKind: this.onlinePage.pageType === this.OnlinePageType.QUERY ? 'Filter' : attrs.widgetKind
          // onlineColumn: column,
          // onlineTable: column.table
        }
      },
      /** 获取数据表字段 */
      getPageTableColumnList(pageTable) {
        if (pageTable && pageTable.tableColumnList) {
          let temp = [
            ...pageTable.tableColumnList.filter(item => {
              let usedWidget = this.pageWidgetList.find(pageWidget => pageWidget.columnId == item.id)
              return (
                this.onlinePage.pageType !== this.OnlinePageType.QUERY || (!item.isVirtualColumn && item.filterType !== OnlineFilterType.NONE && !usedWidget)
              )
            }).map(item => {
              return {
                ...item,
                tableId: pageTable.tableId
              }
            })
          ]
          if (pageTable && pageTable.tableType === OnlineTableType.ONE_TO_MANY && this.getPageMasterTable !== pageTable) {
            temp.unshift({
              tableId: pageTable.tableId,
              columnName: pageTable.modelName,
              fieldName: pageTable.modelName,
              fieldLabel: pageTable.tableLabel,
              widgetType: 'Table',
              columnList: pageTable.tableColumnList,
              table: pageTable
            })
          }
          return temp
        } else {
          return []
        }
      },
      createColumnWidget(column) {
        if (this.currentWidget != null && this.currentWidget.widgetType === this.OnlineWidgetType.Card) {
          this.currentWidget.childWidgetList.push(this.cloneColumnComponent(column))
        } else {
          this.pageWidgetList.push(this.cloneColumnComponent(column))
        }
        if (!this.widgetUseCount[column.id]) {
          this.widgetUseCount[column.id] = 0
        }
        this.widgetUseCount[column.id] = this.widgetUseCount[column.id] + 1
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

      .data-model-card {
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

    .draggable-card-box {
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
