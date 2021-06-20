package com.jeeagile.system.service;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.system.entity.AgileSysUserPost;

import java.util.List;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysUserPostService extends IAgileBaseService<AgileSysUserPost> {

    /**
     * 获取用户岗位列表
     */
    List<AgileSysUserPost> selectListByUserId(String userId);

    /**
     * 根据用户UUID删除岗位
     */
    boolean deleteByUserId(String userId);

    /**
     * 根据岗位ID删除岗位
     */
    boolean deleteByPostId(String postId);

}
