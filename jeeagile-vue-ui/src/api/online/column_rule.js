import { postApi } from '@/utils/axios'

/**
 * 查询数据表规则配置
 */
export const selectColumnRuleList = data => postApi('/online/column/rule/columnRuleList', data)

/**
 * 新增数据表规则配置
 */
export const addColumnRule = data => postApi('/online/column/rule/add', data)

/**
 * 修改数据表规则配置
 */
export const updateColumnRule = data => postApi('/online/column/rule/update', data)

/**
 * 删除数据表规则配置
 */
export const deleteColumnRule = data => postApi('/online/column/rule/delete', data)
