import { postApi } from '@/utils/axios'

/**
 * 分页查询规则引擎 数据对象字段列表
 */
export const selectDroolsModelFieldPage = data => postApi('/drools/field/page', data)

/**
 * 查询规则引擎 数据对象字段列表
 */
export const selectDroolsModelFieldList = data => postApi('/drools/field/list', data)

/**
 * 查看规则引擎 数据对象字段
 */
export const detailDroolsModelField = data => postApi('/drools/field/detail', data)

/**
 * 新增规则引擎 数据对象字段
 */
export const addDroolsModelField = data => postApi('/drools/field/add', data)

/**
 * 修改规则引擎 数据对象字段
 */
export const updateDroolsModelField = data => postApi('/drools/field/update', data)

/**
 * 删除规则引擎 数据对象字段
 */
export const deleteDroolsModelField = data => postApi('/drools/field/delete', data)

