import { postApi } from '@/utils/axios'

/**
 * 查询用户列表
 */
export const selectUserPage = data => postApi('/process/designer/selectUserPage', data)

/**
 * 查询角色列表
 */
export const selectRolePage = data => postApi('/process/designer/selectRolePage', data)

/**
 * 查询部门列表
 */
export const selectDeptPage = data => postApi('/process/designer/selectDeptPage', data)

/**
 * 查询岗位列表
 */
export const selectPostPage = data => postApi('/process/designer/selectPostPage', data)

/**
 * 查询用户分组列表
 */
export const selectGroupPage = data => postApi('/process/designer/selectGroupPage', data)

/**
 * 查询流程表达式列表
 */
export const selectExpressionPage = data => postApi('/process/designer/selectExpressionPage', data)

/**
 * 查询流程脚本列表
 */
export const selectScriptPage = data => postApi('/process/designer/selectScriptPage', data)

/**
 * 查看用户昵称
 */
export const detailUserNickName = data => postApi('/process/designer/detailUserNickName', data)

/**
 * 查看角色名称
 */
export const detailRoleName = data => postApi('/process/designer/detailRoleName', data)

/**
 * 查看部门名称
 */
export const detailDeptName = data => postApi('/process/designer/detailDeptName', data)

/**
 * 查看岗位名称
 */
export const detailPostName = data => postApi('/process/designer/detailPostName', data)

/**
 * 查看分组名称
 */
export const detailGroupName = data => postApi('/process/designer/detailGroupName', data)

/**
 * 查看脚本名称
 */
export const detailScriptName = data => postApi('/process/designer/detailScriptName', data)
