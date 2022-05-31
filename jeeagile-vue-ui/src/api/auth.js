import { postApi } from '@/utils/axios'

/**
 * 用户登录
 */
export const login = data => postApi('/system/auth/login', data)

/**
 * 用户退出
 */
export const logout = () => postApi('/system/auth/logout')

/**
 * 获取登录用户信息
 */
export const getUserInfo = () => postApi('/system/auth/getUserInfo')

/**
 * 获取登录用户菜单
 */
export const getUserMenu = () => postApi('/system/auth/getUserMenu')
