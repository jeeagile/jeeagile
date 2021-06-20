package com.jeeagile.system.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.system.entity.AgileSysPost;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysPostService extends IAgileBaseService<AgileSysPost> {
    /**
     * 分页查询岗位管理数据
     */
    AgilePage<AgileSysPost> selectPostPage(AgilePageable<AgileSysPost> agilePageable);

    /**
     * 查询岗位管理数据
     */
    List<AgileSysPost> selectPostList(AgileSysPost agileSysPost);

    /**
     * 查询岗位管理信息
     */
    AgileSysPost selectPostById(String postId);

    /**
     * 保存岗位管理
     */
    AgileSysPost savePost(AgileSysPost agileSysPost);

    /**
     * 更新岗位管理
     */
    boolean updatePostById(AgileSysPost agileSysPost);


    /**
     * 删除岗位管理
     */
    boolean deletePostById(String postId);
}
