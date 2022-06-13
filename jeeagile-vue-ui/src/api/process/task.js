import { postApi } from '@/utils/axios'

/**
 * 查询流程列表
 */
export const selectProcessDefinitionPage = data => postApi('/process/task/selectProcessDefinitionPage', data)

/**
 * 查看流程定义
 */
export const detailProcessDefinition = definitionId => postApi('/process/task/detailProcessDefinition', definitionId)
