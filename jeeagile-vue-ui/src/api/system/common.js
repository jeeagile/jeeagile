import {postApi} from '@/utils/axios'

/**
 * 获取系统参数配置
 */
export const getSysConfig = configKey => postApi('/system/common/getSysConfig', configKey)

/**
 * 获取系统参数配置值
 */
export const getSysConfigValue = configKey => postApi('/system/common/getSysConfigValue', configKey)

/**
 * 获取系统字典配置数据
 */
export const getSysDictData = (dictType, dictValue) => postApi('/system/common/getSysDictData', { dictType: dictType, dictValue: dictValue })

/**
 * 获取系统字典配置数据
 */
export const getSysDictDataList = dictType => postApi('/system/common/getSysDictDataList', dictType)
