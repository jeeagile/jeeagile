package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.entity.system.AgileSysUserPost;
import com.jeeagile.frame.mapper.system.AgileSysUserPostMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysUserPostServiceImpl extends AgileBaseServiceImpl<AgileSysUserPostMapper, AgileSysUserPost> implements IAgileSysUserPostService {
    @Override
    public List<String> getUserPostIdList(String userId) {
        List<String> userPostIdList = new ArrayList<>();
        LambdaQueryWrapper<AgileSysUserPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysUserPost::getUserId, userId);
        List<AgileSysUserPost> agileSysUserPostList = this.list(lambdaQueryWrapper);
        agileSysUserPostList.forEach(item -> userPostIdList.add(item.getPostId()));
        return userPostIdList;
    }
}
