import { postApi } from '@/utils/axios'

/**
 * 查询表单设计列表
 */
export const selectUserPage = data => postApi('/process/designer/selectUserPage', data)

/**
 * 查看用户昵称
 */
export const detailUserNickName = data => postApi('/process/designer/detailUserNickName', data)
