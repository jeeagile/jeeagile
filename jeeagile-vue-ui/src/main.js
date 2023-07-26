import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import Cookies from 'js-cookie'
import Element from 'element-ui'
import 'normalize.css/normalize.css'

import '@/assets/styles/element-variables.scss'

import '@/assets/styles/index.scss'
import '@/assets/styles/agile.scss'
import '@/assets/styles/process/index.scss'
import '@/assets/icons'

import 'bpmn-js/dist/assets/diagram-js.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css'
import 'bpmn-js-token-simulation/assets/css/bpmn-js-token-simulation.css'
import 'bpmn-js-token-simulation/assets/css/normalize.css'
import 'diagram-js-minimap/assets/diagram-js-minimap.css'
import 'bpmn-js-bpmnlint/dist/assets/css/bpmn-js-bpmnlint.css'

import Pagination from '@/components/Pagination'
import RightToolbar from '@/components/RightToolbar'
import Directive from '@/directive'
import ProcessDesigner from '@/components/ProcessDesigner/ProcessDesigner'
import ProcessView from '@/components/ProcessDesigner/ProcessView'
import TreeSelect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { getSysConfig, getSysConfigValue, getSysDictData, getSysDictDataList } from '@/api/system/common'
import { handleDictLabel, handleTree, resetForm, toCamelCase, getUuid } from '@/utils/agile'
import VueParticles from 'vue-particles'
import { vuePlugin } from 'highlight.js'
import 'highlight.js/styles/atom-one-dark-reasonable.css'
import '@/components/AgileDict'
import '@/components/AgileDict/system'
import '@/components/AgileDict/online'
// 全局组件挂载
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('TreeSelect', TreeSelect)
Vue.component('ProcessDesigner', ProcessDesigner)
Vue.component('ProcessView', ProcessView)
Vue.use(Directive)
Vue.use(VueParticles)


Vue.prototype.resetForm = resetForm
Vue.prototype.handleTree = handleTree
Vue.prototype.handleDictLabel = handleDictLabel
Vue.prototype.getSysDictData = getSysDictData
Vue.prototype.getSysDictDataList = getSysDictDataList
Vue.prototype.getSysConfig = getSysConfig
Vue.prototype.getSysConfigValue = getSysConfigValue
Vue.prototype.uuid = getUuid
Vue.prototype.toCamelCase = toCamelCase

Vue.prototype.messageSuccess = function (message) {
  this.$message({ showClose: true, message: message, type: 'success' })
}

Vue.prototype.messageWarning = function (message) {
  this.$message({ showClose: true, message: message, type: 'warning' })
}

Vue.prototype.messageError = function (message) {
  this.$message({ showClose: true, message: message, type: 'error' })
}

Vue.prototype.messageInfo = function (message) {
  this.$message.info(message)
}

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.use(vuePlugin)

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: (h) => h(App)
}).$mount('#app')
