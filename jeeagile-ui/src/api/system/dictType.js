import {postApi} from '@/utils/axios'

/**
 * 分页查询字典类型列表
 */
export const selectDictTypePage = data => postApi('/system/dict/type/page', data)

/**
 * 查询字典类型列表
 */
export const selectDictTypeList = data => postApi('/system/dict/type/list', data)

/**
 * 查看字典类型
 */
export const detailDictType = data => postApi('/system/dict/type/detail', data)


/**
 * 新增字典类型
 */
export const addDictType = data => postApi('/system/dict/type/add', data)

/**
 * 修改字典类型
 */
export const updateDictType = data => postApi('/system/dict/type/update', data)

/**
 * 删除字典类型
 */
export const deleteDictType = data => postApi('/system/dict/type/delete', data)

/**
 * 清理缓存
 */
export const clearDictTypeCache = data => postApi('/system/dict/type/clearCache', data)

