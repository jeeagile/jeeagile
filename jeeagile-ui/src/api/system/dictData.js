import {postApi} from '@/utils/axios'

/**
 * 查询字典数据列表
 */
export const selectDictDataPage = data => postApi('/system/dict/data/selectPage', data)

/**
 * 查询字典数据列表
 */
export const selectDictDataList = data => postApi('/system/dict/data/selectList', data)

/**
 * 查看字典数据
 */
export const detailDictData = data => postApi('/system/dict/data/detail', data)

/**
 * 新增字典数据
 */
export const addDictData = data => postApi('/system/dict/data/add', data)

/**
 * 修改字典数据
 */
export const updateDictData = data => postApi('/system/dict/data/update', data)

/**
 * 删除字典数据
 */
export const deleteDictData = data => postApi('/system/dict/data/delete', data)

/**
 * 根据字典类型获取字典数据列表
 */
export const getDictDataByDictType = dictType => postApi('/system/dict/data/getDictDataByDictType', dictType)

