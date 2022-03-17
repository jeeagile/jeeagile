'use strict'

let isFunction = require('min-dash').isFunction,
  isObject = require('min-dash').isObject,
  some = require('min-dash').some

let WILDCARD = '*'


function ActivitiModdleExtension(eventBus) {

  let self = this

  eventBus.on('moddleCopy.canCopyProperty', (context) => {
    let property = context.property,
      parent = context.parent

    return self.canCopyProperty(property, parent)
  })
}

ActivitiModdleExtension.$inject = ['eventBus']

/**
 * Check wether to disallow copying property.
 */
ActivitiModdleExtension.prototype.canCopyProperty = function (property, parent) {

  // (1) check wether property is allowed in parent
  if (isObject(property) && !isAllowedInParent(property, parent)) {

    return false
  }

  // (2) check more complex scenarios

  if (is(property, 'activiti:InputOutput') && !this.canHostInputOutput(parent)) {
    return false
  }

  if (isAny(property, ['activiti:Connector', 'activiti:Field']) && !this.canHostConnector(parent)) {
    return false
  }

  if (is(property, 'activiti:In') && !this.canHostIn(parent)) {
    return false
  }
}

ActivitiModdleExtension.prototype.canHostInputOutput = function (parent) {

  // allowed in activiti:Connector
  let connector = getParent(parent, 'activiti:Connector')

  if (connector) {
    return true
  }

  // special rules inside bpmn:FlowNode
  let flowNode = getParent(parent, 'bpmn:FlowNode')

  if (!flowNode) {
    return false
  }

  if (isAny(flowNode, ['bpmn:StartEvent', 'bpmn:Gateway', 'bpmn:BoundaryEvent'])) {
    return false
  }

  if (is(flowNode, 'bpmn:SubProcess') && flowNode.get('triggeredByEvent')) {
    return false
  }

  return true
}

ActivitiModdleExtension.prototype.canHostConnector = function (parent) {

  let serviceTaskLike = getParent(parent, 'activiti:ServiceTaskLike')

  if (is(serviceTaskLike, 'bpmn:MessageEventDefinition')) {

    // only allow on throw and end events
    return (
      getParent(parent, 'bpmn:IntermediateThrowEvent') ||
      getParent(parent, 'bpmn:EndEvent')
    )
  }

  return true
}

ActivitiModdleExtension.prototype.canHostIn = function (parent) {

  let callActivity = getParent(parent, 'bpmn:CallActivity')

  if (callActivity) {
    return true
  }

  let signalEventDefinition = getParent(parent, 'bpmn:SignalEventDefinition')

  if (signalEventDefinition) {

    // only allow on throw and end events
    return (
      getParent(parent, 'bpmn:IntermediateThrowEvent') ||
      getParent(parent, 'bpmn:EndEvent')
    )
  }

  return true
}

module.exports = ActivitiModdleExtension

// helpers //////////

function is(element, type) {
  return element && isFunction(element.$instanceOf) && element.$instanceOf(type)
}

function isAny(element, types) {
  return some(types, (t) => {
    return is(element, t)
  })
}

function getParent(element, type) {
  if (!type) {
    return element.$parent
  }

  if (is(element, type)) {
    return element
  }

  if (!element.$parent) {
    return
  }

  return getParent(element.$parent, type)
}

function isAllowedInParent(property, parent) {

  // (1) find property descriptor
  let descriptor = property.$type && property.$model.getTypeDescriptor(property.$type)

  let allowedIn = descriptor && descriptor.meta && descriptor.meta.allowedIn

  if (!allowedIn || isWildcard(allowedIn)) {
    return true
  }

  // (2) check wether property has parent of allowed type
  return some(allowedIn, (type) => {
    return getParent(parent, type)
  })
}

function isWildcard(allowedIn) {
  return allowedIn.indexOf(WILDCARD) !== -1
}
