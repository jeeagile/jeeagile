import hasRole from './permission/hasRole'
import hasPerm from './permission/hasPerm'

const install = function (Vue) {
  Vue.directive('hasRole', hasRole)
  Vue.directive('hasPerm', hasPerm)
}

if (window.Vue) {
  window.hasRole = hasRole
  window.hasPerm = hasPerm
  window.Vue.use(install)
}
export default install
