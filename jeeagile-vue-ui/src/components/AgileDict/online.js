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
    tag: 'primary'
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

const OnlineFieldKind = new AgileBaseDict('字段分类', [
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
Vue.prototype.OnlineFieldKind = OnlineFieldKind

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


const OnlineWidgetType = new AgileBaseDict('组件类型', [
  {
    value: 'Label',
    label: '文本显示',
    symbol: 'Label'
  },
  {
    value: 'Input',
    label: '文本输入框',
    symbol: 'Input'
  },
  {
    value: 'NumberInput',
    label: '数字输入框',
    symbol: 'NumberInput'
  },
  {
    value: 'NumberRange',
    label: '数字范围输入框',
    symbol: 'NumberRange'
  },
  {
    value: 'Switch',
    label: '开关组件',
    symbol: 'Switch'
  },
  {
    value: 'Select',
    label: '下拉选择框',
    symbol: 'Select'
  },
  {
    value: 'Cascader',
    label: '级联选择框',
    symbol: 'Cascader'
  },
  {
    value: 'Date',
    label: '日期选择框',
    symbol: 'Date'
  },
  {
    value: 'DateRange',
    label: '日期范围选择框',
    symbol: 'DateRange'
  },
  {
    value: 'Upload',
    label: '上传组件',
    symbol: 'Upload'
  },
  {
    value: 'RichEditor',
    label: '富文本编辑',
    symbol: 'RichEditor'
  },
  {
    value: 'Divider',
    label: '分割线',
    symbol: 'Divider'
  },
  {
    value: 'Text',
    label: '文本',
    symbol: 'Text'
  },
  {
    value: 'Image',
    label: '图片',
    symbol: 'Image'
  },
  {
    value: 'Table',
    label: '表格组件',
    symbol: 'Table'
  },
  {
    value: 'Block',
    label: '基础块',
    symbol: 'Block'
  },
  {
    value: 'Card',
    label: '卡片组件',
    symbol: 'Card'
  }
])

Vue.prototype.OnlineWidgetType = OnlineWidgetType

const OnlineWidgetKind = new AgileBaseDict('组件类别', [
  {
    value: '01',
    label: '过滤组件',
    symbol: 'Filter'
  },
  {
    value: '02',
    label: '表单组件',
    symbol: 'Form'
  },
  {
    value: '03',
    label: '数据组件',
    symbol: 'Data'
  },
  {
    value: '04',
    label: '容器组件',
    symbol: 'Container'
  }
])
Vue.prototype.OnlineWidgetKind = OnlineWidgetKind

const OnlineOperationType = new AgileBaseDict('操作类型', [
  {
    value: '01',
    name: '新建',
    symbol: 'ADD'
  },
  {
    value: '02',
    name: '编辑',
    symbol: 'EDIT'
  },
  {
    value: '03',
    name: '删除',
    symbol: 'DELETE'
  },
  {
    value: '04',
    name: '导出',
    symbol: 'EXPORT'
  },
  {
    value: '99',
    name: '自定义操作',
    symbol: 'CUSTOM'
  }
])
Vue.prototype.OnlineOperationType = OnlineOperationType

const OnlineParamValueType = new AgileBaseDict('表单页面参数值类型', [
  {
    value: '01',
    label: '表单参数',
    symbol: 'FORM_PARAM'
  },
  {
    value: '02',
    label: '数据字段',
    symbol: 'TABLE_COLUMN'
  },
  {
    value: '03',
    label: '系统静态字典',
    symbol: 'STATIC_DICT'
  },
  {
    value: '04',
    label: '直接输入',
    symbol: 'INPUT_VALUE'
  }
])
Vue.prototype.OnlineParamValueType = OnlineParamValueType

export {
  OnlineDictType,
  OnlineFormStatus,
  OnlineFormType,
  OnlineTableType,
  OnlineFilterType,
  OnlineFieldKind,
  OnlinePageKind,
  OnlinePageType,
  OnlineWidgetType,
  OnlineWidgetKind,
  OnlineOperationType,
  OnlineParamValueType
}
