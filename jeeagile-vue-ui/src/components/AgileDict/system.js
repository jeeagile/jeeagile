import Vue from 'vue'
import { AgileBaseDict } from './index'


const SysUserSex = new AgileBaseDict('用户性别', [
  {
    value: '0',
    label: '男',
    symbol: 'MAN'
  },
  {
    value: '1',
    label: '女',
    symbol: 'WOMAN'
  },
  {
    value: '2',
    label: '未知',
    symbol: 'UNKNOWN'
  }
])
Vue.prototype.SysUserSex = SysUserSex

const SysMenuKind = new AgileBaseDict('菜单分类', [
  {
    value: '01',
    label: '路由菜单',
    symbol: 'ROUTER'
  },
  {
    value: '02',
    label: '在线表单',
    symbol: 'ONLINE'
  },
  {
    value: '03',
    label: '工单列表',
    symbol: 'ORDER'
  }
])
Vue.prototype.SysMenuKind = SysMenuKind


export {
  SysUserSex,
  SysMenuKind
}
