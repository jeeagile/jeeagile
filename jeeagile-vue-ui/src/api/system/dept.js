import {postApi} from '@/utils/axios'

/**
 * 查询部门列表
 */
export const selectDeptList = data => postApi('/system/dept/list', data)

/**
 * 新增部门
 */
export const addDept = data => postApi('/system/dept/add', data)

/**
 * 查看部门信息
 */
export const detailDept = data => postApi('/system/dept/detail', data)

/**
 * 修改部门信息
 */
export const updateDept = data => postApi('/system/dept/update', data)

/**
 * 删除部门信息
 */
export const deleteDept = data => postApi('/system/dept/delete', data)

