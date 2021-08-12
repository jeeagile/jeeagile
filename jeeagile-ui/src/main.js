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
import '@/assets/icons'

import Pagination from '@/components/Pagination'
import RightToolbar from '@/components/RightToolbar'
import directive from '@/directive'
import TreeSelect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {getDictDataByDictType} from '@/api/system/dictData'
import {getConfigByKey} from '@/api/system/config'
import {handleDictLabel, handleTree, resetForm} from '@/utils/agile'
import VueParticles from 'vue-particles'

// 全局组件挂载
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('TreeSelect', TreeSelect)
Vue.use(directive)

Vue.use(VueParticles)

Vue.prototype.resetForm = resetForm
Vue.prototype.handleTree = handleTree
Vue.prototype.handleDictLabel = handleDictLabel
Vue.prototype.getConfigByKey = getConfigByKey
Vue.prototype.getDictDataByDictType = getDictDataByDictType

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

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: (h) => h(App)
}).$mount('#app')
