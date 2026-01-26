import { postApi } from '@/utils/axios'

/**
 * 查询流程定义列表
 */
export const selectProcessDefinitionPage = data => postApi('/process/definition/page', data)

/**
 * 查看流程定义
 */
export const detailProcessDefinition = definitionId => postApi('/process/definition/info', definitionId)

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

/**
 * 设置为主版本
 */
export const updateMainVersion = definitionId => postApi('/process/definition/updateMainVersion', definitionId)


/**
 * 查询可发起流程定义列表
 */
export const selectMainVersionProcess = data => postApi('/process/definition/selectMainVersion', data)
/**
 * 根据流程ID获取流程定义主版本
 */
export const selectMainProcessDefinition = data => postApi('/process/definition/selectMainProcessDefinition', data)
