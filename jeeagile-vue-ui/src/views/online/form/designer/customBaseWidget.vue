<template>
  <el-col :span="widgetConfig.span || 24">
    <template v-if="widgetConfig.widgetKind === OnlineWidgetKind.Form">
      <template v-if="widgetConfig.widgetType === OnlineWidgetType.Label || widgetConfig.readOnly">
        <el-form-item :label="widgetConfig.showName">
          <span v-if="widgetConfig.widgetType === OnlineWidgetType.Label ||
            widgetConfig.widgetType === OnlineWidgetType.NumberInput ||
            widgetConfig.widgetType === OnlineWidgetType.Input ||
            widgetConfig.widgetType === OnlineWidgetType.Date"
          >
            {{value}}
          </span>
          <span v-else-if="widgetConfig.widgetType === OnlineWidgetType.Select">{{getDictValue(value)}}</span>
          <div v-else-if="widgetConfig.widgetType === OnlineWidgetType.RichEditor" v-html="value"/>
          <span v-else-if="widgetConfig.widgetType === OnlineWidgetType.Switch">{{value ? '是' : '否'}}</span>
        </el-form-item>
      </template>
      <template v-else>
        <el-form-item :label="widgetConfig.showName"
                      :prop="(widgetConfig.tableType!==OnlineTableType.MASTER ? (widgetConfig.variableName + '__') : '') + (widgetConfig.column || {}).columnName">
          <el-input v-if="widgetConfig.widgetType === OnlineWidgetType.Input"
                    class="input-item" clearable
                    :type="widgetConfig.type"
                    :autosize="{minRows: widgetConfig.minRows || 1, maxRows: widgetConfig.maxRows || 1}"
                    :disabled="widgetConfig.disabled"
                    :value="value" @input="handlerWidgetInput"
                    :placeholder="widgetConfig.placeholder"
          />
          <el-input-number v-if="widgetConfig.widgetType === OnlineWidgetType.NumberInput"
                           class="input-item" clearable
                           :disabled="widgetConfig.disabled"
                           :min="widgetConfig.min"
                           :max="widgetConfig.max"
                           :step="widgetConfig.step"
                           :precision="widgetConfig.precision"
                           :controls="widgetConfig.controlVisible === 1"
                           :controls-position="widgetConfig.controlPosition === 1 ? 'right' : undefined"
                           :value="value" @input="handlerWidgetInput"
                           :placeholder="widgetConfig.placeholder"

          />
          <el-select v-if="widgetConfig.widgetType === OnlineWidgetType.Select"
                     class="input-item" clearable filterable
                     :disabled="widgetConfig.disabled"
                     :value="value" @input="handlerWidgetInput"
                     :placeholder="widgetConfig.placeholder"
                     :loading="dropdownWidget.loading"
                     @visible-change="(isShow) => dropdownWidget.onVisibleChange(isShow).catch(e => {})"
                     @change="handlerWidgetChange"
          >
            <el-option v-for="item in dropdownWidget.dropdownList" :key="item.dictValue" :value="item.dictValue" :label="item.dictLabel"/>
          </el-select>
          <el-cascader v-if="widgetConfig.widgetType === OnlineWidgetType.Cascader"
                       class="input-item" filterable
                       :options="dropdownWidget.dropdownList"
                       :value="cascaderValue" @input="handlerWidgetInput"
                       :clearable="true" :show-all-levels="false"
                       :placeholder="widgetConfig.placeholder"
                       :props="{value: 'id', label: 'name', checkStrictly: true}"
                       @visible-change="(isShow) => dropdownWidget.onVisibleChange(isShow).catch(e => {})"
                       @change="handlerWidgetChange"
          />
          <el-date-picker v-if="widgetConfig.widgetType === OnlineWidgetType.Date"
                          :format="widgetConfig.format || 'yyyy-MM-dd'"
                          :value-format="widgetConfig.valueFormat || 'yyyy-MM-dd 00:00:00'"
                          class="input-item" clearable :type="widgetConfig.type || 'date'" align="left"
                          :disabled="widgetConfig.disabled"
                          :value="value" @input="handlerWidgetInput"
                          :placeholder="widgetConfig.placeholder"
                          @change="handlerWidgetChange"
                          style="width: 100%;"
          />
          <rich-editor v-if="widgetConfig.widgetType === OnlineWidgetType.RichEditor"
                       class="input-item" :value="value" @input="handlerWidgetInput"/>
          <el-switch v-if="widgetConfig.widgetType === OnlineWidgetType.Switch"
                     class="input-item" :value="value" @input="handlerWidgetInput"/>
        </el-form-item>
      </template>
    </template>
    <!-- 数据展示组件 -->
    <template v-else-if="widgetConfig.widgetKind === OnlineWidgetKind.Data">
      <el-divider v-if="widgetConfig.widgetType === OnlineWidgetType.Divider"
                  :content-position="widgetConfig.position">
        {{widgetConfig.showName}}
      </el-divider>
      <custom-text-widget v-else-if="widgetConfig.widgetType === OnlineWidgetType.Text" :widgetConfig="widgetConfig"/>
      <custom-image-widget v-else-if="widgetConfig.widgetType === OnlineWidgetType.Image" :widgetConfig="widgetConfig"/>
    </template>
    <!-- 容器组件 -->
    <template v-else-if="widgetConfig.widgetKind === OnlineWidgetKind.Container">
      <el-card v-if="widgetConfig.widgetType === OnlineWidgetType.Card"
               class="base-card" :shadow="widgetConfig.shadow"
               :body-style="{padding: widgetConfig.padding ? widgetConfig.padding + 'px' : '0px'}"
               :style="{
          height: (widgetConfig.height != null && widgetConfig.height !=='') ? widgetConfig.height + 'px' : undefined
        }">
        <div slot="header" class="base-card-header">
          <span>{{widgetConfig.showName}}</span>
        </div>
        <el-row :gutter="widgetConfig.gutter">
          <slot></slot>
        </el-row>
      </el-card>
    </template>
  </el-col>
