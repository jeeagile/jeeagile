/**
 * 获取字典数据
 * @param sender
 * @param onlineDict 字典配置对象
 * @param dictParam 获取字典时传入的参数
 * @returns  字典数据
 */
export async function getOnlineDictData(sender, onlineDict, dictParam) {

}

/**
 * 从数组中查找某一项
 * @param {Array} list 要查找的数组
 * @param {String} id 要查找的节点id
 * @param {String} idKey 主键字段名（如果为null则直接比较）
 * @param {Boolean} removeItem 是否从数组中移除查找到的节点
 * @returns {Object} 找到返回节点，没找到返回undefined
 */

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
/**
 * 通过值返回从根节点到指定节点的路径
 * @param treeRoot
 * @param value 要查询的节点的值
 * @param key 主键字段键值
 * @param childKey 子节点字键值
 * @returns {[]|*[]}
 */
export function findTreeNodePath(treeList, value, key = 'id', childKey = 'children') {
  let tempList = []
  for (let i = 0; i < treeList.length; i++) {
    if (findNode(treeList[i], tempList, value, key, childKey)) {
      return tempList
    }
  }
  return []
}

/**
 * 通过值从树中查找节点
 * @param {Array} treeNode 根节点数组
 * @param {*} value 要查找的节点的id
 * @param {*} key 主键字段键值
 * @param {*} childKey 子节点字键值
 */
function findNode(treeNode, list, value, key = 'id', childKey = 'children') {
  if (Array.isArray(list)) list.push(treeNode[key])
  if (treeNode[key] === value) {
    return treeNode
  }

  if (treeNode[childKey] != null && Array.isArray(treeNode[childKey])) {
    for (let i = 0; i < treeNode[childKey].length; i++) {
      let tempNode = findNode(treeNode[childKey][i], value, list, key, childKey)
      if (tempNode) return tempNode
    }
  }

  if (Array.isArray(list)) list.pop()
}
