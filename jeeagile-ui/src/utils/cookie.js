import Cookies from 'js-cookie'

const TokenKey = 'Agile-Token'

export function getUserToken() {
  return Cookies.get(TokenKey)
}

export function setUserToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeUserToken() {
  return Cookies.remove(TokenKey)
}
