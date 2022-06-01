<template>
  <div class="process-designer">
    <el-container>
      <el-header class="process-designer__header" height="auto">
        <div class="process-designer__logo">
          <a href="https://gitee.com/jeeagile/jeeagile-process-designer" target="_blank">
            <img class="img" src="../../assets/logo/logo.png" alt>
          </a>
        </div>
        <div class="process-designer__tool">
          <el-button-group key="base-tool">
            <el-tooltip class="item" effect="left" content="打开文件" placement="bottom">
              <el-button size="mini" icon="el-icon-folder-opened" @click="$refs.openProcessFile.click()"/>
            </el-tooltip>
            <el-tooltip class="item" effect="left" content="创建新流程" placement="bottom">
              <el-button size="mini" icon="el-icon-circle-plus-outline" @click="handlerCreate"/>
            </el-tooltip>
            <input id="openFile" ref="openProcessFile" type="file" style="display: none" accept=".xml,.bpmn" @change="importProcessFile">
            <el-tooltip effect="light" content="下载bpmn">
              <el-button size="mini" icon="el-icon-download" @click="downloadBpmn()"/>
            </el-tooltip>
            <el-tooltip effect="light" content="下载svg">
              <el-button size="mini" icon="el-icon-picture" @click="downloadSvg()"/>
            </el-tooltip>
            <el-tooltip effect="light" content="预览XML">
              <el-button size="mini" icon="el-icon-view" @click="handlerPreview()"/>
            </el-tooltip>
            <el-tooltip effect="light" content="模拟">
              <el-button size="mini" :icon="processSimulation ? 'el-icon-open' : 'el-icon-turn-off'" :type="processSimulation ? 'success' : ''" @click="handlerSimulation"/>
            </el-tooltip>
            <el-tooltip effect="light" content="校验">
              <el-button size="mini" icon="el-icon-check" @click="handlerValidate"/>
            </el-tooltip>

          </el-button-group>
          <el-button-group key="align-tool">
            <el-tooltip effect="light" content="向左对齐">
              <el-button size="mini" class="align align-left" icon="el-icon-s-data" @click="handlerAlign('left')"/>
            </el-tooltip>
            <el-tooltip effect="light" content="向右对齐">
              <el-button size="mini" class="align align-right" icon="el-icon-s-data" @click="handlerAlign('right')"/>
            </el-tooltip>
            <el-tooltip effect="light" content="向上对齐">
              <el-button size="mini" class="align align-top" icon="el-icon-s-data" @click="handlerAlign('top')"/>
            </el-tooltip>
            <el-tooltip effect="light" content="向下对齐">
              <el-button size="mini" class="align align-bottom" icon="el-icon-s-data" @click="handlerAlign('bottom')"/>
            </el-tooltip>
            <el-tooltip effect="light" content="水平居中">
              <el-button size="mini" class="align align-center" icon="el-icon-s-data" @click="handlerAlign('center')"/>
            </el-tooltip>
            <el-tooltip effect="light" content="垂直居中">
              <el-button size="mini" class="align align-middle" icon="el-icon-s-data" @click="handlerAlign('middle')"/>
            </el-tooltip>
          </el-button-group>
          <el-button-group key="scale-tool">
            <el-tooltip effect="light" content="重置视图居中" placement="bottom">
              <el-button size="mini" icon="el-icon-rank" @click="handlerViewCenter"/>
            </el-tooltip>
            <el-tooltip effect="light" content="放大">
              <el-button size="mini" :disabled="processZoom > 2" icon="el-icon-zoom-in" @click="handlerZoom(0.1)"/>
            </el-tooltip>
            <el-tooltip effect="light" content="实际尺寸">
              <el-button size="mini" icon="el-icon-c-scale-to-original" @click="handlerZoom(0)"/>
            </el-tooltip>
            <el-tooltip effect="light" content="缩小">
              <el-button size="mini" :disabled="processZoom < 0.5" icon="el-icon-zoom-out" @click="handlerZoom(-0.1)"/>
            </el-tooltip>
          </el-button-group>
          <el-button-group key="stack-tool">
            <el-tooltip effect="light" content="撤销">
              <el-button size="mini" :disabled="!processUndo" icon="el-icon-refresh-left" @click="handlerUndo()"/>
            </el-tooltip>
            <el-tooltip effect="light" content="恢复">
              <el-button size="mini" :disabled="!processRedo" icon="el-icon-refresh-right" @click="handlerRedo()"/>
            </el-tooltip>
          </el-button-group>
        </div>
      </el-header>
      <el-container class="process-designer__container">
        <el-main class="process-designer__center">
          <div ref="canvas" class="process-designer__canvas"/>
        </el-main>
        <el-aside class="process-designer__right" :width="panelFold ? '350px':'70px'">
          <div class="panel-title">
            <div v-if="panelFold" class="node-name">{{ processInfo.elementName }}</div>
            <div class="panel-fold" :title="panelFold ? '折叠':'展开'" @click="handlerPanelFold()">
              <i :class="panelFold ? 'el-icon-s-unfold':'el-icon-s-fold'"/>
            </div>
          </div>
          <process-properties v-if="processModeler" :process-modeler="processModeler" :process-info="processInfo" :panel-fold="panelFold"/>
        </el-aside>
      </el-container>
    </el-container>
    <el-dialog class="process-designer__preview" title="预览" width="60%" :visible.sync="previewVisible" append-to-body destroy-on-close>
      <highlightjs :language="previewType" :code="previewCode"/>
    </el-dialog>
  </div>
