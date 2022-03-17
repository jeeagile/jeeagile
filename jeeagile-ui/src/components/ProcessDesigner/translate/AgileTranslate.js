import processLanguage from './i18n'

export default function agileTranslate(i18n) {
  const translations = processLanguage[i18n || 'zn']
  return function (template, replacements) {
    replacements = replacements || {}
    // Translate
    template = translations[template] || template

    // Replace
    return template.replace(/{([^}]+)}/g, (_, key) => {
      return replacements[key] || '{' + key + '}'
    })
  }
}
