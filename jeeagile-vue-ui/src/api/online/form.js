import { postApi } from '@/utils/axios'

/**
 * 查询表单列表
 */
export const selectOnlineFormPage = data => postApi('/online/form/page', data)

/**
 * 查看表单配置
 */
export const detailOnlineForm = formId => postApi('/online/form/detail', formId)

/**
 * 新增表单配置
 */
export const addOnlineForm = data => postApi('/online/form/add', data)

/**
 * 修改表单配置
 */
export const updateOnlineForm = data => postApi('/online/form/update', data)

/**
 * 删除表单配置
 */
export const deleteOnlineForm = data => postApi('/online/form/delete', data)

/**
 * 修改发布状态
 */
export const publishOnlineForm = data => postApi('/online/form/publish', data)

/**
 * 修改表单状态
 */
export const changeFormStatus = data => postApi('/online/form/changeFormStatus', data)

/**
 * 加载表单
 */
export const formPageRender = data => postApi('/online/form/render', data)
