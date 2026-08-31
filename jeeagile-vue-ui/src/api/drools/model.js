import { postApi } from '@/utils/axios'

/**
 * 分页查询规则引擎 数据对象列表
 */
export const selectDroolsModelPage = data => postApi('/drools/model/page', data)

/**
 * 查询规则引擎 数据对象列表
 */
export const selectDroolsModelList = data => postApi('/drools/model/list', data)

/**
 * 查看规则引擎 数据对象
 */
export const detailDroolsModel = data => postApi('/drools/model/detail', data)

/**
 * 新增规则引擎 数据对象
 */
export const addDroolsModel = data => postApi('/drools/model/add', data)

/**
 * 修改规则引擎 数据对象
 */
export const updateDroolsModel = data => postApi('/drools/model/update', data)

/**
 * 删除规则引擎 数据对象
 */
export const deleteDroolsModel = data => postApi('/drools/model/delete', data)
/**
 * 更新规则引擎 数据对象 状态
 */
export const changeDroolsModelStatus = data => postApi('/drools/model/changeStatus', data)
