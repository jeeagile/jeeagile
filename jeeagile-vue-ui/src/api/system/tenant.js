import { postApi } from '@/utils/axios'

/**
 * 查询租户列表
 */
export const selectTenantPage = data => postApi('/system/tenant/page', data)

/**
 * 查看租户信息
 */
export const detailTenant = tenantId => postApi('/system/tenant/detail', tenantId)

/**
 * 新增租户信息
 */
export const addTenant = data => postApi('/system/tenant/add', data)

/**
 * 修改租户信息
 */
export const updateTenant = data => postApi('/system/tenant/update', data)

/**
 * 删除租户信息
 */
export const deleteTenant = data => postApi('/system/tenant/delete', data)

/**
 * 获取租户信息
 */
export const getTenantInfo = data => postApi('/system/tenant/info', data)


