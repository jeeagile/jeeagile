<template>
  <div class="process-properties">
    <el-collapse v-model="activeItem" accordion :class="panelFold?'process-properties__fold':'process-properties__unfold'">
      <el-collapse-item v-for="item in activeItemList" :key="item.name" :name="item.name">
        <div slot="title" class="panel-tab__title" @click="handlerCollapse">
          <i :class="item.icon" :title="item.title" @click="handlerActiveItem(item)"/>
          <span>{{ item.title }}</span>
        </div>
        <component :is="item.component" v-if="activeElement" :process-info="processInfo"
                   :process-modeler="processModeler"
                   :active-element="activeElement"
        />
      </el-collapse-item>
    </el-collapse>
    <el-dialog :title="activeTitle" :visible.sync="activeVisible" width="380px" append-to-body destroy-on-close>
      <component :is="activeComponent" v-if="activeElement" :process-info="processInfo"
                 :process-modeler="processModeler"
                 :active-element="activeElement"
      />
      <div slot="footer">
        <el-button size="mini" @click="activeVisible = false">关 闭</el-button>
        <el-button size="mini" type="primary" @click="activeVisible = false">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import processHelper from '../helper/ProcessHelper'
  import propertiesGroup from './properties'

  export default {
    name: 'ProcessPanel',
    props: {
      processModeler: { // 流程Modeler
        type: Object,
        required: true
      },
      processInfo: { // 流程信息
        type: Object,
        required: true
      },
      panelFold: {
        type: Boolean,
        required: true,
        default: true
      }
    },
    data() {
      return {
        activeItem: null,
        activeItemList: null,
        activeVisible: false,
        activeTitle: '基础设置',
        activeComponent: 'base',
        activeElement: null
      }
    },
    watch: {
      panelFold() {
        if (this.panelFold) {
          this.activeItem = 'base'
        } else {
          this.activeItem = null
        }
      }
    },
    mounted() {
      this.initProcessModeler()
      this.handleProcessModeler()
    },
    methods: {
      initProcessModeler() {
        window.processInstances = {
          modeler: this.processModeler,
          modeling: this.processModeler.get('modeling'),
          eventBus: this.processModeler.get('eventBus'),
          bpmnFactory: this.processModeler.get('bpmnFactory'),
          translate: this.processModeler.get('translate')
        }
      },
      handleProcessModeler() {
        // 监听添加跟节点事件
        this.processModeler.on('root.added', e => {
          this.handlerActiveElement(e.element)
        })
        // 监听选择事件
        this.processModeler.on('selection.changed', ({ newSelection }) => {
          this.handlerActiveElement(newSelection[0] || null)
        })
        // 监听修改事件
        this.processModeler.on('element.changed', ({ element }) => {
          if (element && element.id === this.activeElement?.id) {
            this.handlerActiveElement(element)
          }
        })
        // 监听点击事件
        this.processModeler.on('element.click', ({ element }) => {
          this.handlerActiveElement(element)
        })
      },
      /** 处理流程节点 */
      handlerActiveElement(element) {
        if (!element || element.type === 'label') return
        this.activeElement = element
        this.handlerActiveItemList(element)
        this.handlerActiveElementName(element)
      },
      /** 处理属性配置分组 */
      handlerActiveItemList(element) {
        let activeItemList = propertiesGroup[element.type] ?? []
        let eventActiveItemList = []
        if (element.businessObject?.eventDefinitions) {
          eventActiveItemList = propertiesGroup[element.businessObject?.eventDefinitions[0]?.$type]
        }
        if (eventActiveItemList?.length) {
          activeItemList.push(eventActiveItemList)
        }
        if (!this.activeItemList?.length) {
          activeItemList = propertiesGroup['bpmn:Default']
        }
        const customActiveItemList = this.handlerCustomActiveItemList(element)
        // 追加自定义属性分组 将自定义分组放在前边覆盖系统自带属性分组
        const activeItemTempList = customActiveItemList.concat(activeItemList)
        // 对合并的属性分组进行去重
        this.activeItemList = this.uniqueActiveItemList(activeItemTempList)
        if (element.type === 'bpmn:SequenceFlow' && element.source?.type === ('bpmn:StartEvent')) {
          // 过滤掉条件流转
          this.activeItemList = this.activeItemList.filter(item => item.name !== 'condition') ?? []
        }
        // 排序
        this.activeItemList.sort((a, b) => {
          return a.sort - b.sort
        })
      },
      // 获取自定义属性配置分组
      handlerCustomActiveItemList(element) {
        const customProcessProperties = this.processInfo.processProperties ?? {}
        let customActiveItemList = customProcessProperties[element.type] ?? []
        let customEventActiveItemList = []
        if (element.businessObject?.eventDefinitions) {
          customEventActiveItemList = customProcessProperties[element.businessObject?.eventDefinitions[0]?.$type]
        }
        if (customEventActiveItemList?.length) {
          customActiveItemList.push(customEventActiveItemList)
        }
        return customActiveItemList ?? []
      },
      uniqueActiveItemList(activeItemList) {
        const nameMap = new Map()
        return activeItemList.filter((item) => !nameMap.has(item.name) && nameMap.set(item.name, ''))
      },
      handlerActiveElementName(element) {
        this.processInfo.elementName = processHelper.translate(element.type)
        let eventName = ''
        if (element.businessObject?.eventDefinitions) {
          eventName = processHelper.translate(element.businessObject?.eventDefinitions[0]?.$type)
        }
        this.processInfo.elementName = this.processInfo.elementName.replace('{eventName}', eventName)
      },
      /** 点击图标弹出属性配置 */
      handlerActiveItem(activeItem) {
        if (this.panelFold) return
        this.activeVisible = true
        this.activeComponent = activeItem.component
        this.activeTitle = activeItem.title
      },
      /** 处理折叠后点击图标不在展开对应属性配置 */
      handlerCollapse(e) {
        if (this.panelFold) return
        e.stopPropagation()
      }
    }
  }
</script>
<style lang='scss'>

</style>
