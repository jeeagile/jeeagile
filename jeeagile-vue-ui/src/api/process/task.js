import { postApi } from '@/utils/axios'

/**
 * 查询流程模型列表
 */
export const selectTodoPage = data => postApi('/process/task/todo', data)

/**
 * 同意审核
 */
export const approveProcessTask = data => postApi('/process/task/approve', data)

/**
 * 拒绝审核
 */
export const rejectProcessTask = data => postApi('/process/task/reject', data)
