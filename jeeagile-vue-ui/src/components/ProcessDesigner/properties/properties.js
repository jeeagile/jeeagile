import Base from './base'
import Form from './form'
import UserTask from './task/UserTask'
import ReceiveTask from './task/ReceiveTask'
import SendTask from './task/SendTask'
import ScriptTask from './task/ScriptTask'
import Async from './async'
import Button from './button'
import Process from './process'
import Instance from './instance'
import Condition from './condition'
import Listener from './listener'
import Parameter from './parameter'
import Extensions from './extensions'
import Documentation from './documentation'

/** 基本设置 */
export const BaseProperties = {
  name: 'base',
  title: '基本设置',
  icon: 'el-icon-setting',
  sort: 0,
  component: Base
}
/** 流程设置 */
export const ProcessProperties = {
  name: 'process',
  title: '流程设置',
  icon: 'el-icon-set-up',
  sort: 1,
  component: Process
}
/** 用户任务 */
export const UserTaskProperties = {
  name: 'userTask',
  title: '用户设置',
  icon: 'el-icon-user',
  sort: 2,
  component: UserTask
}
/** 接收任务 */
export const ReceiveTaskProperties = {
  name: 'receiveTask',
  title: '接收设置',
  icon: 'el-icon-message',
  sort: 3,
  component: ReceiveTask
}
/** 发送任务 */
export const SendTaskProperties = {
  name: 'sendTask',
  title: '发送设置',
  icon: 'el-icon-message',
  sort: 4,
  component: SendTask
}

/** 脚本任务 */
export const ScriptTaskProperties = {
  name: 'scriptTask',
  title: '脚本设置',
  icon: 'el-icon-tickets',
  sort: 5,
  component: ScriptTask
}
/** 表单设置 */
export const FormProperties = {
  name: 'form',
  title: '表单设置',
  icon: 'el-icon-edit',
  sort: 6,
  component: Form
}
/** 会签配置 */
export const InstanceProperties = {
  name: 'instance',
  title: '会签配置',
  icon: 'el-icon-s-operation',
  sort: 7,
  component: Instance
}
/** 持续异步 */
export const AsyncProperties = {
  name: 'async',
  title: '持续异步',
  icon: 'el-icon-odometer',
  sort: 8,
  component: Async
}
/** 条件流转 */
export const ConditionProperties = {
  name: 'condition',
  title: '条件流转',
  icon: 'el-icon-guide',
  sort: 9,
  component: Condition
}
/** 任务监听 */
export const ListenersProperties = {
  name: 'Listeners',
  title: '监听器',
  icon: 'el-icon-headset',
  sort: 10,
  component: Listener
}
/** 按钮配置 */
export const ButtonProperties = {
  name: 'button',
  title: '按钮配置',
  icon: 'el-icon-coin',
  sort: 12,
  component: Button
}
export const ParameterProperties = {
  name: 'parameter',
  title: '输入/输出',
  icon: 'el-icon-sort',
  sort: 13,
  component: Parameter
}
/** 扩展属性 */
export const ExtensionsProperties = {
  name: 'extensions',
  title: '扩展属性',
  icon: 'el-icon-circle-plus-outline',
  sort: 15,
  component: Extensions
}
/** 元素文档 */
export const DocumentationProperties = {
  name: 'documentation',
  title: '元素文档',
  icon: 'el-icon-document',
  sort: 16,
  component: Documentation
}
/** 协作 属性分组 */
export const CollaborationPropertiesGroup = [
  BaseProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]

