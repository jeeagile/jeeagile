import Vue from 'vue'
import { AgileBaseDict } from './index'

const ProcessDeploymentStatus = new AgileBaseDict('发布状态', [
  {
    value: '1',
    label: '已发布',
    symbol: 'PUBLISHED'
  },
  {
    value: '2',
    label: '未发布',
    symbol: 'UNPUBLISHED'
  }
])
Vue.prototype.ProcessDeploymentStatus = ProcessDeploymentStatus

const ProcessOrderStatus = new AgileBaseDict('工单状态', [
  {
    value: '01',
    label: '已提交',
    symbol: 'SUBMITTED'
  },
  {
    value: '02',
    label: '审批中',
    symbol: 'APPROVAL'
  },
  {
    value: '03',
    label: '已拒绝',
    symbol: 'REJECTED'
  },
  {
    value: '04',
    label: '已完成',
    symbol: 'FINISHED'
  },
  {
    value: '05',
    label: '终止',
    symbol: 'STOPPED'
  },
  {
    value: '06',
    label: '撤销',
    symbol: 'CANCEL'
  }
])
Vue.prototype.ProcessOrderStatus = ProcessOrderStatus

export {
  ProcessDeploymentStatus
}
