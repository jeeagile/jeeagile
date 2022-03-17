import AgileTranslate from './AgileTranslate'

export default function (i18n) {
  return { translate: ['value', AgileTranslate(i18n)] }
}
