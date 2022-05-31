export default {
  /** 创建元素 */
  createElement(elementName, properties) {
    return window.processInstances.bpmnFactory.create(elementName, properties)
  },
  /** 更新元素 */
  updateProperties(element, properties) {
    window.processInstances.modeling.updateProperties(element, properties)
  },
  /** 更新元素 */
  updateModdleProperties(element, moddleElement, properties) {
    window.processInstances.modeling.updateModdleProperties(element, moddleElement, properties)
  },
  /** 流程翻译 */
  translate(key) {
    return window.processInstances.translate(key)
  }
}
