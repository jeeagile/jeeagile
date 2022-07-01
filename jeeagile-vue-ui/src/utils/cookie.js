import Cookies from 'js-cookie'

const AGILE_TOKEN = 'AGILE_TOKEN'
const AGILE_TENANT_ID = 'AGILE_TENANT_ID'
const AGILE_TENANT_SIGN = 'AGILE_TENANT_SIGN'

export function getUserToken() {
  return Cookies.get(AGILE_TOKEN)
}

export function setUserToken(token) {
  return Cookies.set(AGILE_TOKEN, token)
}

export function removeUserToken() {
  return Cookies.remove(AGILE_TOKEN)
}

export function getUserTenantId() {
  return Cookies.get(AGILE_TENANT_ID)
}

export function setUserTenantId(tenantId) {
  return Cookies.set(AGILE_TENANT_ID, tenantId)
}
export function removeUserTenantId() {
  return Cookies.remove(AGILE_TENANT_ID)
}

export function getUserTenantSign() {
  return Cookies.get(AGILE_TENANT_SIGN)
}

export function setUserTenantSign(tenantSign) {
  return Cookies.set(AGILE_TENANT_SIGN, tenantSign)
}

export function removeUserTenantSign() {
  return Cookies.remove(AGILE_TENANT_SIGN)
}

