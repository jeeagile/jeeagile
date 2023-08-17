import { getSysDictDataList } from '@/api/system/common'
import { OnlineDictType } from '@/components/AgileDict/online'
import { selectDictData } from '@/api/online/operation'

/**
 * 获取字典数据
 * @param sender
 * @param onlineDict 字典配置对象
 * @param dictParam 获取字典时传入的参数
 * @returns  字典数据
 */
export async function getOnlineDictData(sender, onlineDict, dictParam) {
  switch(onlineDict.dictType){
    case OnlineDictType.TABLE:
      const filterList = Object.keys(dictParam).map(key => {
        return {
          columnName: key,
          columnValue: dictParam[key]
        }
      })
      let params = {
        dictId: onlineDict.id,
        filterList: filterList
      }
      const dictDataList = await selectDictData(params).then(response => {
        return response.data
      })
      return Promise.resolve(dictDataList)
    case OnlineDictType.SYSTEM:
      if (onlineDict.systemDictType != null) {
        const dictDataList = await getSysDictDataList(onlineDict.systemDictType).then(response => {
          return response.data
        })
        return Promise.resolve(dictDataList)
      } else {
        return Promise.reject(new Error('未知的静态字典！'))
      }
    case OnlineDictType.CUSTOM:
      let dictData = JSON.parse(onlineDict.dictDataJson)
      if (dictData != null && Array.isArray(dictData)) {
        return Promise.resolve(dictData)
      } else {
        return Promise.reject(new Error('获取自定义字典数据错误！')).catch(() => {
        })
      }
    default:
      return Promise.reject(new Error('未知的字典类型！')).catch(() => {
      })
  }
}

/**
 * 从数组中查找某一项
 * @param list 要查找的数组
 * @param value 要查找的节点值
 * @param key 要查找的节点键值
 * @param remove 是否从数组中移除查找到的节点
 * @returns {null|*}
 */
export function findItemFromList(list, value, key, remove = false) {
  if (Array.isArray(list) && list.length > 0 && value != null) {
    for (let i = 0; i < list.length; i++) {
      let item = list[i]
      if (((key == null || key === '') && item === value) || (key != null && item[key] === value)) {
        if (remove) list.splice(i, 1)
        return item
      }
    }
  }
  return null
}
