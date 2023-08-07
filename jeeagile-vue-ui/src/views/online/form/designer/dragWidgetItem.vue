<template>
  <el-col :span="widgetConfig.span" class="drag-widget-container"
          :class="{'draggable-item': draggable, 'has-error': widgetConfig.hasError}"
          :style="{width: widgetConfig.widgetKind === OnlineWidgetKind.Filter ? (pageConfig().labelWidth + 200 + 'px') : undefined}"
          @click.stop.native="onClick">
    <i v-if="canDelete" class="el-icon-delete drag-widget-delete" style="color: #F56C6C;" @click.stop="onDeleteClick"/>
    <template v-if="widgetConfig.widgetKind === OnlineWidgetKind.Filter">
      <custom-filter-widget class="drag-widget-item" :widgetConfig="{ ...widgetConfig }"
                            :value="widgetConfig.defaultValue" @input="() => {}"/>
    </template>
    <template v-else>
      <custom-table-widget v-if="widgetConfig.widgetType === OnlineWidgetType.Table"
                           class="drag-widget-item" :pageType="pageConfig().pageType"
                           :widgetConfig="{ ...widgetConfig, span: null }" :dictList="{}"/>
      <custom-base-widget v-else class="drag-widget-item"
                          :pageConfig="pageConfig()"
                          :widgetConfig="{ ...widgetConfig, span: null }"
                          :value="widgetConfig.defaultValue" @input="() => {}">
        <draggable draggable=".draggable-item" :list="widgetConfig.childWidgetList" style="min-height: 50px;"
                   group="componentsGroup" :animation="340"
        >
          <template v-if="Array.isArray(widgetConfig.childWidgetList) && widgetConfig.childWidgetList.length > 0">
            <drag-widget-item v-for="child in widgetConfig.childWidgetList" :key="child.id"
                              :class="{'active-widget': child === current()}"
                              @click="onChildClick"
                              @delete="onChildDeleteClick"
                              :widgetConfig="child"/>
          </template>
        </draggable>
      </custom-base-widget>
    </template>
  </el-col>
</template>

<script>
  import Draggable from 'vuedraggable'
  import CustomBaseWidget from './customBaseWidget'
  import CustomTableWidget from './customTableWidget'
  import CustomFilterWidget from './customFilterWidget'

  export default {
    components: {
      Draggable,
      CustomBaseWidget,
      CustomTableWidget,
      CustomFilterWidget
    },
    inject: ['current', 'isLocked', 'pageConfig'],
    props: {
      widgetConfig: {
        type: Object,
        required: true
      },
      canDelete: {
        type: Boolean,
        default: true
      }
    },
    data() {
      return {
        value: this.widgetConfig.defaultValue
      }
    },
    methods: {
      onClick() {
        this.$emit('click', this.widgetConfig)
      },
      onChildClick(element) {
        this.$emit('click', element)
      },
      onDeleteClick() {
        this.$emit('delete', this.widgetConfig)
      },
      onChildDeleteClick(element) {
        this.widgetConfig.childWidgetList = this.widgetConfig.childWidgetList.filter(item => {
          return item.id !== element.id
        })
        this.$emit('delete', this.widgetConfig, true)
      }
    },
    computed: {
      draggable() {
        return !this.isLocked() || this.widgetConfig.widgetKind !== this.OnlineWidgetKind.Container
      }
    }
  }
</script>

<style scoped>
  .drag-widget-container {
    position: relative;
  }

  .drag-widget-container.active-widget > .drag-widget-item {
    background: #b0d7fd;
    border: 1px dashed #77b9fc;
  }

  .has-error {
    background: #fdd5d5 !important;
    border: 1px dashed #F56C6C !important;
  }

  .drag-widget-item {
    cursor: move;
    border: 1px dashed #e2e0e0;
    padding: 13px 10px;
  }

  .drag-widget-item:hover {
    background: #f3f9ff;
  }

  .has-error .drag-widget-item:hover {
    background: #fdd5d5 !important;
  }

  .drag-widget-delete {
    width: 22px;
    height: 22px;
    top: -10px;
    right: 24px;
    line-height: 22px;
    text-align: center;
    border-radius: 50%;
    font-size: 12px;
    border: 1px solid #F56C6C;
    cursor: pointer;
    z-index: 1;
    position: absolute;
    display: none;
  }

  .drag-widget-container:hover > .drag-widget-delete {
    display: block;
  }
</style>
<style>
  .drag-widget-container .el-form-item {
    margin: 0px !important;
  }
</style>