</template>

<script>
  import { DropdownWidget } from '../util/widget'
  import { getOnlineDictData, findItemFromList, findTreeNodePath } from '../util'
  import CustomTextWidget from './customTextWidget'
  import CustomImageWidget from './customImageWidget'

  export default {
    props: {
      pageConfig: {
        type: Object,
        required: true
      },
      widgetConfig: {
        type: Object,
        required: true
      },
      value: {
        type: [String, Number, Date, Object, Array, Boolean]
      },
      getDropdownParams: {
        type: Function
      }
    },
    inject: ['preview'],
    components: {
      CustomTextWidget,
      CustomImageWidget
    },
    data() {
      return {
        cascaderValue: [],
        dropdownWidget: (this.widgetConfig.widgetType === this.OnlineWidgetType.Select || this.widgetConfig.widgetType === this.OnlineWidgetType.Cascader)
          ? new DropdownWidget(this.loadDropdownData, this.widgetConfig.widgetType === this.OnlineWidgetType.Cascader) : undefined
      }
    },
    methods: {
      /** 重置 */
      reset() {
        this.handlerWidgetInput(undefined)
        this.cascaderValue = []
        if (this.dropdownWidget) this.dropdownWidget.dirty = true
      },
      /** 加载下拉选项 */
      loadDropdownData() {
        if (this.preview()) {
          return Promise.resolve([])
        }
        return new Promise((resolve, reject) => {
          if (this.widgetConfig.onlineColumn != null && this.widgetConfig.onlineColumn.onlineDict != null) {
            let params = {}
            let onlineDict = this.widgetConfig.onlineColumn.onlineDict
            if (onlineDict.dictType === this.OnlineDictType.TABLE) {
              params = this.getDropdownParams ? this.getDropdownParams(this.widgetConfig) : {}
            }
            if (params == null) {
              reject()
            } else {
              getOnlineDictData(this, onlineDict, params).then(res => {
                resolve(res)
              }).catch(e => {
                reject(e)
              })
            }
          } else {
            reject()
          }
        })
      },
      getDropdownImpl() {
        return this.dropdownWidget
      },
      onVisibleChange() {
        if (this.dropdownWidget) {
          this.dropdownWidget.onVisibleChange(true).then(res => {
            if (this.widgetConfig.widgetType === this.OnlineWidgetType.Cascader) {
              this.cascaderValue = findTreeNodePath(res, this.value, 'dictValue')
            }
          })
        }
      },
      handlerWidgetInput(value) {
        if (this.widgetConfig.widgetType === this.OnlineWidgetType.Cascader) {
          this.cascaderValue = value
          if (Array.isArray(value) && value.length > 0) {
            this.$emit('input', value[value.length - 1], this.widgetConfig)
          } else {
            this.$emit('input', undefined, this.widgetConfig)
          }
        } else {
          this.$emit('input', value, this.widgetConfig)
        }
      },
      handlerWidgetChange(value) {
        if (this.widgetConfig.widgetType === this.OnlineWidgetType.Cascader) {
          if (Array.isArray(value) && value.length > 0) {
            this.$emit('change', value[value.length - 1], this.widgetConfig)
          } else {
            this.$emit('change', undefined, this.widgetConfig)
          }
        } else {
          this.$emit('change', value, this.widgetConfig)
        }
      },
      getDictValue(dictValue) {
        if (this.dropdownWidget && Array.isArray(this.dropdownWidget.dropdownList)) {
          return (findItemFromList(this.dropdownWidget.dropdownList, dictValue, 'dictValue') || {}).dictLabel
        } else {
          return ''
        }
      }
    }
  }
</script>
<style lang='scss'>
  .input-item {
    width: 100% !important;
  }
</style>
