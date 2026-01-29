<template>
  <div class="process-view__container">
    <div ref="canvas" class="process-view__canvas">
      <div class="process-view__zoom">
        <el-button-group key="scale-control">
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
      </div>
    </div>
  </div>
</template>

<script>
import BpmnViewer from 'bpmn-js/lib/Viewer'
import MoveCanvasModule from 'diagram-js/lib/navigation/movecanvas'
import ModelingModule from 'bpmn-js/lib/features/modeling'
import ZoomScrollModule from 'diagram-js/lib/navigation/zoomscroll/ZoomScroll'

import defaultDiagram from './default'

export default {
  name: 'ProcessViewer',
  componentName: 'ProcessViewer',
  props: {
    value: String, // xml 字符串
    highLineData: Object // 高亮节点
  },
  data() {
    return {processZoom: 1}
  },
  watch: {
    value: function (newValue) { // 在 xmlString 发生变化时，重新创建，从而绘制流程图
      this.createNewDiagram(newValue)
    }
  },
  mounted() {
    this.initBpmnViewer()
    this.createNewDiagram(this.value)
    this.$once('hook:beforeDestroy', () => {
      if (this.bpmnViewer) this.bpmnViewer.destroy()
      this.$emit('destroy', this.bpmnViewer)
      this.bpmnViewer = null
    })
  },
  methods: {
    initBpmnViewer() {
      if (this.bpmnViewer) return
      this.bpmnViewer = new BpmnViewer({
        container: this.$refs.canvas,
        additionalModules: [
          MoveCanvasModule,
          ModelingModule,
          ZoomScrollModule
        ]
      })
      this.handlerViewCenter()
    },
    getElementColor(data) {
      const colorJson = []
      for (const k in data?.highLine) {
        const par = {
          'name': data.highLine[k],
          'stroke': 'blue'
        }
        colorJson.push(par)
      }
      for (const k in data?.highPoint) {
        const par = {
          'name': data.highPoint[k],
          'stroke': 'blue'
        }
        colorJson.push(par)
      }
      for (const k in data?.handlePoint) {
        const par = {
          'name': data?.handlePoint[k],
          'stroke': 'blue',
          'fill': '#a3d68e'
        }
        colorJson.push(par)
      }

      for (const k in data?.waitingPoint) {
        const par = {
          'name': data.waitingPoint[k],
          'stroke': 'red'
        }
        colorJson.push(par)
      }

      return colorJson
    },
    /* 创建新的流程图 */
    async createNewDiagram(processXml) {
      // 将字符串转换成图显示出来
      let processId = `Process_${new Date().getTime()}`
      let processName = `业务流程_${new Date().getTime()}`
      let xmlString = processXml || defaultDiagram(processId, processName)
      try {
        let {warnings} = await this.bpmnViewer.importXML(xmlString)
        if (warnings && warnings.length) {
          warnings.forEach(warn => console.warn(warn))
        }

        const elementColor = this.getElementColor(this.highLineData)
        let modeling = this.bpmnViewer.get('modeling')
        const elementRegistry = this.bpmnViewer.get('elementRegistry')
        for (const i in elementColor) {
          modeling.setColor(elementRegistry.get(elementColor[i].name), elementColor[i])
        }

      } catch (e) {
        console.error(`[Process Designer Warn]: ${e?.message || e}`)
      }
      this.$nextTick(() => {
        setTimeout(() => {
          this.handlerViewCenter()
        }, 100)
      })
    },
    /** 重置视图居中 */
    handlerViewCenter() {
      try {
        this.handlerZoom(null)
        this.bpmnViewer.get('canvas').zoom('fit-viewport', 'auto')
      } catch (error) {
        console.warn('View center operation failed:', error)
        // 使用安全的默认缩放
        this.bpmnViewer.get('canvas').zoom(1.0)
      }
    },
    /** 视图放大缩小 */
    handlerZoom(zoomStep) {
      if (!zoomStep) {
        this.processZoom = 1.0
      } else {
        const newZoom = this.processZoom + zoomStep
        this.processZoom = Math.max(0.1, Math.min(3.0, newZoom))
      }

      try {
        this.bpmnViewer.get('canvas').zoom(this.processZoom)
      } catch (error) {
        console.warn('Zoom operation failed:', error)
        // 回退到安全的缩放值
        this.bpmnViewer.get('canvas').zoom(1.0)
      }
    }
  }
}
</script>
<style lang='scss'>

.process-view__container {
  flex: 1;
  width: 100%;
  height: 100%;

  .process-view__zoom {
    position: absolute;
    z-index: 9999999;
    min-height: 100px;
    width: 100%;
    height: 100%;
    text-align: center;

    .el-button {
      text-align: center;
    }

    .el-button-group {
      margin: 4px;
      vertical-align: text-top;
    }

    .el-tooltip__popper {
      .el-button {
        width: 100%;
        text-align: left;
        padding-left: 8px;
        padding-right: 8px;
      }

      .el-button:hover {
        background: rgba(64, 158, 255, 0.8);
        color: #ffffff;
      }
    }
  }

  .process-view__canvas {
    flex: 1;
    height: 100%;
    position: relative;
    background: url("data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGRlZnM+PHBhdHRlcm4gaWQ9ImEiIHdpZHRoPSI0MCIgaGVpZ2h0PSI0MCIgcGF0dGVyblVuaXRzPSJ1c2VyU3BhY2VPblVzZSI+PHBhdGggZD0iTTAgMTBoNDBNMTAgMHY0ME0wIDIwaDQwTTIwIDB2NDBNMCAzMGg0ME0zMCAwdjQwIiBmaWxsPSJub25lIiBzdHJva2U9IiNlMGUwZTAiIG9wYWNpdHk9Ii4yIi8+PHBhdGggZD0iTTQwIDBIMHY0MCIgZmlsbD0ibm9uZSIgc3Ryb2tlPSIjZTBlMGUwIi8+PC9wYXR0ZXJuPjwvZGVmcz48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSJ1cmwoI2EpIi8+PC9zdmc+") repeat !important;

    .bjs-powered-by {
      display: none; // 可隐藏BPMN.IO链接
    }
  }
}

.highlight .djs-visual > :nth-child(1) {
  stroke: green !important;
  marker-end: url("#sequenceflow-end-white-green-6h1a1cgkq5r3k7b1lz1yaxf66");
}

</style>
