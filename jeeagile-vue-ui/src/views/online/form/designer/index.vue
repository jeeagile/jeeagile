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
          <el-button class="table-btn delete" type="text" icon="el-icon-delete" @click="resetPageDesigner">重置
          </el-button>
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
        <el-tabs class="tab-box" v-model="activeTabName">
          <el-tab-pane :label="currentWidget == null ? '表单属性' : '组件属性'" name="widget">
            <el-scrollbar style="height:calc(100vh - 210px);">
              <el-row v-if="currentWidget != null" class="scroll-box">
                <el-alert v-if="currentWidget.hasError" :title="currentWidget.errorMessage" type="error"
                          :closable="false" style="margin-bottom: 15px;"/>
                <el-form class="full-width-input" size="small" label-position="right" label-width="90px">
                  <el-form-item label="组件类型">
                    <el-select v-model="currentWidget.widgetType" :disabled="true">
                      <el-option v-for="item in OnlineWidgetType.getList()" :key="item.value"
                                 :label="item.label" :value="item.value"/>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="组件标识">
                    <el-input v-model="currentWidget.variableName" placeholder="" disabled/>
                  </el-form-item>
                  <el-form-item label="标题名">
                    <el-input v-model="currentWidget.showName" placeholder=""/>
                  </el-form-item>
                  <el-form-item label="占位提示"
                                v-if="currentWidget.widgetType !== OnlineWidgetType.RichEditor &&
                  currentWidget.widgetType !== 'Upload' &&
                  currentWidget.widgetKind === 'Form'"
                  >
                    <el-input v-model="currentWidget.placeholder" placeholder=""/>
                  </el-form-item>
                  <el-form-item label="栅格数量"
                                v-if="currentWidget.widgetType !== OnlineWidgetType.Divider &&
                  currentWidget.widgetType !== OnlineWidgetType.Text &&
                  currentWidget.widgetKind !== OnlineWidgetType.Filter &&
                  pageConfig.pageType !== OnlinePageType.QUERY"
                  >
                    <el-slider v-model="currentWidget.span" :min="1" :max="24"/>
                  </el-form-item>
                  <!-- Input属性 -->
                  <el-row v-if="currentWidget.widgetType === OnlineWidgetType.Input">
                    <el-form-item label="输入框类型" v-if="currentWidget.widgetKind !== OnlineWidgetKind.Filter">
                      <el-select v-model="currentWidget.type" placeholder="">
                        <el-option value="text" label="单行文本"/>
                        <el-option value="textarea" label="多行文本"/>
                      </el-select>
                    </el-form-item>
                    <el-form-item label="最小行数"
                                  v-if="currentWidget.widgetType === OnlineWidgetType.Input && currentWidget.widgetType === OnlineWidgetType.Textarea">
                      <el-input-number v-model="currentWidget.minRows"/>
                    </el-form-item>
                    <el-form-item label="最大行数"
                                  v-if="currentWidget.widgetType === OnlineWidgetType.Input && currentWidget.widgetType === OnlineWidgetType.Textarea">
                      <el-input-number v-model="currentWidget.maxRows"/>
                    </el-form-item>
                  </el-row>
                  <!-- Date属性 -->
                  <el-row v-if="currentWidget.widgetType === OnlineWidgetType.Date">
                    <el-form-item label="日期类型">
                      <el-select v-model="currentWidget.type" placeholder="" @change="dateTypeChange">
                        <el-option value="year" label="年（year）"/>
                        <el-option value="month" label="月（month）"/>
                        <el-option value="date" label="日（date）"/>
                        <el-option value="datetime" label="日期时间（datetime）"/>
                      </el-select>
                    </el-form-item>
                    <el-form-item label="显示格式">
                      <el-input v-model="currentWidget.format" placeholder="请输入日期显示格式"/>
                    </el-form-item>
                    <el-form-item label="绑定值格式">
                      <el-input v-model="currentWidget.valueFormat" placeholder="请输入绑定值格式"/>
                    </el-form-item>
                  </el-row>
                  <!-- 数字输入框属性 -->
                  <el-row v-if="currentWidget.widgetType === OnlineWidgetType.NumberInput">
                    <el-form-item label="最小值">
                      <el-input-number v-model="currentWidget.min"/>
                    </el-form-item>
                    <el-form-item label="最大值">
                      <el-input-number v-model="currentWidget.max"/>
                    </el-form-item>
                    <el-form-item label="步长">
                      <el-input-number v-model="currentWidget.step"/>
                    </el-form-item>
                    <el-form-item label="精度">
                      <el-input-number v-model="currentWidget.precision"/>
                    </el-form-item>
                    <el-form-item label="控制按钮">
                      <el-radio-group v-model="currentWidget.controlVisible">
                        <el-radio-button :label="1">显示</el-radio-button>
                        <el-radio-button :label="0">隐藏</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="按钮位置">
                      <el-radio-group v-model="currentWidget.controlPosition">
                        <el-radio-button :label="0">默认</el-radio-button>
                        <el-radio-button :label="1">右侧</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                  </el-row>
                  <!-- 上传组件属性 -->
                  <el-row v-if="currentWidget.widgetType === OnlineWidgetType.Upload">
                    <el-form-item label="文件字段名">
                      <el-input v-model="currentWidget.fileFieldName" placeholder="" disabled/>
                    </el-form-item>
                    <el-form-item label="上传地址">
                      <el-input v-model="currentWidget.actionUrl" placeholder="" readonly/>
                    </el-form-item>
                    <el-form-item label="下载地址">
                      <el-input v-model="currentWidget.downloadUrl" placeholder="" readonly/>
                    </el-form-item>
                  </el-row>
                  <!-- 基础卡片属性 -->
                  <el-row v-if="currentWidget.widgetType === OnlineWidgetType.Card">
                    <el-form-item label="内边距">
                      <el-input-number v-model="currentWidget.padding"/>
                    </el-form-item>
                    <el-form-item label="栅格间距">
                      <el-input-number v-model="currentWidget.gutter"/>
                    </el-form-item>
                    <el-form-item label="卡片高度">
                      <el-input-number v-model="currentWidget.height" placeholder="请输入卡片高度，单位px"/>
                    </el-form-item>
                    <el-form-item label="阴影显示">
                      <el-radio-group v-model="currentWidget.shadow">
                        <el-radio-button label="always">一直显示</el-radio-button>
                        <el-radio-button label="hover">移入显示</el-radio-button>
                        <el-radio-button label="never">永不显示</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                  </el-row>
                  <!-- 分割线属性 -->
                  <el-form-item label="阴影显示" v-if="currentWidget.widgetType === OnlineWidgetType.Divider">
                    <el-radio-group v-model="currentWidget.position">
                      <el-radio-button label="left">居左</el-radio-button>
                      <el-radio-button label="center">居中</el-radio-button>
                      <el-radio-button label="right">居右</el-radio-button>
                    </el-radio-group>
                  </el-form-item>
                  <!-- 表格属性 -->
                  <el-row v-if="currentWidget.widgetType === OnlineWidgetType.Table">
                    <el-form-item class="color-select" label="标题颜色" style="height: 32px;"
                                  v-if="pageConfig.pageType !== OnlinePageType.QUERY">
                      <el-color-picker v-model="currentWidget.titleColor" style="width: 100%;"/>
                    </el-form-item>
                    <el-form-item label="表格高度">
                      <el-input-number v-model="currentWidget.height" placeholder="请输入表格高度，单位px"/>
                    </el-form-item>
                    <el-form-item label="是否分页">
                      <el-switch v-model="currentWidget.pageFlag" key="pageFlag"/>
                    </el-form-item>
                    <el-col :span="24">
                      <el-divider>表格字段</el-divider>
                    </el-col>
                    <el-col :span="24" style="border-top: 1px solid #EBEEF5;">
                      <el-table :data="currentWidget.tableColumnList" :show-header="false" empty-text="请选择绑定字段"
                                key="tableColumn">
                        <el-table-column label="操作" width="45px">
                          <template slot-scope="scope">
                            <el-button class="table-btn delete" type="text" icon="el-icon-remove-outline"
                                       @click="deleteTableColumn(scope.row)"
                            />
                          </template>
                        </el-table-column>
                        <el-table-column label="表格列名" prop="showName" width="100px">
                          <template slot-scope="scope">
                            <el-button class="table-btn" type="text" style="text-decoration: underline;"
                                       @click="editTableColumn(scope.row)">{{scope.row.showName}}
                            </el-button>
                          </template>
                        </el-table-column>
                        <el-table-column label="表格字段">
                          <template slot-scope="scope">
                            <span>{{(scope.row.onlineColumn || {}).columnName}}</span>
                          </template>
                        </el-table-column>
                      </el-table>
                      <el-button class="full-line-btn" icon="el-icon-plus" @click="addTableColumn()">添加表格字段
                      </el-button>
                    </el-col>
                  </el-row>
                  <!-- 文本属性 -->
                  <el-row v-if="currentWidget.widgetType === 'Text'">
                    <el-form-item class="color-select" label="背景颜色" style="height: 32px;">
                      <el-color-picker v-model="currentWidget.backgroundColor" style="width: 100%;"/>
                    </el-form-item>
                    <el-form-item class="color-select" label="文字颜色" style="height: 32px;">
                      <el-color-picker v-model="currentWidget.color" style="width: 100%;"/>
                    </el-form-item>
                    <el-form-item label="文字大小">
                      <el-input-number v-model="currentWidget.fontSize" placeholder="请输入文字大小，单位px"/>
                    </el-form-item>
                    <el-form-item label="文字行高">
                      <el-input-number v-model="currentWidget.lineHeight" placeholder="请输入文字行高，单位px"/>
                    </el-form-item>
                    <el-form-item label="首行缩进">
                      <el-input-number v-model="currentWidget.indent" placeholder="请输入首行缩进，单位em"/>
                    </el-form-item>
                    <el-form-item label="内边距">
                      <el-input-number v-model="currentWidget.padding"/>
                    </el-form-item>
                    <el-form-item label="文本修饰">
                      <el-radio-group v-model="currentWidget.decoration">
                        <el-radio-button label="none">标准</el-radio-button>
                        <el-radio-button label="underline">下划线</el-radio-button>
                        <el-radio-button label="line-through">中划线</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="对齐方式">
                      <el-radio-group v-model="currentWidget.align">
                        <el-radio-button label="left">左对齐</el-radio-button>
                        <el-radio-button label="right">右对齐</el-radio-button>
                        <el-radio-button label="center">中间对齐</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                  </el-row>
                  <!-- 图片属性 -->
                  <el-row v-if="currentWidget.widgetType === OnlineWidgetType.Image">
                    <el-form-item label="图片宽度">
                      <el-input-number v-model="currentWidget.width"/>
                    </el-form-item>
                    <el-form-item label="图片高度">
                      <el-input-number v-model="currentWidget.height"/>
                    </el-form-item>
                    <el-form-item label="显示模式">
                      <el-select v-model="currentWidget.fit" placeholder="">
                        <el-option value="fill" label="充满"/>
                        <el-option value="contain" label="包含"/>
                        <el-option value="cover" label="裁剪"/>
                        <el-option value="none" label="原始尺寸"/>
                      </el-select>
                    </el-form-item>
                    <el-form-item label="图片URL">
                      <el-input v-model="currentWidget.src" placeholder=""/>
                    </el-form-item>
                  </el-row>
                  <!-- 公共属性 -->
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="是否只读" v-if="currentWidget.widgetKind === OnlineWidgetKind.Form">
                        <el-switch v-model="currentWidget.readOnly"/>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="是否禁用"
                                    v-if="currentWidget.widgetType !== OnlineWidgetType.RichEditor && currentWidget.widgetKind === OnlineWidgetKind.Form">
                        <el-switch v-model="currentWidget.disabled"/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <!-- 字典下拉参数设置 -->
                  <el-row v-if="currentWidget.widgetType === OnlineWidgetType.Select && currentWidgetDict">
                    <el-col :span="24">
                      <el-divider>下拉选项配置</el-divider>
                    </el-col>
                    <el-col :span="24">
                      <el-form-item label="数据字典">
                        <el-input v-model="currentWidget.onlineColumn.onlineDict.dictName" readonly/>
                      </el-form-item>
                    </el-col>
                    <el-col :span="24" style="border-top: 1px solid #EBEEF5;"
                            v-if="Array.isArray(currentWidgetDict.paramList) && currentWidgetDict.paramList.length > 0"
                    >
                      <el-table :data="currentWidgetDict.paramList" :show-header="false" key="dictParamTable">
                        <el-table-column label="参数名称" prop="dictParamName" width="100px"/>
                        <el-table-column label="参数值" width="120px">
                          <template slot-scope="scope">
                            <el-button class="table-btn" type="text" style="text-decoration: underline;"
                                       @click="editDictParam(scope.row)">
                          <span v-if="scope.row.paramValueType === OnlineParamValueType.FORM_PARAM">
                            {{(getPageParam(scope.row.dictValue) || {}).columnName}}
                          </span>
                              <span v-else-if="scope.row.paramValueType === OnlineParamValueType.TABLE_COLUMN">
                            {{(getTableColumn(scope.row.dictValue) || {}).columnName}}
                          </span>
                              <span v-else-if="scope.row.paramValueType === OnlineParamValueType.STATIC_DICT">
                            {{getDictValueShowName(scope.row.dictValue)}}
                          </span>
                              <span v-else-if="scope.row.paramValueType === OnlineParamValueType.INPUT_VALUE">
                            {{scope.row.dictValue}}
                          </span>
                              <span v-else>尚未设置参数值</span>
                            </el-button>
                          </template>
                        </el-table-column>
                        <el-table-column label="操作" width="40px">
                          <template slot-scope="scope">
                            <el-button class="table-btn delete" type="text" icon="el-icon-remove-outline"
                                       :disabled="scope.row.dictValue == null" @click="deleteDictParam(scope.row)"
                            />
                          </template>
                        </el-table-column>
                      </el-table>
                    </el-col>
                  </el-row>
                </el-form>
              </el-row>
              <el-row v-if="currentWidget == null" class="scroll-box">
                <el-form class="full-width-input" size="small" label-position="left" label-width="90px">
                  <el-form-item label="表单类别">
                    <el-select v-model="pageConfig.pageKind" placeholder="">
                      <el-option v-for="item in OnlinePageKind.getList()" :key="item.value"
                                 :label="item.label" :value="item.value"/>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="表单类型">
                    <el-input :value="OnlinePageType.getLabel(pageConfig.pageType)" readonly disabled/>
                  </el-form-item>
                  <el-form-item label="标签位置">
                    <el-radio-group v-model="pageConfig.labelPosition">
                      <el-radio-button label="left">居左</el-radio-button>
                      <el-radio-button label="right">居右</el-radio-button>
                      <el-radio-button label="top">顶部</el-radio-button>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="标签宽度">
                    <el-input-number v-model="pageConfig.labelWidth"/>
                  </el-form-item>
                  <el-form-item label="栅格间距">
                    <el-input-number v-model="pageConfig.gutter"/>
                  </el-form-item>
                  <el-form-item label="弹窗宽度" v-if="pageConfig.pageKind === OnlinePageKind.DIALOG">
                    <el-input-number v-model="pageConfig.width"/>
                  </el-form-item>
                  <el-form-item label="弹窗高度" v-if="pageConfig.pageKind === OnlinePageKind.DIALOG">
                    <el-input-number v-model="pageConfig.height"/>
                  </el-form-item>
                </el-form>
              </el-row>
            </el-scrollbar>
          </el-tab-pane>
          <el-tab-pane label="操作管理" name="operation"
                       v-if="currentWidget && currentWidget.widgetType === OnlineWidgetType.Table">
            <el-scrollbar style="height:calc(100vh - 210px);">
              <el-row v-if="currentWidget != null && currentWidget.widgetType === OnlineWidgetType.Table"
                      class="scroll-box">
                <el-form class="full-width-input" size="small" label-position="left" label-width="90px">
                  <el-row>
                    <el-col :span="24">
                      <el-form-item label="操作列宽度">
                        <el-input-number v-model="currentWidget.operationWidth" placeholder="请输入表格高度，单位px"/>
                      </el-form-item>
                    </el-col>
                    <el-col :span="24" style="border-top: 1px solid #EBEEF5;">
                      <el-table :data="currentWidget.operationList" :show-header="false" key="operationTable">
                        <el-table-column label="操作" width="40px">
                          <template slot-scope="scope">
                            <el-button class="table-btn delete" type="text" icon="el-icon-remove-outline"
                                       :disabled="scope.row.builtin" @click="deleteTableOperation(scope.row)"
                            />
                          </template>
                        </el-table-column>
                        <el-table-column label="操作名称" prop="name" width="70px">
                          <template slot-scope="scope">
                            <el-button class="table-btn" type="text" style="text-decoration: underline;"
                                       @click="editTableOperation(scope.row)">{{scope.row.name}}
                            </el-button>
                          </template>
                        </el-table-column>
                        <el-table-column label="是否启动" prop="enabled" width="70px">
                          <template slot-scope="scope">
                            <el-switch v-model="scope.row.enabled"/>
                          </template>
                        </el-table-column>
                        <el-table-column label="操作类型" prop="type" width="80px">
                          <template slot-scope="scope">
                            <el-tag size="mini" effect="dark" :type="scope.row.rowOperation ? 'success' : 'warning'">
                              {{scope.row.rowOperation ? '行内操作' : '表格操作'}}
                            </el-tag>
                          </template>
                        </el-table-column>
                      </el-table>
                      <el-button class="full-line-btn" icon="el-icon-plus" @click="addTableOperation()"
                                 :disabled="pageConfig.pageType === OnlinePageType.ORDER"
                      >
                        添加自定义操作
                      </el-button>
                    </el-col>
                    <el-col :span="24">
                      <el-divider>查询参数</el-divider>
                    </el-col>
                    <el-col :span="24" style="border-top: 1px solid #EBEEF5;">
                      <el-table :data="currentWidget.queryParamList" :show-header="false" key="queryParamTable">
                        <el-table-column label="操作" width="40px">
                          <template slot-scope="scope">
                            <el-button class="table-btn delete" type="text" icon="el-icon-remove-outline"
                                       @click="deleteQueryParam(scope.row)"
                            />
                          </template>
                        </el-table-column>
                        <el-table-column label="参数名称" width="100px">
                          <template slot-scope="scope">
                            <el-tag size="mini" effect="dark"
                                    :type="scope.row.tableType == OnlineTableType.MASTER ? 'success' : 'primary'">
                              {{scope.row.onlineColumn.columnName}}
                            </el-tag>
                          </template>
                        </el-table-column>
                        <el-table-column label="参数值">
                          <template slot-scope="scope">
                            <span v-if="scope.row.paramValueType === OnlineParamValueType.FORM_PARAM">
                              {{(getPageParam(scope.row.dictValue) || {}).columnName}}
                            </span>
                            <span v-else-if="scope.row.paramValueType === OnlineParamValueType.TABLE_COLUMN">
                              {{scope.row.onlineColumn.columnName}}
                            </span>
                            <span v-else-if="scope.row.paramValueType === OnlineParamValueType.STATIC_DICT">
                              {{getDictValueShowName(scope.row.paramValue)}}
                            </span>
                            <span v-else-if="scope.row.paramValueType === OnlineParamValueType.INPUT_VALUE">
                              {{scope.row.paramValue}}
                            </span>
                            <span v-else>尚未设置参数值</span>
                          </template>
                        </el-table-column>

                      </el-table>
                      <el-button class="full-line-btn" icon="el-icon-plus" @click="addQueryParam(null)"
                                 :disabled="pageConfig.pageType === OnlinePageType.ORDER"
                      >
                        添加查询参数
                      </el-button>
                    </el-col>
                  </el-row>
                </el-form>
              </el-row>
            </el-scrollbar>
          </el-tab-pane>
          <el-tab-pane label="表单参数" name="operation" v-if="currentWidget == null">
            <el-row class="scroll-box">
              <el-form size="small" label-position="left" label-width="90px">
                <el-col :span="24" style="border-top: 1px solid #EBEEF5;">
                  <el-table :data="pageConfig.paramList" :show-header="false" key="formParamTable">
                    <el-table-column label="操作" width="45px">
                      <template slot-scope="scope">
                        <el-button class="table-btn delete" type="text" icon="el-icon-remove-outline"
                                   :disabled="scope.row.builtin" @click="deletePageParam(scope.row)"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column label="参数名" prop="columnName"/>
                    <el-table-column label="参数类型" width="100px">
                      <template slot-scope="scope">
                        <el-tag v-if="scope.row.columnPrimary" size="mini" type="warning">主键</el-tag>
                        <el-tag v-if="scope.row.slaveColumn" size="mini" type="primary">关联字段</el-tag>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-col>
              </el-form>
            </el-row>
          </el-tab-pane>
        </el-tabs>
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
  import EditWidgetTableColumn from './editWidgetTableColumn'
  import EditWidgetTableOperation from './editWidgetTableOperation'
  import EditWidgetTableQueryParam from './editWidgetTableQueryParam'
  import EditWidgtDictParam from './editWidgtDictParam'
  import * as SystemStaticDict from '@/components/AgileDict/system'

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
                  // eslint-disable-next-line max-depth
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
          widgetKind: this.onlinePage.pageType === this.OnlinePageType.QUERY ? this.OnlineWidgetKind.Filter : attrs.widgetKind
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
          return column.onlineDict.treeFlag == this.SysYesNo.YES ? { ...DefaultWidgetAttributes.Cascader } : { ...DefaultWidgetAttributes.Select }
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
            queryParamList: []
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
          if (widget.onlineColumn && widget.onlineColumn.onlineDict && !widget.onlineColumn.onlineDict.paramList) {
            if (widget.dictParamList) {
              widget.onlineColumn.onlineDict.paramList = widget.dictParamList
            } else {
              if (widget.onlineColumn.onlineDict.dictParamJson) {
                widget.onlineColumn.onlineDict.paramList = JSON.parse(widget.onlineColumn.onlineDict.dictParamJson)
              }
            }
          }
          if (widget.onlineColumn) this.currentWidgetDict = widget.onlineColumn.onlineDict
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
      /** 日期类型组件 */
      dateTypeChange(type) {
        switch (type) {
        case 'year':
          this.currentWidget.format = 'yyyy'
          this.currentWidget.valueFormat = 'yyyy'
          break
        case 'month':
          this.currentWidget.format = 'yyyy-MM'
          this.currentWidget.valueFormat = 'yyyy-MM'
          break
        case 'date':
          this.currentWidget.format = 'yyyy-MM-dd'
          this.currentWidget.valueFormat = 'yyyy-MM-dd'
          break
        case 'datetime':
          this.currentWidget.format = 'yyyy-MM-dd HH:mm:ss'
          this.currentWidget.valueFormat = 'yyyy-MM-dd HH:mm:ss'
          break
        default:
          this.currentWidget.format = 'yyyy-MM-dd HH:mm:ss'
          this.currentWidget.valueFormat = 'yyyy-MM-dd HH:mm:ss'
          break
        }
      },
      /** 删除表格字段 */
      deleteTableColumn(column) {
        if (column == null) return
        this.$confirm('是否删除此表格列？').then(res => {
          this.currentWidget.tableColumnList = this.currentWidget.tableColumnList.filter(item => {
            return item.columnId !== column.columnId
          })
        })
      },
      /** 编辑表格字段 */
      editTableColumn(column) {
        this.handlerTableColumn(column)
      },
      /** 添加表格字段 */
      addTableColumn() {
        this.handlerTableColumn(null)
      },
      /** 添加、编辑表格字段 */
      handlerTableColumn(column) {
        let maxShowOrder = 0
        this.currentWidget.tableColumnList.forEach(item => {
          maxShowOrder = Math.max(item.showOrder, maxShowOrder)
        })
        maxShowOrder++
        this.$dialog.show(column ? '编辑字段' : '添加字段', EditWidgetTableColumn, {
          area: '450px'
        }, {
          showOrder: maxShowOrder,
          rowData: column,
          tableList: this.getTableWidgetTableList(this.currentWidget.onlineTable),
          usedColumnList: this.currentWidget.tableColumnList.map(item => item.columnId)
        }).then(res => {
          if (column == null) {
            this.currentWidget.tableColumnList.push(res)
          } else {
            this.currentWidget.tableColumnList = this.currentWidget.tableColumnList.map(item => {
              return (item.columnId === column.columnId) ? res : item
            })
          }
          this.currentWidget.tableColumnList = this.currentWidget.tableColumnList.sort((value1, value2) => {
            return value1.showOrder - value2.showOrder
          })
        })
      },
      /** 获取表格组件相关数据表 */
      getTableWidgetTableList(onlineTable) {
        let tableList = []
        if (onlineTable != null) {
          tableList.push(onlineTable)
          if (onlineTable.tableType === this.OnlineTableType.MASTER) {
            this.pageTableList.map(item => {
              if (item.tableType === this.OnlineTableType.ONE_TO_ONE) {
                tableList.push(item)
              }
            })
          }
        }
        return tableList
      },
      /** 删除表格操作按钮 */
      deleteTableOperation(operation) {
        if (operation == null) return
        this.$confirm('是否删除此操作？').then(res => {
          this.currentWidget.operationList = this.currentWidget.operationList.filter(item => {
            return item.id !== operation.id
          })
        })
      },
      /** 编辑表格操作按钮 */
      editTableOperation(operation) {
        this.handlerTableOperation(operation)
      },
      /** 添加表格操作按钮 */
      addTableOperation() {
        this.handlerTableOperation(null)
      },
      /** 添加、编辑表格操作按钮 */
      handlerTableOperation(operation) {
        this.$dialog.show(operation ? '编辑操作' : '新建操作', EditWidgetTableOperation, {
          area: '450px'
        }, {
          rowData: operation,
          pageList: this.onlinePageList.filter(item => item.id !== this.onlinePage.id)
        }).then(res => {
          if (operation == null) {
            let maxId = 0
            this.currentWidget.operationList.forEach(item => {
              maxId = Math.max(maxId, item.id)
            })
            res.id = ++maxId
            this.currentWidget.operationList.push(res)
          } else {
            this.currentWidget.operationList = this.currentWidget.operationList.map(item => {
              return (item.id === operation.id) ? res : item
            })
          }
        })
      },
      /** 删除字典参数 */
      deleteDictParam(param) {
        this.currentWidgetDict.paramList = this.currentWidgetDict.paramList.map(item => {
          if (item.dictParamName === param.dictParamName) {
            item.paramValueType = undefined
            item.dictValue = undefined
          }
          return item
        })
      },
      /** 编辑字典参数 */
      editDictParam(param) {
        let columnList = []
        if (this.getPageMasterTable != null && Array.isArray(this.getPageMasterTable.tableColumnList)) {
          columnList = this.getPageMasterTable.tableColumnList.filter(column => {
            return (this.pageConfig.pageType !== this.OnlinePageType.QUERY || column.filterType !== this.OnlineFilterType.NONE)
          })
        }
        this.$dialog.show('字典参数设置', EditWidgtDictParam, {
          area: '400px'
        }, {
          rowData: param,
          pageParamList: this.pageConfig.paramList,
          columnList: columnList
        }).then(res => {
          this.currentWidgetDict.paramList = this.currentWidgetDict.paramList.map(item => {
            return item.dictParamName === param.dictParamName ? res : item
          })
          this.currentWidgetDict = {
            ...this.currentWidgetDict
          }
        })
      },
      /** 删除查询参数 */
      deleteQueryParam(row) {
        this.$confirm('是否移除此表格过滤参数？').then(res => {
          this.currentWidget.queryParamList = this.currentWidget.queryParamList.filter(param => {
            return param.columnId !== row.columnId
          })
        })
      },
      /** 添加查询参数 */
      addQueryParam() {
        let tableFilterColumnList = []
        if (this.getPageMasterTable != null && Array.isArray(this.getPageMasterTable.tableColumnList)) {
          tableFilterColumnList = this.pageTableList.map(table => {
            return {
              tableId: table.tableId,
              tableName: table.tableName,
              // eslint-disable-next-line multiline-ternary
              columnList: Array.isArray(table.tableColumnList) ? table.tableColumnList.filter(column => {
                return (this.pageConfig.pageType !== this.OnlinePageType.QUERY || column.filterType !== this.OnlineFilterType.NONE)
              }) : []
            }
          })
        }
        this.$dialog.show('添加查询参数', EditWidgetTableQueryParam, {
          area: '450px'
        }, {
          tableList: this.getTableWidgetTableList(this.currentWidget.onlineTable),
          usedColumnList: (this.currentWidget.queryParamList || []).map(item => item.columnId),
          pageParamList: this.pageConfig.paramList,
          tableFilterColumnList: tableFilterColumnList
        }).then(res => {
          if (this.currentWidget.queryParamList == null) this.currentWidget.queryParamList = []
          this.currentWidget.queryParamList.push(res)
        })
      },
      /** 删除表单页面参数 */
      deletePageParam(row) {
        this.$confirm('是否删除此页面参数？').then(res => {
          this.pageConfig.paramList = this.pageConfig.paramList.filter(item => {
            return item.columnName !== row.columnName
          })
        })
      },
      /** 获取表单页面参数 */
      getPageParam(paramName) {
        return findItemFromList(this.pageConfig.paramList, paramName, 'columnName')
      },
      /** 获取字段对应信息 */
      getTableColumn(columnId) {
        for (let i = 0; i < this.pageTableList.length; i++) {
          let table = this.pageTableList[i]
          if (table && Array.isArray(table.tableColumnList)) {
            let column = findItemFromList(table.tableColumnList, columnId, 'id')
            if (column != null) return column
          }
        }
        return null
      },
      /** 获取静态字典展示名称 */
      getDictValueShowName(dictValue) {
        let staticDict = SystemStaticDict[dictValue[0]]
        if (staticDict) {
          return staticDict.dictName + ' / ' + staticDict.getLabel(dictValue[1])
        }
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
      /** 重置表单设计 */
      resetPageDesigner() {
        this.$confirm('是否重置表单组件？', '表单设计', {
          type: 'warning'
        }).then(res => {
          this.widgetVariableNameSet.clear()
          if (this.pageConfig.pageType === this.OnlinePageType.QUERY) {
            this.widgetVariableNameSet.add(this.pageConfig.tableWidget.variableName)
          }
          this.widgetUseCount = []
          this.pageWidgetList = []
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

      .el-scrollbar__wrap {
        overflow-x: hidden;
      }

      .full-line-btn {
        width: 100%;
        margin-top: 10px;
        border: 1px dashed #EBEEF5;
      }
    }
  }
</style>
