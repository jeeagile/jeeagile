import { postApi } from '@/utils/axios'

/**
 * 查询流程模型列表
 */
export const selectProcessModelPage = data => postApi('/process/model/page', data)

/**
 * 查看流程模型
 */
export const detailProcessModel = modelId => postApi('/process/model/detail', modelId)

/**
 * 新增流程模型
 */
export const addProcessModel = data => postApi('/process/model/add', data)

/**
 * 修改流程模型
 */
export const updateProcessModel = data => postApi('/process/model/update', data)

/**
 * 删除流程模型
 */
export const deleteProcessModel = data => postApi('/process/model/delete', data)
