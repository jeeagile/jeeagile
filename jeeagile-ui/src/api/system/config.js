import {postApi} from '@/utils/axios'

/**
 * 查询参数列表
 */
export const selectConfigPage = data => postApi('/system/config/page', data)

/**
 * 查看参数配置
 */
export const detailConfig = configId => postApi('/system/config/detail', configId)

/**
 * 新增参数配置
 */
export const addConfig = data => postApi('/system/config/add', data)

/**
 * 修改参数配置
 */
export const updateConfig = data => postApi('/system/config/update', data)

/**
 * 删除参数配置
 */
export const deleteConfig = data => postApi('/system/config/delete', data)

/**
 * 清理缓存
 */
export const clearConfigCache = data => postApi('/system/config/clearCache', data)

