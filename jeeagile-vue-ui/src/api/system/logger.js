import {postApi} from '@/utils/axios'

/**
 * 查询参数列表
 */
export const selectLoggerPage = data => postApi('/system/logger/page', data)

/**
 * 删除日志记录
 */
export const deleteLogger = logId => postApi('/system/logger/delete', logId)

/**
 * 删除所有日志基类
 */
export const clearLogger = () => postApi('/system/logger/clear')

/**
 * 导出操作日志
 */
export const exportLogger = data => postApi('/system/logger/export', data, { responseType: 'blob' })
