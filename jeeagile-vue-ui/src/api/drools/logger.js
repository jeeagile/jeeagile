import { postApi } from '@/utils/axios'

/**
 * 查询规则执行日志
 */
export const selectDroolsLoggerPage = data => postApi('/drools/logger/page', data)

/**
 * 查看规则执行日志详细信息
 */
export const detailDroolsLogger = data => postApi('/drools/logger/detail', data)


/**
 * 查看规则执行日志详细信息
 */
export const droolsStatistic = data => postApi('/drools/logger/statistic', data)
