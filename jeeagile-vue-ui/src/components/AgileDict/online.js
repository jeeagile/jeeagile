import Vue from 'vue'
import { AgileBaseDict } from './index'

const OnlineDictType = new AgileBaseDict('字典类型', [
  {
    value: '01',
    label: '数据表字典',
    symbol: 'TABLE'
  },
  {
    value: '02',
    label: '系统管理字典',
    symbol: 'SYSTEM'
  },
  {
    value: '99',
    label: '自定义字典',
    symbol: 'CUSTOM'
  }
])
Vue.prototype.OnlineDictType = OnlineDictType

const OnlineFormStatus = new AgileBaseDict('表单状态', [
  {
    value: '01',
    label: '表单信息录入',
    symbol: 'FORM_BASIC'
  },
  {
    value: '02',
    label: '数据模型录入',
    symbol: 'DATA_MODEL'
  },
  {
    value: '03',
    label: '表单页面设计',
    symbol: 'PAGE_DESIGN'
  }
])
Vue.prototype.OnlineFormStatus = OnlineFormStatus

const OnlineFormType = new AgileBaseDict('表单类型', [
  {
    value: '01',
    label: '业务表单',
    symbol: 'BUSINESS'
  },
  {
    value: '02',
    label: '流程表单',
    symbol: 'FLOW'
  }
])
Vue.prototype.OnlineFormType = OnlineFormType

const OnlineTableType = new AgileBaseDict('数据表类型', [
  {
    value: '01',
    label: '数据主表',
    symbol: 'MASTER'
  },
  {
    value: '02',
    label: '一对一从表',
    symbol: 'ONE_TO_ONE'
  },
  {
    value: '03',
    label: '一对多从表',
    symbol: 'ONE_TO_MANY'
  }
])
Vue.prototype.OnlineTableType = OnlineTableType

export {
  OnlineDictType,
  OnlineFormStatus,
  OnlineFormType,
  OnlineTableType
}
