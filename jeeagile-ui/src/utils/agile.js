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
 * @param {*} rootId 根Id 默认 0
 */
export function handleTree(data, id, parentId, children, rootId) {
  id = id || 'id'
  parentId = parentId || 'parentId'
  children = children || 'children'
  rootId = rootId || '0'
  // 对源数据深度克隆
  const cloneData = JSON.parse(JSON.stringify(data))
  // 循环所有项
  const treeData = cloneData.filter(father => {
    let branchArr = cloneData.filter(child => {
      // 返回每一项的子级数组
      return father[id] === child[parentId]
    })
    branchArr.length > 0 ? father.children = branchArr : ''
    // 返回第一层
    return father[parentId] === rootId
  })
  return treeData != '' ? treeData : data
}
