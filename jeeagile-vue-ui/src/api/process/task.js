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
 * 同意审核
 */
export const approveProcessTask = data => postApi('/process/task/approve', data)

/**
 * 拒绝审核
 */
export const rejectProcessTask = data => postApi('/process/task/reject', data)
