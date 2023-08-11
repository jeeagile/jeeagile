<template>
  <el-form-item :label="widgetConfig.showName" :prop="widgetConfig.variableName">
    <el-input v-if="widgetConfig.widgetType === OnlineWidgetType.Input"
              class="filter-item" clearable
              :type="widgetConfig.type"
              :autosize="{minRows: widgetConfig.minRows || 1, maxRows: widgetConfig.maxRows || 1}"
              :disabled="widgetConfig.disabled"
              :value="value" @input="handlerWidgetInput"
              :placeholder="widgetConfig.placeholder"
    />
    <el-input-number v-if="widgetConfig.widgetType === OnlineWidgetType.NumberInput"
                     class="filter-item" clearable
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
               class="filter-item" clearable filterable
               :disabled="widgetConfig.disabled"
               :value="value" @input="handlerWidgetInput"
               :placeholder="widgetConfig.placeholder"
               :loading="dropdownWidget.loading"
               @visible-change="dropdownWidget.onVisibleChange"
               @change="handlerWidgetChange"
    >
      <el-option v-for="item in dropdownWidget.dropdownList" :key="item.dictValue" :value="item.dictValue" :label="item.dictLabel"/>
    </el-select>
    <el-cascader v-if="widgetConfig.widgetType === OnlineWidgetType.Cascader"
                 class="filter-item" filterable
                 :options="dropdownWidget.dropdownList"
                 :value="cascaderValue" @input="handlerWidgetInput"
                 :clearable="true" :show-all-levels="false"
                 :placeholder="widgetConfig.placeholder"
                 :props="{value: 'id', label: 'name', checkStrictly: true}"
                 @visible-change="(isShow) => dropdownWidget.onVisibleChange(isShow).catch(e => {})"
                 @change="handlerWidgetChange"
    />
    <el-date-picker v-if="widgetConfig.widgetType === OnlineWidgetType.DateRange"
                    class="filter-item" :value="value" @input="handlerWidgetInput"
                    :clearable="true" :allowTypes="['day']" align="left"
                    range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
                    format="yyyy-MM-dd" value-format="yyyy-MM-dd HH:mm:ss"
    />
    <input-number-range v-if="widgetConfig.widgetType === OnlineWidgetType.NumberRange"
                        class="filter-item" :value="value" @input="handlerWidgetInput"
                        :clearable="true" startPlaceholder="最小值" endPlaceholder="最大值"/>
  </el-form-item>
</template>

<script>
  import { DropdownWidget } from '../util/widget'
  import { getOnlineDictData, findTreeNodePath } from '../util'

  export default {
    props: {
      widgetConfig: {
        type: Object,
        required: true
      },
      dictList: {
        type: Object,
        default: () => {
        }
      },
      value: {
        type: [String, Number, Date, Object, Array]
      },
      dropdownParam: {
        type: Function
      }
    },
    inject: ['preview'],
    data() {
      return {
        cascaderValue: [],
        dropdownWidget: (this.widgetConfig.widgetType === this.OnlineWidgetType.Select || this.widgetConfig.widgetType === this.OnlineWidgetType.Cascader)
          ? new DropdownWidget(this.loadDropdownData, this.widgetConfig.widgetType === this.OnlineWidgetType.Cascader) : undefined
      }
    },
    methods: {
      reset() {
        this.handlerWidgetInput(undefined)
        this.cascaderValue = []
        if (this.dropdownWidget) this.dropdownWidget.dirty = true
      },
      handlerWidgetInput(value) {
        if (this.widgetConfig.widgetType === this.OnlineWidgetType.Cascader) {
          if (Array.isArray(value) && value.length > 0) {
            this.cascaderValue = value
            this.$emit('input', value[value.length - 1], this.widgetConfig)
          } else {
            this.$emit('input', undefined, this.widgetConfig)
          }
        } else {
          this.$emit('input', value, this.widgetConfig)
        }
      },
      handlerWidgetChange(value) {
        this.$emit('change', value)
      },
      loadDropdownData() {
        // if (this.preview()) {
        //   return Promise.resolve([])
        // }
        return new Promise((resolve, reject) => {
          if (this.widgetConfig.onlineColumn != null && this.widgetConfig.onlineColumn.onlineDict != null) {
            debugger
            let dropdownParam = {}
            let onlineDict = this.widgetConfig.onlineColumn.onlineDict
            if (onlineDict.dictType === this.OnlineDictType.TABLE) {
              dropdownParam = this.getDropdownParams ? this.getDropdownParams(this.widgetConfig) : {}
            }
            if (dropdownParam == null) {
              resolve([])
            } else {
              getOnlineDictData(this, onlineDict, dropdownParam).then(res => {
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
            // 级联组件获取下拉数据
            if (this.widgetConfig.widgetType === this.OnlineWidgetType.Cascader) {
              this.cascaderValue = findTreeNodePath(res, this.value, 'dictValue')
            }
          })
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
