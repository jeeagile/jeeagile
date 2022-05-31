/**
 * 操作权限处理
 */
import store from '@/store'

export default {
  inserted(el, binding, vnode) {
    const { value } = binding
    const all_permission = '*:*:*'
    const permList = store.getters && store.getters.userPerm

    if (value && value instanceof Array && value.length > 0) {
      const permissionFlag = value

      const hasPermission = permList.some(permission => {
        return all_permission === permission || permissionFlag.includes(permission)
      })

      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error(`请设置操作权限标签值`)
    }
  }
}
