import { postApi } from '@/utils/axios'

/**
 * 查询参数列表
 */
export const selectLogPage = data => postApi('/logger/operate/page', data)

/**
 * 删除日志记录
 */
export const deleteLog = logId => postApi('/logger/operate/delete', logId)

/**
 * 删除所有日志基类
 */
export const cleanLog = () => postApi('/logger/operate/clear')
