import { postApi } from '@/utils/axios'

/**
 * 查询字典列表
 */
export const selectDictPage = data => postApi('/online/dict/page', data)

/**
 * 查询字典列表
 */
export const selectDictList = data => postApi('/online/dict/list', data)

/**
 * 查看字典配置
 */
export const detailDict = dictId => postApi('/online/dict/detail', dictId)

/**
 * 新增字典配置
 */
export const addDict = data => postApi('/online/dict/add', data)

/**
 * 修改字典配置
 */
export const updateDict = data => postApi('/online/dict/update', data)

/**
 * 删除字典配置
 */
export const deleteDict = data => postApi('/online/dict/delete', data)
