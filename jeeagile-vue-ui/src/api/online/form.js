import { postApi } from '@/utils/axios'

/**
 * 查询表单列表
 */
export const selectFormPage = data => postApi('/online/form/page', data)

/**
 * 查看表单配置
 */
export const detailForm = formId => postApi('/online/form/detail', formId)

/**
 * 新增表单配置
 */
export const addForm = data => postApi('/online/form/add', data)

/**
 * 修改表单配置
 */
export const updateForm = data => postApi('/online/form/update', data)

/**
 * 删除表单配置
 */
export const deleteForm = data => postApi('/online/form/delete', data)

/**
 * 修改发布状态
 */
export const changePublishStatus = data => postApi('/online/form/publish', data)
