import { postApi } from '@/utils/axios'

/**
 * 查询字典数据
 */
export const selectDictData = data => postApi('/online/operation/selectDictData', data)

/**
 * 分页查询数据
 */
export const selectPageData = data => postApi('/online/operation/selectPageData', data)

/**
 * 查看数据
 */
export const selectOneData = data => postApi('/online/operation/selectOneData', data)

/**
 * 保存数据
 */
export const saveTableData = data => postApi('/online/operation/saveTableData', data)

/**
 * 更新数据
 */
export const updateTableData = data => postApi('/online/operation/updateTableData', data)

/**
 * 删除数据
 */
export const deleteTableData = data => postApi('/online/operation/deleteTableData', data)
