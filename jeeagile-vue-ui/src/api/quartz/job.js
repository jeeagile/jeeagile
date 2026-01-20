import { postApi } from '@/utils/axios'

/**
 * 查询任务列表
 */
export const selectJobPage = data => postApi('/quartz/job/page', data)

/**
 * 查看任务信息
 */
export const detailJob = configId => postApi('/quartz/job/detail', configId)

/**
 * 新增任务信息
 */
export const addJob = data => postApi('/quartz/job/add', data)

/**
 * 修改任务信息
 */
export const updateJob = data => postApi('/quartz/job/update', data)

/**
 * 删除任务
 */
export const deleteJob = data => postApi('/quartz/job/delete', data)

/**
 * 执行任务
 */
export const executeJob = data => postApi('/quartz/job/execute', data)

/**
 * 修改任务状态
 */
export const changeJobStatus = data => postApi('/quartz/job/changeStatus', data)
