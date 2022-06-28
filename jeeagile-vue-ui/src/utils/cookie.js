import Cookies from 'js-cookie'

const AGILE_TOKEN = 'AGILE_TOKEN'

export function getUserToken() {
  return Cookies.get(AGILE_TOKEN)
}

export function setUserToken(token) {
  return Cookies.set(AGILE_TOKEN, token)
}

export function removeUserToken() {
  return Cookies.remove(AGILE_TOKEN)
}

