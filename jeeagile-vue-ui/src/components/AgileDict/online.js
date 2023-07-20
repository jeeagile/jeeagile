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
export {
  OnlineDictType
}
