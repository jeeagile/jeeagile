import { postApi } from '@/utils/axios'

/**
 * 查询表单列表
 */
export const selectOnlinePageList = data => postApi('/online/page/list', data)

/**
 * 查看表单配置
 */
export const detailOnlinePage = pageId => postApi('/online/page/detail', pageId)

/**
 * 新增表单配置
 */
export const addOnlinePage = data => postApi('/online/page/add', data)

/**
 * 修改表单配置
 */
export const updateOnlinePage = data => postApi('/online/page/update', data)

/**
 * 删除表单配置
 */
export const deleteOnlinePage = data => postApi('/online/page/delete', data)

