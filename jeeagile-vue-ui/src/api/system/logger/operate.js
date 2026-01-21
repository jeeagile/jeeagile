import { postApi } from '@/utils/axios'

/**
 * 查询参数列表
 */
export const selectOperateLoggerPage = data => postApi('/system/operate/logger/page', data)

/**
 * 删除日志记录
 */
export const deleteOperateLogger = logId => postApi('/system/operate/logger/delete', logId)

/**
 * 删除所有日志基类
 */
export const clearOperateLogger = () => postApi('/system/operate/logger/clear')

/**
 * 导出操作日志
 */
export const exportOperateLogger = data => postApi('/system/operate/logger/export', data, { responseType: 'blob' })
