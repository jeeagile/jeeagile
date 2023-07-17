import { postApi } from '@/utils/axios'

/**
 * 查询数据库列表
 */
export const selectJdbcPage = data => postApi('/system/jdbc/page', data)

/**
 * 查看数据库配置
 */
export const detailJdbc = jdbcId => postApi('/system/jdbc/detail', jdbcId)

/**
 * 新增数据库配置
 */
export const addJdbc = data => postApi('/system/jdbc/add', data)

/**
 * 修改数据库配置
 */
export const updateJdbc = data => postApi('/system/jdbc/update', data)

/**
 * 删除数据库配置
 */
export const deleteJdbc = data => postApi('/system/jdbc/delete', data)

/**
 * 导出数据库配置
 */
export const exportJdbc = data => postApi('/system/jdbc/export', data, { responseType: 'blob' })

/**
 * 查询数据库表列表
 */
export const selectJdbcTableList = data => postApi('/system/jdbc/selectTableList', data)
/**
 * 查询数据库表列表
 */
export const selectTableColumnList = data => postApi('/system/jdbc/selectTableColumnList', data)
