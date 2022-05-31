import { postApi } from '@/utils/axios'

/**
 * 查询表单设计列表
 */
export const selectProcessFormPage = data => postApi('/process/form/page', data)

/**
 * 查询表单设计列表
 */
export const selectProcessFormList = data => postApi('/process/form/list', data)

/**
 * 查看表单设计
 */
export const detailProcessForm = formId => postApi('/process/form/detail', formId)

/**
 * 新增表单设计
 */
export const addProcessForm = data => postApi('/process/form/add', data)

/**
 * 修改表单设计
 */
export const updateProcessForm = data => postApi('/process/form/update', data)

/**
 * 删除表单设计
 */
export const deleteProcessForm = data => postApi('/process/form/delete', data)

/**
 * 保存表单设计
 * @param data
 * @returns {*}
 */
export const saveProcessFormDesigner = data => postApi('/process/form/designer', data)