/** 流程 属性分组 */
export const ProcessPropertiesGroup = [
  BaseProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 开始 属性分组 */
export const StartEventPropertiesGroup = [
  BaseProperties,
  FormProperties,
  AsyncProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 中间事件 属性分组 */
export const IntermediateThrowEventPropertiesGroup = [
  BaseProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 中间消息捕获事件 属性分组 */
export const IntermediateCatchEventPropertiesGroup = [
  BaseProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]

/** 任务 属性分组 */
export const TaskPropertiesGroup = [
  BaseProperties,
  AsyncProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 发送任务 属性分组 */
export const SendTaskPropertiesGroup = [
  BaseProperties,
  SendTaskProperties,
  AsyncProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 接收任务 属性分组 */
export const ReceiveTaskPropertiesGroup = [
  BaseProperties,
  ReceiveTaskProperties,
  AsyncProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 用户任务 属性分组 */
export const UserTaskPropertiesGroup = [
  BaseProperties,
  FormProperties,
  UserTaskProperties,
  AsyncProperties,
  InstanceProperties,
  ListenersProperties,
  ButtonProperties,
  ParameterProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 手工任务 属性分组 */
export const ManualTaskPropertiesGroup = [
  BaseProperties,
  ListenersProperties,
  ParameterProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 业务规则任务 属性分组 */
export const BusinessRuleTaskPropertiesGroup = [
  BaseProperties,
  ListenersProperties,
  ParameterProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 服务任务 属性分组 */
export const ServiceTaskPropertiesGroup = [
  BaseProperties,
  ListenersProperties,
  ParameterProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 脚本任务 属性分组 */
export const ScriptTaskPropertiesGroup = [
  BaseProperties,
  ScriptTaskProperties,
  ListenersProperties,
  ParameterProperties,
  ExtensionsProperties,
  DocumentationProperties
]

/** 引用流程 属性分组 */
export const CallActivityPropertiesGroup = [
  BaseProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 结束 属性分组 */
export const EndEventPropertiesGroup = [
  BaseProperties,
  ListenersProperties,
  ParameterProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 流程线 属性分组 */
export const SequenceFlowPropertiesGroup = [
  BaseProperties,
  ConditionProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 互斥网关 属性分组 */
export const ExclusiveGatewayPropertiesGroup = [
  BaseProperties,
  AsyncProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 并行网关 属性分组 */
export const ParallelGatewayPropertiesGroup = [
  BaseProperties,
  AsyncProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 包容网关 属性分组 */
export const InclusiveGatewayProperties = [
  BaseProperties,
  AsyncProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 复杂网关 属性分组 */
export const ComplexGatewayProperties = [
  BaseProperties,
  AsyncProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 事件网关 属性分组 */
export const EventBasedGatewayProperties = [
  BaseProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 数据对象 属性分组 */
export const DataObjectReferenceProperties = [
  BaseProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 数据存储 属性分组 */
export const DataStoreReferencePropertiesGroup = [
  BaseProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 子流程 属性分组 */
export const SubProcessPropertiesGroup = [
  BaseProperties,
  ProcessProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 池/参与者 属性分组 */
export const ParticipantPropertiesGroup = [
  BaseProperties,
  ProcessProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]
/** 分组 属性分组 */
export const GroupPropertiesGroup = [
  BaseProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]

/** 默认 属性分组 */
export const DefaultPropertiesGroup = [
  BaseProperties,
  ListenersProperties,
  ExtensionsProperties,
  DocumentationProperties
]

/** 文本解释 属性分组 */
export const TextAnnotationPropertiesGroup = [
  BaseProperties,
  ExtensionsProperties,
  DocumentationProperties
]

/** 文本解释 属性分组 */
export const AssociationPropertiesGroup = [
  BaseProperties,
  ExtensionsProperties,
  DocumentationProperties
]

export default {
  /** 默认 */
  'bpmn:Default': DefaultPropertiesGroup,
  /** 协作 */
  'bpmn:Collaboration': CollaborationPropertiesGroup,
  /** 流程 */
  'bpmn:Process': ProcessPropertiesGroup,
  /** 开始 */
  'bpmn:StartEvent': StartEventPropertiesGroup,
  /** 中间事件 */
  'bpmn:IntermediateThrowEvent': IntermediateThrowEventPropertiesGroup,
  /** 中间消息捕获事件 */
  'bpmn:IntermediateCatchEvent': IntermediateCatchEventPropertiesGroup,
  /** 任务 */
  'bpmn:Task': TaskPropertiesGroup,
  /** 发送任务 */
  'bpmn:SendTask': SendTaskPropertiesGroup,
  /** 接收任务 */
  'bpmn:ReceiveTask': ReceiveTaskPropertiesGroup,
  /** 用户任务 */
  'bpmn:UserTask': UserTaskPropertiesGroup,
  /** 手工任务 */
  'bpmn:ManualTask': ManualTaskPropertiesGroup,
  /** 业务规则任务 */
  'bpmn:BusinessRuleTask': BusinessRuleTaskPropertiesGroup,
  /** 服务任务 */
  'bpmn:ServiceTask': ServiceTaskPropertiesGroup,
  /** 脚本任务 */
  'bpmn:ScriptTask': ScriptTaskPropertiesGroup,
  /** 引用流程 */
  'bpmn:CallActivity': CallActivityPropertiesGroup,
  /** 结束 */
  'bpmn:EndEvent': EndEventPropertiesGroup,
  /** 流程线 */
  'bpmn:SequenceFlow': SequenceFlowPropertiesGroup,
  /** 互斥网关 */
  'bpmn:ExclusiveGateway': ExclusiveGatewayPropertiesGroup,
  /** 并行网关 */
  'bpmn:ParallelGateway': ParallelGatewayPropertiesGroup,
  /** 包容网关 */
  'bpmn:InclusiveGateway': InclusiveGatewayProperties,
  /** 复杂网关 */
  'bpmn:ComplexGateway': ComplexGatewayProperties,
  /** 事件网关 */
  'bpmn:EventBasedGateway': EventBasedGatewayProperties,
  /** 数据对象 */
  'bpmn:DataObjectReference': DataObjectReferenceProperties,
  /** 数据存储 */
  'bpmn:DataStoreReference': DataStoreReferencePropertiesGroup,
  /** 子流程 */
  'bpmn:SubProcess': SubProcessPropertiesGroup,
  /** 池/参与者 */
  'bpmn:Participant': ParticipantPropertiesGroup,
  /** 分组 */
  'bpmn:Group': GroupPropertiesGroup,
  /** 文本解释 */
  'bpmn:TextAnnotation': TextAnnotationPropertiesGroup,
  /** 结合关系 */
  'bpmn:Association': AssociationPropertiesGroup
}
export const ListenerVisible = {
  'bpmn:Collaboration': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:Process': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:StartEvent': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:IntermediateThrowEvent': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:IntermediateCatchEvent': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:Task': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:SendTask': {
    executionVisible: true,
    taskVisible: true
  },
  'bpmn:ReceiveTask': {
    executionVisible: true,
    taskVisible: true
  },
  'bpmn:UserTask': {
    executionVisible: true,
    taskVisible: true
  },
  'bpmn:ManualTask': {
    executionVisible: true,
    taskVisible: true
  },
  'bpmn:BusinessRuleTask': {
    executionVisible: true,
    taskVisible: true
  },
  'bpmn:ServiceTask': {
    executionVisible: true,
    taskVisible: true
  },
  'bpmn:ScriptTask': {
    executionVisible: true,
    taskVisible: true
  },
  'bpmn:CallActivity': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:EndEvent': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:SequenceFlow': {
    executionVisible: true,
    taskVisible: true
  },
  'bpmn:ExclusiveGateway': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:ParallelGateway': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:InclusiveGateway': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:ComplexGateway': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:EventBasedGateway': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:DataObjectReference': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:DataStoreReference': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:SubProcess': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:Participant': {
    executionVisible: true,
    taskVisible: false
  },
  'bpmn:Group': {
    executionVisible: true,
    taskVisible: false
  }
}
export const ParameterVisible = {
  'bpmn:UserTask': {
    inputVisible: true,
    outputVisible: true
  },
  'bpmn:ManualTask': {
    inputVisible: true,
    outputVisible: true
  },
  'bpmn:BusinessRuleTask': {
    inputVisible: true,
    outputVisible: true
  },
  'bpmn:ServiceTask': {
    inputVisible: true,
    outputVisible: true
  },
  'bpmn:ScriptTask': {
    inputVisible: true,
    outputVisible: true
  },
  'bpmn:EndEvent': {
    inputVisible: true,
    outputVisible: false
  }
}
