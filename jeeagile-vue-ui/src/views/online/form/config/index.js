import { OnlineWidgetType, OnlineWidgetKind } from '@/components/AgileDict/online'

export const OnlineBaseWidgetList = [
  {
    id: 'card',
    name: 'card',
    icon: 'card',
    label: '基础卡片',
    type: OnlineWidgetType.Card
  },
  {
    id: 'divider',
    name: 'divider',
    icon: 'divider',
    label: '分割线',
    type: OnlineWidgetType.Divider
  },
  {
    id: 'text',
    name: 'text',
    icon: 'text',
    label: '文本',
    type: OnlineWidgetType.Text
  },
  {
    id: 'image',
    name: 'image',
    icon: 'image',
    label: '图片',
    type: OnlineWidgetType.Image
  }
]

export const DefaultPageConfig = {
  pageType: undefined,
  pageKind: undefined,
  gutter: 20,
  labelWidth: 100,
  labelPosition: 'right',
  tableWidget: undefined,
  width: 800,
  height: 300
}

export const DefaultWidgetAttributes = {
  Label: {
    widgetKind: OnlineWidgetKind.Form,
    widgetType: OnlineWidgetType.Label,
    span: 12,
    placeholder: '',
    defaultValue: '',
    readOnly: true
  },
  Input: {
    widgetKind: OnlineWidgetKind.Form,
    widgetType: OnlineWidgetType.Input,
    span: 12,
    type: 'text',
    placeholder: '',
    defaultValue: '',
    minRows: 2,
    maxRows: 2,
    readOnly: false,
    disabled: false
  },
  NumberInput: {
    widgetKind: OnlineWidgetKind.Form,
    widgetType: OnlineWidgetType.NumberInput,
    span: 12,
    defaultValue: 0,
    min: 0,
    max: 100,
    step: 1,
    precision: 0,
    controlVisible: 1,
    controlPosition: 0,
    readOnly: false,
    disabled: false
  },
  NumberRange: {
    widgetKind: OnlineWidgetKind.Filter,
    widgetType: OnlineWidgetType.NumberRange,
    readOnly: false,
    disabled: false
  },
  Switch: {
    widgetKind: OnlineWidgetKind.Form,
    widgetType: OnlineWidgetType.Switch,
    span: 12,
    readOnly: false,
    disabled: false
  },
  Select: {
    widgetKind: OnlineWidgetKind.Form,
    widgetType: OnlineWidgetType.Select,
    span: 12,
    placeholder: ''
  },
  Cascader: {
    widgetKind: OnlineWidgetKind.Form,
    widgetType: OnlineWidgetType.Cascader,
    span: 12,
    placeholder: ''
  },
  Date: {
    widgetKind: OnlineWidgetKind.Form,
    widgetType: OnlineWidgetType.Date,
    span: 12,
    placeholder: '',
    type: 'date',
    format: 'yyyy-MM-dd',
    valueFormat: 'yyyy-MM-dd',
    readOnly: false,
    disabled: false
  },
  DateRange: {
    widgetKind: OnlineWidgetKind.Filter,
    widgetType: OnlineWidgetType.DateRange,
    readOnly: false,
    disabled: false
  },
  RichEditor: {
    widgetKind: OnlineWidgetKind.Form,
    widgetType: OnlineWidgetType.RichEditor,
    span: 24
  },
  Upload: {
    widgetKind: OnlineWidgetKind.Form,
    widgetType: OnlineWidgetType.Upload,
    span: 12,
    isImage: true,
    maxFileCount: undefined,
    fileFieldName: 'uploadFile',
    actionUrl: '',
    downloadUrl: ''
  },
  Divider: {
    widgetKind: OnlineWidgetKind.Data,
    widgetType: OnlineWidgetType.Divider,
    span: 24,
    position: 'center'
  },
  Text: {
    widgetKind: OnlineWidgetKind.Data,
    widgetType: OnlineWidgetType.Text,
    span: 24,
    color: undefined,
    backgroundColor: undefined,
    fontSize: undefined,
    lineHeight: undefined,
    indent: undefined,
    decoration: 'none',
    align: 'left',
    padding: 0
  },
  Image: {
    widgetKind: OnlineWidgetKind.Data,
    widgetType: OnlineWidgetType.Image,
    span: 24,
    src: '',
    height: undefined,
    width: undefined,
    fit: 'fill'
  },
  Table: {
    widgetKind: OnlineWidgetKind.Data,
    widgetType: OnlineWidgetType.Table,
    span: 24,
    supportBottom: 0,
    height: undefined,
    pageFlag: true,
    titleColor: '#409EFF',
    tableColumnList: [],
    operationList: [
      {
        id: '01',
        type: 'ADD',
        name: '新建',
        icon: 'el-icon-plus',
        enabled: true,
        builtin: true,
        rowOperation: false,
        btnType: 'primary',
        plain: false,
        formId: undefined
      },
      {
        id: '02',
        type: 'EDIT',
        name: '编辑',
        icon: 'el-icon-edit',
        enabled: true,
        builtin: true,
        rowOperation: true,
        btnClass: 'table-btn success',
        formId: undefined
      },
      {
        id: '03',
        type: 'DELETE',
        name: '删除',
        icon: 'el-icon-delete',
        enabled: true,
        builtin: true,
        rowOperation: true,
        btnClass: 'table-btn delete',
        formId: undefined
      }
    ],
    operationWidth: 150,
    queryParamList: []
  },
  Card: {
    widgetKind: OnlineWidgetKind.Container,
    widgetType: OnlineWidgetType.Card,
    span: 12,
    padding: 15,
    gutter: 15,
    height: undefined,
    shadow: 'always',
    childWidgetList: []
  }
}
