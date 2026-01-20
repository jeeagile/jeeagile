import { postApi } from '@/utils/axios'

/**
 * 分页查询代码生成列表
 */
export const selectGeneratorTablePage = data => postApi('/generator/selectTablePage', data)

/**
 * 查询数据库表
 */
export const selectDbTablePage = data => postApi('/generator/selectDbTablePage', data)

/**
 * 导入数据库表
 */
export const importGeneratorTable = data => postApi('/generator/importTable', data)

/**
 * 查看代码生成表信息
 */
export const detailGeneratorTable = data => postApi('/generator/detailTable', data)

/**
 * 更新代码生成表信息
 */
export const updateGeneratorTable = data => postApi('/generator/updateTable', data)

/**
 * 删除代码生成表信息
 */
export const deleteGeneratorTable = data => postApi('/generator/deleteTable', data)

/**
 * 同步代码生成表信息
 */
export const syncGeneratorTable = data => postApi('/generator/syncTable', data)

/**
 * 预览代码
 */
export const previewGeneratorCode = data => postApi('/generator/previewCode', data)

/**
 * 下载代码
 */
export const downloadGeneratorCode = data => postApi('/generator/downloadCode', data, { responseType: 'blob' })

