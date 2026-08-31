import { postApi } from '@/utils/axios'

/**
 * 分页查询规则引擎 规则配置列表
 */
export const selectDroolsRulePage = data => postApi('/drools/rule/page', data)

/**
 * 查询规则引擎 规则配置列表
 */
export const selectDroolsRuleList = data => postApi('/drools/rule/list', data)

/**
 * 查看规则引擎 规则配置
 */
export const detailDroolsRule = data => postApi('/drools/rule/detail', data)

/**
 * 新增规则引擎 规则配置
 */
export const addDroolsRule = data => postApi('/drools/rule/add', data)

/**
 * 修改规则引擎 规则配置
 */
export const updateDroolsRule = data => postApi('/drools/rule/update', data)

/**
 * 删除规则引擎 规则配置
 */
export const deleteDroolsRule = data => postApi('/drools/rule/delete', data)

/**
 * 更新规则引擎 规则配置 状态
 */
export const changeDroolsRuleStatus = data => postApi('/drools/rule/changeStatus', data)

/**
 * 查看规则
 */
export const detailDroolsRuleInfo = data => postApi('/drools/rule/info', data)

/**
 * 保存规则
 */
export const saveDroolsRuleContent = data => postApi('/drools/rule/saveRuleContent', data)

/**
 * 验证规则
 */
export const validateDroolsRuleContent = data => postApi('/drools/rule/validateRuleContent', data)

/**
 * 规则测试
 */
export const testDroolsRule  = data => postApi('/drools/rule/test', data)
