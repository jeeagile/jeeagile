import {postApi} from '@/utils/axios'

/**
 * 初始化数据接口
 */
export const initData = data => postApi('/system/role/init', data)

/**
 * 分页查询角色列表
 */
export const selectRolePage = data => postApi('/system/role/page', data)

/**
 * 查询角色列表
 */
export const selectRoleList = data => postApi('/system/role/list', data)

/**
 * 新增角色
 */
export const addRole = data => postApi('/system/role/add', data)

/**
 * 查看角色信息
 */
export const detailRole = data => postApi('/system/role/detail', data)

/**
 * 修改角色信息
 */
export const updateRole = data => postApi('/system/role/update', data)

/**
 * 删除角色信息
 */
export const deleteRole = data => postApi('/system/role/delete', data)

/**
 * 修改角色状态
 */
export const changeRoleStatus = data => postApi('/system/role/changeStatus', data)

/**
 * 修改角色数据范围
 */
export const updateDataScope = data => postApi('/system/role/dataScope', data)


