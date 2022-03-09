import {postApi} from '@/utils/axios'

/**
 * 用户登录
 */
export const login = data => postApi('/system/user/login', data)

/**
 * 用户退出
 */
export const logout = () => postApi('/system/user/logout')

/**
 * 获取登录用户信息
 */
export const getUserInfo = () => postApi('/system/user/getUserInfo')

/**
 * 获取登录用户菜单
 */
export const getUserMenu = () => postApi('/system/user/getUserMenu')
