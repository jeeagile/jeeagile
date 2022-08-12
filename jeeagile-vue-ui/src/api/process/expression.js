import { postApi } from '@/utils/axios'

/**
 * 查询流程表达式列表
 */
export const selectProcessExpressionPage = data => postApi('/process/expression/page', data)

/**
 * 查询流程表达式列表
 */
export const selectProcessExpressionList = data => postApi('/process/expression/list', data)

/**
 * 查看流程表达式
 */
export const detailProcessExpression = expressionId => postApi('/process/expression/detail', expressionId)

/**
 * 新增流程表达式
 */
export const addProcessExpression = data => postApi('/process/expression/add', data)

/**
 * 修改流程表达式
 */
export const updateProcessExpression = data => postApi('/process/expression/update', data)

/**
 * 删除流程表达式
 */
export const deleteProcessExpression = data => postApi('/process/expression/delete', data)

