import {postApi} from '@/utils/axios'

/**
 * 获取验证码
 */
export const getCodeImage = () => postApi('/system/kaptcha/image')

/**
 * 验证验证码
 */
export const validCode = (data) => postApi('/system/kaptcha/valid', data)


