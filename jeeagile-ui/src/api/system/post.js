import {postApi} from '@/utils/axios'

/**
 * 分页查询岗位列表
 */
export const selectPostPage = data => postApi('/system/post/page', data)

/**
 * 查询岗位列表
 */
export const selectPostList = data => postApi('/system/post/list', data)

/**
 * 新增岗位配置
 */
export const addPost = data => postApi('/system/post/add', data)

/**
 * 查看岗位配置
 */
export const detailPost = data => postApi('/system/post/detail', data)

/**
 * 修改岗位配置
 */
export const updatePost = data => postApi('/system/post/update', data)

/**
 * 删除岗位配置
 */
export const deletePost = data => postApi('/system/post/delete', data)

