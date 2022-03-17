import camundaDescriptor from './camunda/camunda.json'
import activitiDescriptor from './activiti/activiti.json'
import flowableDescriptor from './flowable/flowable.json'

export default function (prefix) {
  switch (prefix) {
  case 'activiti':
    return activitiDescriptor
  case 'camunda':
    return camundaDescriptor
  case 'flowable':
    return flowableDescriptor
  default:
    return activitiDescriptor
  }
}
