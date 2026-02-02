import { postApi } from '@/utils/axios'

/**
 * 启动流程定义
 */
export const startProcess = data => postApi('/process/order/start', data)

/**
 * 查询我的发起
 */
export const selectApplyProcessPage = data => postApi('/process/order/apply', data)

/**
 * 查看流程实例
 */
export const detailProcessOrder = orderId => postApi('/process/order/detail', orderId)

/**
 * 查看流程实例历史审批
 */
export const detailProcessHistory = orderId => postApi('/process/order/history', orderId)

/**
 * 撤销流程实例
 */
export const cancelProcessOrder = orderId => postApi('/process/order/cancel', orderId)

/**
 * 分页在线工单列表
 */
export const selectOnlineOrderList = data => postApi('/process/order/selectOnlineOrderList', data)

