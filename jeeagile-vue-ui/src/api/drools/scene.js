import { postApi } from '@/utils/axios'

/**
 * 分页查询规则引擎 规则场景列表
 */
export const selectDroolsScenePage = data => postApi('/drools/scene/page', data)

/**
 * 查询规则引擎 规则场景列表
 */
export const selectDroolsSceneList = data => postApi('/drools/scene/list', data)

/**
 * 查看规则引擎 规则场景
 */
export const detailDroolsScene = data => postApi('/drools/scene/detail', data)

/**
 * 新增规则引擎 规则场景
 */
export const addDroolsScene = data => postApi('/drools/scene/add', data)

/**
 * 修改规则引擎 规则场景
 */
export const updateDroolsScene = data => postApi('/drools/scene/update', data)

/**
 * 删除规则引擎 规则场景
 */
export const deleteDroolsScene = data => postApi('/drools/scene/delete', data)
/**
 * 执行规则引擎 规则信息
 */
export const detailDroolsSceneInfo  = data => postApi('/drools/scene/info', data)
/**
 * 执行规则引擎 规则场景
 */
export const executeDroolsScene  = data => postApi('/drools/scene/execute', data)
