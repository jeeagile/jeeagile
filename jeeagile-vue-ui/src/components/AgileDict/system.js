import Vue from 'vue'
import { AgileBaseDict } from './index'

const SysUserStatus = new AgileBaseDict('用户状态', [
  {
    value: 0,
    label: '启用',
    symbol: 'ENABLE'
  },
  {
    value: 1,
    label: '停用',
    symbol: 'DISABLE'
  }
])
Vue.prototype.SysUserStatus = SysUserStatus
const SysUserSex = new AgileBaseDict('用户性别', [
  {
    value: 0,
    label: '男',
    symbol: 'MAN'
  },
  {
    value: 1,
    label: '女',
    symbol: 'WOMAN'
  },
  {
    value: 2,
    label: '未知',
    symbol: 'UNKNOWN'
  }
])
Vue.prototype.SysUserSex = SysUserSex

const SysYesNo = new AgileBaseDict('系统是否', [
  {
    value: '0',
    label: '否',
    symbol: 'NO'
  },
  {
    value: '1',
    label: '是',
    symbol: 'YES'
  }
])
Vue.prototype.SysYesNo = SysYesNo

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

const SysPublishStatus = new AgileBaseDict('发布状态', [
  {
    value: '01',
    label: '已发布',
    symbol: 'PUBLISHED'
  },
  {
    value: '02',
    label: '未发布',
    symbol: 'UNPUBLISHED'
  }
])
Vue.prototype.SysPublishStatus = SysPublishStatus

const SysSwitchStatus = new AgileBaseDict('开关状态', [
  {
    value: '0',
    label: '停用',
    symbol: 'DISABLE'
  },
  {
    value: '1',
    label: '启用',
    symbol: 'ENABLE'
  }
])
Vue.prototype.SysSwitchStatus = SysSwitchStatus

const SysNormalDisable = new AgileBaseDict('正常停用', [
  {
    value: '0',
    label: '正常',
    symbol: 'NORMAL'
  },
  {
    value: '1',
    label: '停用',
    symbol: 'DISABLE'
  }
])
Vue.prototype.SysNormalDisable = SysNormalDisable


const SysSuccessFail = new AgileBaseDict('成功失败', [
  {
    value: 0,
    label: '成功',
    symbol: 'SUCCESS'
  },
  {
    value: 1,
    label: '失败',
    symbol: 'FAIL'
  }
])
Vue.prototype.SysSuccessFail = SysSuccessFail
export {
  SysUserStatus,
  SysUserSex,
  SysYesNo,
  SysPublishStatus,
  SysMenuKind,
  SysSwitchStatus,
  SysNormalDisable,
  SysSuccessFail
}
