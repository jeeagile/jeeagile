import activitiExtension from './activiti'
import camundaExtension from './camunda'
import flowableExtension from './flowable'

export default function (prefix) {
  switch (prefix) {
  case 'activiti':
    return activitiExtension
  case 'camunda':
    return camundaExtension
  case 'flowable':
    return flowableExtension
  default:
    return activitiExtension
  }
}
