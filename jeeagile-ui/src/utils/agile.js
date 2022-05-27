import arrayToTree from 'array-to-tree'

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
      if (data[key].dictValue == ('' + value)) {
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
