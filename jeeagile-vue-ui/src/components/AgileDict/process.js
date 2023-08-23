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

export {
  ProcessDeploymentStatus
}
