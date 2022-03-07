package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.system.entity.AgileSysUserPost;
import com.jeeagile.system.mapper.AgileSysUserPostMapper;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysUserPostServiceImpl extends AgileBaseServiceImpl<AgileSysUserPostMapper, AgileSysUserPost> implements IAgileSysUserPostService {

    @Override
    public List<AgileSysUserPost> selectListByUserId(String userId) {
        QueryWrapper<AgileSysUserPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgileSysUserPost::getUserId, userId);
        return this.list(queryWrapper);
    }

    @Override
    public boolean deleteByUserId(String userId) {
        if (AgileStringUtil.isNotEmpty(userId)) {
            QueryWrapper<AgileSysUserPost> whereWrapper = new QueryWrapper<>();
            whereWrapper.lambda().eq(AgileSysUserPost::getUserId, userId);
            return this.remove(whereWrapper);
        } else {
            return true;
        }
    }

    @Override
    public boolean deleteByPostId(String postId) {
        if (AgileStringUtil.isNotEmpty(postId)) {
            QueryWrapper<AgileSysUserPost> whereWrapper = new QueryWrapper<>();
            whereWrapper.lambda().eq(AgileSysUserPost::getPostId, postId);
            return this.remove(whereWrapper);
        } else {
            return true;
        }
    }
}
