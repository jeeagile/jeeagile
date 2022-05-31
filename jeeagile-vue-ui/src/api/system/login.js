import { postApi } from '@/utils/axios'

/**
 * 查询参数列表
 */
export const selectLoginPage = data => postApi('/system/login/page', data)

/**
 * 删除日志记录
 */
export const deleteLogin = logId => postApi('/system/login/delete', logId)

/**
 * 删除所有日志基类
 */
export const cleanLogin = () => postApi('/system/login/clear')

/**
 * 导出登录日志
 */
export const exportLogin = data => postApi('/system/login/export', data, { responseType: 'blob' })
