import { postApi } from '@/utils/axios'

/**
 * 查询数据模型列表
 */
export const selectOnlineTableList = data => postApi('/online/table/list', data)

/**
 * 查看数据模型配置
 */
export const detailOnlineTable = tableId => postApi('/online/table/detail', tableId)

/**
 * 新增数据模型配置
 */
export const addOnlineTable = data => postApi('/online/table/add', data)

/**
 * 修改数据模型配置
 */
export const updateOnlineTable = data => postApi('/online/table/update', data)

/**
 * 删除数据模型配置
 */
export const deleteOnlineTable = data => postApi('/online/table/delete', data)

/**
 * 添加表字段
 */
export const addOnlineColumn = data => postApi('/online/table/addOnlineColumn', data)

/**
 * 刷新表字段
 */
export const refreshOnlineColumn = data => postApi('/online/table/refreshOnlineColumn', data)
/**
 * 获取表单页面数据表
 */
export const selectPageTableList = data => postApi('/online/table/pageTableList', data)

