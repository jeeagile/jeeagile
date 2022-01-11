import { postApi } from '@/utils/axios'

/**
 * 在线用户列表
 */
export const getOnlineUserList = (data) => postApi('/monitor/online/user', data)

/**
 * 用户强退
 */
export const forceLogout = (data) => postApi('/monitor/online/forceLogout', data)

/**
 * 批量用户强退
 */
export const batchLogout = (data) => postApi('/monitor/online/batchLogout', data)
