import {postApi} from '@/utils/axios'

/**
 * 分页查询用户列表
 */
export const selectUserPage = data => postApi('/system/user/page', data)

/**
 * 查询用户列表
 */
export const selectUserList = data => postApi('/system/user/list', data)

/**
 * 新增用户
 */
export const addUser = data => postApi('/system/user/add', data)

/**
 * 查看用户信息
 */
export const detailUser = data => postApi('/system/user/detail', data)

/**
 * 修改用户信息
 */
export const updateUser = data => postApi('/system/user/update', data)

/**
 * 删除用户信息
 */
export const deleteUser = data => postApi('/system/user/delete', data)

/**
 * 重置用户密码
 */
export const resetUserPwd = data => postApi('/system/user/resetPwd', data)

/**
 * 修改用户状态
 */
export const changeUserStatus = data => postApi('/system/user/changeStatus', data)

