import { postApi } from '@/utils/axios'

/**
 * 查询数据模型列表
 */
export const selectOnlineColumnList = data => postApi('/online/column/list', data)

/**
 * 查看数据模型配置
 */
export const detailOnlineColumn = columnId => postApi('/online/column/detail', columnId)

/**
 * 新增数据模型配置
 */
export const addOnlineColumn = data => postApi('/online/column/add', data)

/**
 * 修改数据模型配置
 */
export const updateOnlineColumn = data => postApi('/online/column/update', data)

/**
 * 删除数据模型配置
 */
export const deleteOnlineColumn = data => postApi('/online/column/delete', data)
