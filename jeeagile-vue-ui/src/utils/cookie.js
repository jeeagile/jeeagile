import Cookies from 'js-cookie'

const AGILE_TOKEN_KEY = 'AGILE_TOKEN'

export function getUserToken() {
  return Cookies.get(AGILE_TOKEN_KEY)
}

export function setUserToken(token) {
  return Cookies.set(AGILE_TOKEN_KEY, token)
}

export function removeUserToken() {
  return Cookies.remove(AGILE_TOKEN_KEY)
}
