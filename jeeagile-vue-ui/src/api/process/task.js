import { postApi } from '@/utils/axios'

/**
 * 查询我的待办
 */
export const selectTodoPage = data => postApi('/process/task/todo', data)

/**
 * 查询我的已办
 */
export const selectDonePage = data => postApi('/process/task/done', data)

/**
 * 审核同意
 */
export const approveProcessTask = data => postApi('/process/task/approve', data)

/**
 * 审核驳回
 */
export const rejectProcessTask = data => postApi('/process/task/reject', data)

/**
 * 审核拒绝
 */
export const refuseProcessTask = data => postApi('/process/task/refuse', data)
