export class AgileBaseDict extends Map {
  constructor(dictName, dataList, valueKey = 'value', symbolKey = 'symbol') {
    super()
    this.dictName = dictName
    this.setList(dataList, valueKey, symbolKey)
  }

  setList(dataList, valueKey = 'value', symbolKey = 'symbol') {
    this.clear()
    if (Array.isArray(dataList)) {
      dataList.forEach((item) => {
        this.set(item[valueKey], item)
        if (item[symbolKey] != null) {
          Object.defineProperty(this, item[symbolKey], {
            get: function () {
              return item[valueKey]
            }
          })
        }
      })
    }
  }

  getList(valueKey = 'value', labelKey = 'label', parentKey = 'parentKey', filter) {
    let temp = []
    this.forEach((value, key) => {
      let obj = {
        key: key,
        value: (typeof value === 'string') ? value : value[valueKey],
        label: (typeof value === 'string') ? value : value[labelKey],
        parentKey: value[parentKey]
      }
      if (typeof filter !== 'function' || filter(obj)) {
        temp.push(obj)
      }
    })

    return temp
  }

  /**
   * 获取字典标签
   */
  getLabel(value, labelKey = 'label') {
    // 如果id为boolean类型，则自动转换为0和1
    if (typeof value === 'boolean') {
      value = value ? 1 : 0
    }
    return (this.get(value) || {})[labelKey]
  }

  /**
   * 获取
   */
  getTag(value, tagKey = 'tag') {
    // 如果id为boolean类型，则自动转换为0和1
    if (typeof value === 'boolean') {
      value = value ? 1 : 0
    }
    return (this.get(value) || {})[tagKey] || 'error'
  }
}


