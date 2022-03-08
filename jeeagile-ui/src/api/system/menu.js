import {postApi} from '@/utils/axios'

/**
 * 查询菜单列表
 */
export const selectMenuList = data => postApi('/system/menu/list', data)

/**
 * 新增菜单
 */
export const addMenu = data => postApi('/system/menu/add', data)

/**
 * 查看菜单信息
 */
export const detailMenu = data => postApi('/system/menu/detail', data)

/**
 * 修改菜单信息
 */
export const updateMenu = data => postApi('/system/menu/update', data)

/**
 * 删除菜单信息
 */
export const deleteMenu = data => postApi('/system/menu/delete', data)

/**
 * 修改菜单排序
 */
export const updateMenuSort = data => postApi('/system/menu/updateSort', data)