</template>
<script>
  import ProcessModeler from 'bpmn-js/lib/Modeler'
  // 流程模拟组件
  import processSimulation from 'bpmn-js-token-simulation'
  // 流程缩略图组件
  import processMinimap from 'diagram-js-minimap'
  // 流程校验组件
  import processLint from 'bpmn-js-bpmnlint'
  // 流程校验配置
  import processLintConfig from './config/.bpmnlintrc'
  // 自定义 palette
  import agilePalette from './palette'
  // 自定义 context-pad
  import agileContextPadProvider from './context-pad'
  // 自定义 流程翻译组件
  import agileTranslate from './translate'
  // 自定义 流程扩展组件
  import agileExtension from './extension'
  // 自定义 流程解析文件
  import agileDescriptor from './descriptor'

  import processProperties from './properties'

  import defaultDiagram from './default'
  import { saveAs } from 'file-saver'

  export default {
    name: 'ProcessDesigner',
    components: { processProperties },
    props: {
      processCode: { // 流程编码
        type: String,
        default: `process_${new Date().getTime()}`
      },
      processName: { // 流程名称
        type: String,
        default: `流程_${new Date().getTime()}`
      },
      processDesc: { // 流程描述
        type: String,
        default: null
      },
      i18n: { // 语言
        type: String,
        default: 'zn'
      },
      processPrefix: { // 流程类型 activiti camunda flowable
        type: String,
        default: 'activiti'
      },
      keyboard: { // 快捷健
        type: Boolean,
        default: true
      },
      processXml: { // 流程XML
        type: String,
        default: ''
      },
      processProperties: { // 自定义属性配置项
        type: Object,
        default: () => ({})
      }
    },
    data() {
      return {
        processModeler: null,
        processZoom: 1,
        processRedo: false,
        processUndo: false,
        previewVisible: false,
        previewType: 'xml',
        previewCode: null,
        processSimulation: false,
        panelFold: true,
        processInfo: {
          processCode: this.processCode,
          processName: this.processName,
          processPrefix: this.processPrefix,
          processDesc: this.processDesc,
          processProperties: this.processProperties,
          elementName: null
        }
      }
    },
    computed: {
      additionalModules() {
        const modules = []
        // 流程模拟模块
        modules.push(processSimulation)
        // 流程缩略图模块
        modules.push(processMinimap)
        // 流程校验模块
        modules.push(processLint)
        // palette
        modules.push(agilePalette)
        // context-pad
        modules.push(agileContextPadProvider)
        // 流程翻译组件
        modules.push(agileTranslate(this.i18n))
        // 流程扩展组件
        modules.push(agileExtension(this.processPrefix))

        return modules
      },
      moddleExtensions() {
        const extensions = {}
        // 设置流程解析文件
        extensions[this.processPrefix] = agileDescriptor(this.processPrefix)
        return extensions
      }
    },
    mounted() {
      this.initProcessModeler()
      this.createNewProcess(this.processXml)
      this.monitorWindowsWidth()
    },
    methods: {
      monitorWindowsWidth() {
        window.onresize = () => {
          return (() => {
              if (!this.panelFold) return
              this.panelFold = document.body.clientWidth - 700 > 0
            }
          )()
        }
      },
      /** 初始化流程设计器 */
      initProcessModeler() {
        if (this.processModeler) return
        this.processModeler = new ProcessModeler({
          container: this.$refs.canvas,
          paletteContainer: this.$refs.palette,
          keyboard: this.keyboard ? { bindTo: document } : null,
          linting: { bpmnlint: processLintConfig },
          additionalModules: this.additionalModules,
          moddleExtensions: this.moddleExtensions
        })
        this.initModelListeners()
      },
      /** 注册监听事件 */
      initModelListeners() {
        const eventBus = this.processModeler.get('eventBus')
        // 监听视图改变
        eventBus.on('commandStack.changed', async () => {
          try {
            this.processRedo = this.processModeler.get('commandStack').canRedo()
            this.processUndo = this.processModeler.get('commandStack').canUndo()
            let { xml } = await this.processModeler.saveXML({ format: true })
            this.$emit('input', xml)
            this.$emit('change', xml)
          } catch (e) {
            console.error(`[Process Designer Warn]: ${e.message || e}`)
          }
        })
        // 监听视图缩放变化
        this.processModeler.on('canvas.viewbox.changed', ({ viewbox }) => {
          const { scale } = viewbox
          this.processZoom = Math.floor(scale * 100) / 100
        })
      },
      /** 创建新的流程图 */
      async createNewProcess(processXml) {
        let newProcessXml = processXml || defaultDiagram(this.processCode, this.processName, this.processPrefix)
        try {
          let { warnings } = await this.processModeler.importXML(newProcessXml)
          if (warnings && warnings.length) {
            warnings.forEach(warn => console.warn(warn))
          }
          this.processRedo = false
          this.processUndo = false
        } catch (e) {
          console.error(`[Create New Process Warn]: ${e.message || e}`)
        }
      },
      /** 导入本地流程文件 */
      importProcessFile() {
        if (this.$refs.openProcessFile.files.length < 1) {
          return
        }
        const file = this.$refs.openProcessFile.files[0]
        const reader = new FileReader()
        reader.readAsText(file)
        reader.onload = () => {
          this.createNewProcess(reader.result).then(() => {
              this.handlerViewCenter()
            }
          )
        }
        document.getElementById('openFile').value = null
      },
      /** 创建新流程 */
      handlerCreate() {
        this.$confirm('您确定放弃当前修改创建新流程吗？', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => this.createNewProcess(null).then(() => {
            this.handlerViewCenter()
          }
        ))
      },
      /** 下载BPMN */
      async downloadBpmn() {
        try {
          const { err, xml } = await this.processModeler.saveXML()
          // 读取异常时抛出异常
          if (err) {
            return console.error(err)
          }
          const blob = new Blob([xml], { type: 'application/xml' })
          saveAs(blob, `${this.processInfo.processName}.bpmn`)
        } catch (e) {
          console.error(`[Download Bpmn Warn ]: ${e.message || e}`)
        }
      },
      /** 下载SVG */
      async downloadSvg() {
        try {
          const { err, svg } = await this.processModeler.saveSVG()
          // 读取异常时抛出异常
          if (err) {
            return console.error(err)
          }
          const blob = new Blob([svg], { type: 'application/xml' })
          saveAs(blob, `${this.processInfo.processName}.svg`)
        } catch (e) {
          console.error(`[Download Svg Warn ]: ${e.message || e}`)
        }
      },
      /** 查看流程文件 */
      handlerPreview() {
        this.processModeler.saveXML({ format: true }).then(({ xml }) => {
          this.previewCode = xml
          this.previewType = 'xml'
          this.previewVisible = true
        })
      },
      /** 流程模拟 */
      handlerSimulation() {
        this.processSimulation = !this.processSimulation
        this.processModeler.get('toggleMode').toggleMode()
      },
      /** 流程校验 */
      handlerValidate() {
        this.processModeler.get('linting').toggle()
      },
      /** 重置视图居中 */
      handlerViewCenter() {
        this.handlerZoom(null)
        this.processModeler.get('canvas').zoom('fit-viewport', 'auto')
      },
      /** 视图放大缩小 */
      handlerZoom(zoomStep) {
        this.processZoom = !zoomStep ? 1.0 : this.processZoom + zoomStep
        this.processModeler.get('canvas').zoom(this.processZoom)
      },
      /** 处理元素对齐方式 */
      handlerAlign(align) {
        const alignElements = this.processModeler.get('alignElements')
        const selection = this.processModeler.get('selection')
        const selectedElements = selection.get()
        if (!selectedElements || selectedElements.length <= 1) {
          this.$message.warning('请按住 Shift 键选择多个元素对齐')
          return
        }
        this.$confirm('自动对齐可能造成图形变形，是否继续？', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => alignElements.trigger(selectedElements, align))
      },
      /** 恢复 */
      handlerRedo() {
        this.processModeler.get('commandStack').redo()
      },
      /** 撤销 */
      handlerUndo() {
        this.processModeler.get('commandStack').undo()
      },
      /** 展开 折叠 */
      handlerPanelFold() {
        this.panelFold = !this.panelFold
      }
    }
  }
</script>

