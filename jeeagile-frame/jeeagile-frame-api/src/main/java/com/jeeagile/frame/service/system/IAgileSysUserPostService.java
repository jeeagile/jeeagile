package com.jeeagile.frame.service.system;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.frame.entity.system.AgileSysUserPost;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysUserPostService extends IAgileBaseService<AgileSysUserPost> {
    /**
     * 获取用户分配岗位ID
     * @param userId
     * @return
     */
    List<String> getUserPostIdList(String userId);
}
