import UserTask from './properties/UserTask'

/** 用户任务 */
export const UserTaskProperties = {
  name: 'userTask',
  title: '用户设置',
  icon: 'el-icon-user',
  sort: 2,
  component: UserTask
}


/** 用户任务 属性分组 */
export const UserTaskPropertiesGroup = [
  UserTaskProperties
]

export default {
  /** 用户任务 */
  'bpmn:UserTask': UserTaskPropertiesGroup
}
