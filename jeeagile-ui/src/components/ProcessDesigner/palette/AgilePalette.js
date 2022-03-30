import {forEach, isArray, isFunction} from 'min-dash'

import {
  attr as domAttr,
  classes as domClasses,
  clear as domClear,
  delegate as domDelegate,
  domify,
  event as domEvent,
  matches as domMatches,
  query as domQuery
} from 'min-dom'


let TOGGLE_SELECTOR = '.djs-palette-toggle',
  ENTRY_SELECTOR = '.entry',
  ELEMENT_SELECTOR = TOGGLE_SELECTOR + ', ' + ENTRY_SELECTOR

let PALETTE_PREFIX = 'djs-palette-',
  PALETTE_SHOWN_CLS = 'shown',
  PALETTE_OPEN_CLS = 'open',
  PALETTE_TWO_COLUMN_CLS = 'two-column'

let DEFAULT_PRIORITY = 1000


/**
 * A palette containing modeling elements.
 */
export default function AgilePalette(eventBus, canvas) {

  this._eventBus = eventBus
  this._canvas = canvas

  let self = this

  eventBus.on('tool-manager.update', (event) => {
    let tool = event.tool

    self.updateToolHighlight(tool)
  })

  eventBus.on('i18n.changed', () => {
    self._update()
  })

  eventBus.on('diagram.init', () => {

    self._diagramInitialized = true

    self._rebuild()
  })
}

AgilePalette.$inject = ['eventBus', 'canvas']


/**
 * Register a provider with the palette
 *
 * @param  {number} [priority=1000]
 * @param  {PaletteProvider} provider
 *
 * @example
 * const paletteProvider = {
 *   getPaletteEntries: function() {
 *     return function(entries) {
 *       return {
 *         ...entries,
 *         'entry-1': {
 *           label: 'My Entry',
 *           action: function() { alert("I have been clicked!"); }
 *         }
 *       };
 *     }
 *   }
 * };
 *
 * palette.registerProvider(800, paletteProvider);
 */
AgilePalette.prototype.registerProvider = function (priority, provider) {
  if (!provider) {
    provider = priority
    priority = DEFAULT_PRIORITY
  }

  this._eventBus.on('palette.getProviders', priority, (event) => {
    event.providers.push(provider)
  })

  this._rebuild()
}


/**
 * Returns the palette entries
 *
 * @return {Object<string, PaletteEntryDescriptor>} map of entries
 */
AgilePalette.prototype.getEntries = function () {
  let providers = this._getProviders()

  return providers.reduce(addPaletteEntries, {})
}

AgilePalette.prototype._rebuild = function () {

  if (!this._diagramInitialized) {
    return
  }

  let providers = this._getProviders()

  if (!providers.length) {
    return
  }

  if (!this._container) {
    this._init()
  }

  this._update()
}

/**
 * Initialize
 */
AgilePalette.prototype._init = function () {

  let self = this

  let eventBus = this._eventBus

  let parentContainer = this._getParentContainer()

  let container = this._container = domify(AgilePalette.HTML_MARKUP)

  parentContainer.appendChild(container)
  domClasses(parentContainer).add(PALETTE_PREFIX + PALETTE_SHOWN_CLS)

  domDelegate.bind(container, ELEMENT_SELECTOR, 'click', (event) => {

    let target = event.delegateTarget

    if (domMatches(target, TOGGLE_SELECTOR)) {
      return self.toggle()
    }

    self.trigger('click', event)
  })

  // prevent drag propagation
  domEvent.bind(container, 'mousedown', (event) => {
    event.stopPropagation()
  })

  // prevent drag propagation
  domDelegate.bind(container, ENTRY_SELECTOR, 'dragstart', (event) => {
    self.trigger('dragstart', event)
  })

  eventBus.on('canvas.resized', this._layoutChanged, this)

  eventBus.fire('palette.create', {
    container: container
  })
}

AgilePalette.prototype._getProviders = function (id) {

  let event = this._eventBus.createEvent({
    type: 'palette.getProviders',
    providers: []
  })

  this._eventBus.fire(event)

  return event.providers
}

/**
 * Update palette state.
 *
 * @param  {Object} [state] { open, twoColumn }
 */
AgilePalette.prototype._toggleState = function (state) {

  state = state || {}

  let parent = this._getParentContainer(),
    container = this._container

  let eventBus = this._eventBus

  let twoColumn

  let cls = domClasses(container),
    parentCls = domClasses(parent)

  if ('twoColumn' in state) {
    twoColumn = state.twoColumn
  } else {
    twoColumn = this._needsCollapse(parent.clientHeight, this._entries || {})
  }

  // always update two column
  cls.toggle(PALETTE_TWO_COLUMN_CLS, twoColumn)
  parentCls.toggle(PALETTE_PREFIX + PALETTE_TWO_COLUMN_CLS, twoColumn)

  if ('open' in state) {
    cls.toggle(PALETTE_OPEN_CLS, state.open)
    parentCls.toggle(PALETTE_PREFIX + PALETTE_OPEN_CLS, state.open)
  }

  eventBus.fire('palette.changed', {
    twoColumn: twoColumn,
    open: this.isOpen()
  })
}

