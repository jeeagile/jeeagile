import Vue from 'vue'
import { AgileBaseDict } from './index'

const OnlineDictType = new AgileBaseDict('字典类型', [
  {
    value: '01',
    label: '数据表字典',
    symbol: 'TABLE',
    tag: 'success'
  },
  {
    value: '02',
    label: '系统管理字典',
    symbol: 'SYSTEM',
    tag: 'primary'
  },
  {
    value: '99',
    label: '自定义字典',
    symbol: 'CUSTOM',
    tag: 'danger'
  }
])
Vue.prototype.OnlineDictType = OnlineDictType

const OnlineFormStatus = new AgileBaseDict('表单状态', [
  {
    value: '01',
    label: '表单信息录入',
    symbol: 'FORM_BASIC',
    tag: 'warning'
  },
  {
    value: '02',
    label: '数据模型录入',
    symbol: 'DATA_MODEL',
    tag: 'primary'
  },
  {
    value: '03',
    label: '表单页面设计',
    symbol: 'PAGE_DESIGN',
    tag: 'success'
  }
])
Vue.prototype.OnlineFormStatus = OnlineFormStatus

const OnlineFormType = new AgileBaseDict('表单类型', [
  {
    value: '01',
    label: '业务表单',
    symbol: 'BUSINESS',
    tag: 'success'
  },
  {
    value: '02',
    label: '流程表单',
    symbol: 'FLOW',
    tag: 'info'
  }
])
Vue.prototype.OnlineFormType = OnlineFormType

const OnlineTableType = new AgileBaseDict('数据表类型', [
  {
    value: '01',
    label: '数据主表',
    symbol: 'MASTER',
    tag: 'success'
  },
  {
    value: '02',
    label: '一对一从表',
    symbol: 'ONE_TO_ONE',
    tag: 'primary'
  },
  {
    value: '03',
    label: '一对多从表',
    symbol: 'ONE_TO_MANY',
    tag: 'warning'
  }
])
Vue.prototype.OnlineTableType = OnlineTableType

const OnlineFilterType = new AgileBaseDict('字段过滤类型', [
  {
    value: '01',
    label: '无过滤',
    symbol: 'NONE'
  },
  {
    value: '02',
    label: '普通过滤',
    symbol: 'EQUAL'
  },
  {
    value: '03',
    label: '范围过滤',
    symbol: 'RANGE'
  },
  {
    value: '04',
    label: '模糊过滤',
    symbol: 'LIKE'
  }
])
Vue.prototype.OnlineFilterType = OnlineFilterType

const OnlineFieldClassify = new AgileBaseDict('字段分类', [
  {
    value: '01',
    label: '文件上传字段',
    symbol: 'UPLOAD_FILE'
  },
  {
    value: '02',
    label: '图片上传字段',
    symbol: 'UPLOAD_IMAGE'
  },
  {
    value: '03',
    label: '富文本字段',
    symbol: 'RICH_TEXT'
  },
  {
    value: '04',
    label: '创建人字段',
    symbol: 'CREATE_USER'
  },
  {
    value: '05',
    label: '创建时间字段',
    symbol: 'CREATE_TIME'
  },
  {
    value: '06',
    label: '更新人字段',
    symbol: 'UPDATE_USER'
  },
  {
    value: '07',
    label: '更新时间字段',
    symbol: 'UPDATE_TIME'
  },
  {
    value: '08',
    label: '逻辑删除字段',
    symbol: 'LOGIC_DELETE'
  }
])
Vue.prototype.OnlineFieldClassify = OnlineFieldClassify

const OnlinePageKind = new AgileBaseDict('表单页面分类', [
  {
    value: '01',
    label: '弹出页面',
    symbol: 'DIALOG',
    tag: 'success'
  },
  {
    value: '02',
    label: '跳转页面',
    symbol: 'JUMP',
    tag: 'primary'
  }
])
Vue.prototype.OnlinePageKind = OnlinePageKind

const OnlinePageType = new AgileBaseDict('表单页面类型', [
  {
    value: '01',
    label: '查询页面',
    symbol: 'QUERY',
    tag: 'success'
  },
  {
    value: '02',
    label: '编辑页面',
    symbol: 'EDIT',
    tag: 'primary'
  },
  {
    value: '03',
    label: '流程页面',
    symbol: 'FLOW',
    tag: 'warning'
  },
  {
    value: '04',
    label: '工单列表',
    symbol: 'ORDER',
    tag: 'info'
  }
])
Vue.prototype.OnlinePageType = OnlinePageType

export {
  OnlineDictType,
  OnlineFormStatus,
  OnlineFormType,
  OnlineTableType,
  OnlineFilterType,
  OnlineFieldClassify,
  OnlinePageKind,
  OnlinePageType
}
