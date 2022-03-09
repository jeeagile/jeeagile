import {postApi} from '@/utils/axios'

/**
 * 获取个人信息
 */
export const getPersonInfo = () => postApi('/system/person/getPersonInfo')

/**
 * 更新个人信息
 */
export const updatePersonInfo = data => postApi('/system/person/updateInfo', data)

/**
 * 更新个人密码
 */
export const updatePersonPwd = data => postApi('/system/person/updatePwd', data)

/**
 * 更新个人头像
 */
export const uploadPersonAvatar = data => postApi('/system/person/uploadAvatar', data)
