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
/**
 * 保存流程设计
 */
export const saveProcessDesigner = data => postApi('/process/model/designer', data)
/**
 * 流程发布
 */
export const processDeployment = modelId => postApi('/process/model/deployment', modelId)
