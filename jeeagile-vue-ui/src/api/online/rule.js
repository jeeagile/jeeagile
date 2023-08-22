import { postApi } from '@/utils/axios'

/**
 * 查询规则类型列表
 */
export const selectRuleList = data => postApi('/online/rule/list', data)

/**
 * 新增规则配置
 */
export const addOnlineRule = data => postApi('/online/rule/add', data)

/**
 * 修改规则配置
 */
export const updateOnlineRule = data => postApi('/online/rule/update', data)

/**
 * 删除规则配置
 */
export const deleteOnlineRule = data => postApi('/online/rule/delete', data)



