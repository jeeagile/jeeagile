export default (id, name, type) => {
  if (!type) type = 'activiti'
  const process_target = {
    activiti: 'http://activiti.org/bpmn',
    camunda: 'http://bpmn.io/schema/bpmn',
    flowable: 'http://flowable.org/bpmn'
  }
  return `<?xml version="1.0" encoding="UTF-8"?>
          <bpmn2:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:bpmn2="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" id="diagram_${id}" targetNamespace="${process_target[type]}" xsi:schemaLocation="http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd">
             <bpmn2:process id="${id}" name="${name}" isExecutable="true" />
             <bpmndi:BPMNDiagram id="BPMNDiagram_1">
                <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="${id}" />
             </bpmndi:BPMNDiagram>
          </bpmn2:definitions>`
}
