import { postApi } from '@/utils/axios'

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
