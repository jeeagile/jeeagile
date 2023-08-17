import arrayToTree from 'array-to-tree'
import { v4 } from 'uuid'

/**
 * 表单重置
 * @param refName
 */
export function resetForm(refName) {
  if (this.$refs[refName]) {
    this.$refs[refName].resetFields()
  }
}

/**
 * 回显数据字典
 * @param data
 * @param value
 * @returns {string}
 */
export function handleDictLabel(data, value) {
  let actions = []
  if (data instanceof Array && data.length > 0) {
    Object.keys(data).some((key) => {
      if (data[key].dictValue === ('' + value)) {
        actions.push(data[key].dictLabel)
        return true
      }
    })
  }
  return actions.join('')
}

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(data, id, parentId, children) {
  id = id || 'id'
  parentId = parentId || 'parentId'
  children = children || 'children'
  return arrayToTree(data, {
    customID: id,
    parentProperty: parentId,
    childrenProperty: children
  })
}

/**
 * 生成uuid
 * @returns {string}
 */
export function getUuid() {
  const uuid = v4()
  return uuid.replace(/[-]/g, '')
}

/**
 *  转驼峰
 */
export function toCamelCase(str) {
  return str.replace(/\_(\w)/g, (all, letter) => {
    return letter.toUpperCase()
  })
}

/**
 * 通过值返回从根节点到指定节点的路径
 * @param treeList
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

  if (treeNode[childKey] !== null && Array.isArray(treeNode[childKey])) {
    for (let i = 0; i < treeNode[childKey].length; i++) {
      let tempNode = findNode(treeNode[childKey][i], list, value, key, childKey)
      if (tempNode) return tempNode
    }
  }

  if (Array.isArray(list)) list.pop()
}
