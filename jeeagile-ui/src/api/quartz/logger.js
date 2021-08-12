import { postApi } from '@/utils/axios'

/**
 * 查询参数列表
 */
export const selectLoggerPage = data => postApi('/quartz/job/logger/page', data)

/**
 * 删除日志记录
 */
export const deleteLogger = logId => postApi('/quartz/job/logger/delete', logId)

/**
 * 删除所有日志基类
 */
export const clearLogger = () => postApi('/quartz/job/logger/clear')
