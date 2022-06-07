import { postApi } from '@/utils/axios'

/**
 * 查询流程定义列表
 */
export const selectProcessDefinitionPage = data => postApi('/process/definition/page', data)

/**
 * 查看流程定义
 */
export const detailProcessDefinition = definitionId => postApi('/process/definition/detail', definitionId)

/**
 * 删除流程定义
 */
export const deleteProcessDefinition = data => postApi('/process/definition/delete', data)

/**
 * 激活流程定义
 */
export const activeProcessDefinition = definitionId => postApi('/process/definition/active', definitionId)

/**
 * 挂起流程定义
 */
export const suspendProcessDefinition = definitionId => postApi('/process/definition/suspend', definitionId)
