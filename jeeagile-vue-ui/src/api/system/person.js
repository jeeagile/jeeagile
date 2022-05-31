import {postApi} from '@/utils/axios'

/**
 * 获取个人信息
 */
export const getPersonInfo = () => postApi('/system/person/info')

/**
 * 更新个人信息
 */
export const updatePersonInfo = data => postApi('/system/person/update', data)

/**
 * 更新个人密码
 */
export const updatePersonPwd = data => postApi('/system/person/password', data)

/**
 * 更新个人头像
 */
export const uploadPersonAvatar = data => postApi('/system/person/avatar', data)
