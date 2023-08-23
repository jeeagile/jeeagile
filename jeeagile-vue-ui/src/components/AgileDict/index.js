import Vue from 'vue'

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

const AgileUserStatus = new AgileBaseDict('用户状态', [
  {
    value: 0,
    label: '启用',
    symbol: 'ENABLE'
  },
  {
    value: 1,
    label: '停用',
    symbol: 'DISABLE'
  }
])
Vue.prototype.AgileUserStatus = AgileUserStatus

const AgileSwitchStatus = new AgileBaseDict('开关状态', [
  {
    value: '0',
    label: '停用',
    symbol: 'DISABLE'
  },
  {
    value: '1',
    label: '启用',
    symbol: 'ENABLE'
  }
])
Vue.prototype.AgileSwitchStatus = AgileSwitchStatus

const AgileNormalDisable = new AgileBaseDict('正常停用', [
  {
    value: '0',
    label: '正常',
    symbol: 'NORMAL'
  },
  {
    value: '1',
    label: '停用',
    symbol: 'DISABLE'
  }
])
Vue.prototype.AgileNormalDisable = AgileNormalDisable

const AgileShowHide = new AgileBaseDict('显示隐藏', [
  {
    value: '0',
    label: '显示',
    symbol: 'SHOW'
  },
  {
    value: '1',
    label: '隐藏',
    symbol: 'HIDE'
  }
])
Vue.prototype.AgileShowHide = AgileShowHide

const AgileSuccessFail = new AgileBaseDict('成功失败', [
  {
    value: '0',
    label: '成功',
    tag: 'success',
    symbol: 'SUCCESS'
  },
  {
    value: '1',
    label: '失败',
    tag: 'danger',
    symbol: 'FAIL'
  }
])
Vue.prototype.AgileSuccessFail = AgileSuccessFail

const AgileOperateType = new AgileBaseDict('日志操作类型', [
  {
    value: 'SELECT',
    label: '查询数据',
    symbol: 'SELECT'
  },
  {
    value: 'DETAIL',
    label: '查看明细',
    symbol: 'DETAIL'
  },
  {
    value: 'ADD',
    label: '添加数据',
    symbol: 'ADD'
  },
  {
    value: 'UPDATE',
    label: '更新数据',
    symbol: 'UPDATE'
  },
  {
    value: 'DELETE',
    label: '删除数据',
    symbol: 'DELETE'
  },
  {
    value: 'CLEAR',
    label: '清空数据',
    symbol: 'CLEAR'
  },
  {
    value: 'GRANT',
    label: '用户授权',
    symbol: 'GRANT'
  },
  {
    value: 'EXPORT',
    label: '导出数据',
    symbol: 'EXPORT'
  },
  {
    value: 'IMPORT',
    label: '导入数据',
    symbol: 'IMPORT'
  },
  {
    value: 'LOGIN',
    label: '用户登录',
    symbol: 'LOGIN'
  },
  {
    value: 'FORCE',
    label: '用户强退',
    symbol: 'FORCE'
  },
  {
    value: 'GENERATOR',
    label: '生成代码',
    symbol: 'GENERATOR'
  },
  {
    value: 'CLEAN',
    label: '清空数据',
    symbol: 'CLEAN'
  },
  {
    value: 'OTHER',
    label: '其它操作',
    symbol: 'OTHER'
  }
])
Vue.prototype.AgileOperateType = AgileOperateType

const AgileAuditStatus = new AgileBaseDict('审核状态', [
  {
    value: '0',
    label: '审核中',
    tag: 'primary',
    symbol: 'AUDIT'
  },
  {
    value: '1',
    label: '审核通过',
    tag: 'success',
    symbol: 'PASS'
  },
  {
    value: '2',
    label: '审核拒绝',
    tag: 'danger',
    symbol: 'REJECT'
  }
])
Vue.prototype.AgileAuditStatus = AgileAuditStatus

const AgilePublishStatus = new AgileBaseDict('发布状态', [
  {
    value: '01',
    label: '已发布',
    symbol: 'PUBLISHED'
  },
  {
    value: '02',
    label: '未发布',
    symbol: 'UNPUBLISHED'
  }
])
Vue.prototype.AgilePublishStatus = AgilePublishStatus

const AgileYesNo = new AgileBaseDict('系统是否', [
  {
    value: '0',
    label: '否',
    symbol: 'NO'
  },
  {
    value: '1',
    label: '是',
    symbol: 'YES'
  }
])
Vue.prototype.AgileYesNo = AgileYesNo

const AgileDataScope = new AgileBaseDict('数据权限', [
  {
    value: '01',
    label: '全部数据权限',
    symbol: 'ALL'
  },
  {
    value: '02',
    label: '本部门数据权限',
    symbol: 'DEPT'
  },
  {
    value: '03',
    label: '本部门及以下数据权限',
    symbol: 'DEPT_AND_CHILD'
  },
  {
    value: '04',
    label: '仅本人数据权限',
    symbol: 'SELF'
  },
  {
    value: '05',
    label: '自定义部门数据权限',
    symbol: 'CUSTOM'
  }
])
Vue.prototype.AgileDataScope = AgileDataScope


export {
  AgileUserStatus,
  AgileYesNo,
  AgilePublishStatus,
  AgileSwitchStatus,
  AgileNormalDisable,
  AgileSuccessFail,
  AgileAuditStatus
}


