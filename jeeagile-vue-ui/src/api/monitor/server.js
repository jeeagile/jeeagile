import {postApi} from '@/utils/axios'

/**
 * 获取服务器监控信息
 */
export const getServerInfo = () => postApi('/monitor/server/info')
