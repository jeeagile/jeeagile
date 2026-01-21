import { postApi } from '@/utils/axios'

/**
 * 查询参数列表
 */
export const selectLoginLoggerPage = data => postApi('/system/login/logger/page', data)

/**
 * 删除日志记录
 */
export const deleteLoginLogger = logId => postApi('/system/login/logger/delete', logId)

/**
 * 删除所有日志基类
 */
export const cleanLoginLogger = () => postApi('/system/login/logger/clear')

/**
 * 导出登录日志
 */
export const exportLoginLogger = data => postApi('/system/login/logger/export', data, { responseType: 'blob' })
