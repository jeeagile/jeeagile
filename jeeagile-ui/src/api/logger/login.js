import {postApi} from '@/utils/axios'

/**
 * 查询参数列表
 */
export const selectLoginPage = data => postApi('/logger/login/page', data)

/**
 * 删除日志记录
 */
export const deleteLogin = logId => postApi('/logger/login/delete', logId)

/**
 * 删除所有日志基类
 */
export const cleanLogin = () => postApi('/logger/login/clear')