AgilePalette.prototype._update = function () {

  let entriesContainer = domQuery('.djs-palette-entries', this._container),
    entries = this._entries = this.getEntries()

  domClear(entriesContainer)

  forEach(entries, (entry, id) => {

    let grouping = entry.group || 'default'

    let container = domQuery('[data-group=' + grouping + ']', entriesContainer)
    if (!container) {
      container = domify('<div class="group" data-group="' + grouping + '"></div>')
      entriesContainer.appendChild(container)
    }

    let html = entry.html || (
      entry.separator ?
        '<hr class="separator" />' :
        '<div class="entry" draggable="true"></div>')


    let control = domify(html)
    container.appendChild(control)

    if (!entry.separator) {
      domAttr(control, 'data-action', id)

      if (entry.title) {
        domAttr(control, 'title', entry.title)
      }

      if (entry.className) {
        addClasses(control, entry.className)
      }

      if (entry.imageUrl) {
        control.appendChild(domify('<img src="' + entry.imageUrl + '">'))
      }
    }
  })

  // open after update
  this.open()
}


/**
 * Trigger an action available on the palette
 *
 * @param  {string} action
 * @param  {Event} event
 */
AgilePalette.prototype.trigger = function (action, event, autoActivate) {
  let entries = this._entries,
    entry,
    handler,
    originalEvent,
    button = event.delegateTarget || event.target

  if (!button) {
    return event.preventDefault()
  }

  entry = entries[domAttr(button, 'data-action')]

  // when user clicks on the palette and not on an action
  if (!entry) {
    return
  }

  handler = entry.action

  originalEvent = event.originalEvent || event

  // simple action (via callback function)
  if (isFunction(handler)) {
    if (action === 'click') {
      handler(originalEvent, autoActivate)
    }
  } else {
    if (handler[action]) {
      handler[action](originalEvent, autoActivate)
    }
  }

  // silence other actions
  event.preventDefault()
}

AgilePalette.prototype._layoutChanged = function () {
  this._toggleState({})
}

/**
 * Do we need to collapse to two columns?
 *
 * @param {number} availableHeight
 * @param {Object} entries
 *
 * @return {boolean}
 */
AgilePalette.prototype._needsCollapse = function (availableHeight, entries) {

  // top margin + bottom toggle + bottom margin
  // implementors must override this method if they
  // change the palette styles
  let margin = 20 + 10 + 20

  let entriesHeight = Object.keys(entries).length * 46

  return availableHeight < entriesHeight + margin
}

/**
 * Close the palette
 */
AgilePalette.prototype.close = function () {

  this._toggleState({
    open: false,
    twoColumn: false
  })
}


/**
 * Open the palette
 */
AgilePalette.prototype.open = function () {
  this._toggleState({ open: true })
}


AgilePalette.prototype.toggle = function (open) {
  if (this.isOpen()) {
    this.close()
  } else {
    this.open()
  }
}

AgilePalette.prototype.isActiveTool = function (tool) {
  return tool && this._activeTool === tool
}

AgilePalette.prototype.updateToolHighlight = function (name) {
  let entriesContainer,
    toolsContainer

  if (!this._toolsContainer) {
    entriesContainer = domQuery('.djs-palette-entries', this._container)

    this._toolsContainer = domQuery('[data-group=tools]', entriesContainer)
  }

  toolsContainer = this._toolsContainer

  forEach(toolsContainer.children, (tool) => {
    let actionName = tool.getAttribute('data-action')

    if (!actionName) {
      return
    }

    let toolClasses = domClasses(tool)

    actionName = actionName.replace('-tool', '')

    if (toolClasses.contains('entry') && actionName === name) {
      toolClasses.add('highlighted-entry')
    } else {
      toolClasses.remove('highlighted-entry')
    }
  })
}


/**
 * Return true if the palette is opened.
 *
 * @example
 *
 * palette.open();
 *
 * if (palette.isOpen()) {
 *   // yes, we are open
 * }
 *
 * @return {boolean} true if palette is opened
 */
AgilePalette.prototype.isOpen = function () {
  return domClasses(this._container).has(PALETTE_OPEN_CLS)
}

/**
 * Get container the palette lives in.
 *
 * @return {Element}
 */
AgilePalette.prototype._getParentContainer = function () {
  return this._canvas.getContainer()
}


/* markup definition */

AgilePalette.HTML_MARKUP =
  '<div class="djs-palette">' +
  '<div class="djs-palette-entries"></div>' +
  '<div class="djs-palette-toggle"></div>' +
  '</div>'


// helpers //////////////////////

function addClasses(element, classNames) {

  let classes = domClasses(element)

  let actualClassNames = isArray(classNames) ? classNames : classNames.split(/\s+/g)
  actualClassNames.forEach((cls) => {
    classes.add(cls)
  })
}

function addPaletteEntries(entries, provider) {

  let entriesOrUpdater = provider.getPaletteEntries()

  if (isFunction(entriesOrUpdater)) {
    return entriesOrUpdater(entries)
  }

  forEach(entriesOrUpdater, (entry, id) => {
    entries[id] = entry
  })

  return entries
}
