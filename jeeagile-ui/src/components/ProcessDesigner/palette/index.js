import AgilePalette from './AgilePalette'
import AgilePaletteProvider from './AgilePaletteProvider'

export default {
  __depends__: [
    {
      __init__: ['agilePalette'],
      agilePalette: ['type', AgilePalette]
    }
  ],
  __init__: ['agilePaletteProvider'],
  agilePaletteProvider: ['type', AgilePaletteProvider]
}
