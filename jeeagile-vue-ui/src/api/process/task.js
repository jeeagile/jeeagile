import { postApi } from '@/utils/axios'

/**
 * 查询可发起流程定义列表
 */
export const selectProcessDefinitionPage = data => postApi('/process/task/selectProcessDefinitionPage', data)

/**
 * 查看流程定义
 */
export const detailProcessDefinition = definitionId => postApi('/process/task/detailProcessDefinition', definitionId)

/**
 * 启动流程定义
 */
export const startProcessInstance = definitionId => postApi('/process/task/startProcessInstance', definitionId)
