import { postApi } from '@/utils/axios'

/**
 * 启动流程定义
 */
export const startProcessInstance = data => postApi('/process/instance/start', data)

/**
 * 查询我的发起
 */
export const selectApplyProcessPage = data => postApi('/process/instance/apply', data)

/**
 * 查看流程实例
 */
export const detailProcessInstance = instanceId => postApi('/process/instance/detail', instanceId)

/**
 * 查看流程实例历史审批
 */
export const detailProcessHistory = instanceId => postApi('/process/instance/history', instanceId)

/**
 * 撤销流程实例
 */
export const cancelProcessInstance = instanceId => postApi('/process/instance/cancel', instanceId)



