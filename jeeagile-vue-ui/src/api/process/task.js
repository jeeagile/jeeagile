import { postApi } from '@/utils/axios'
/**
 * 查询流程模型列表
 */
export const selectTodoPage = data => postApi('/process/task/todo', data)

