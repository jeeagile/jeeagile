package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.core.util.validate.ValidateUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.system.entity.AgileSysPost;
import com.jeeagile.system.mapper.AgileSysPostMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysPostServiceImpl extends AgileBaseServiceImpl<AgileSysPostMapper, AgileSysPost> implements IAgileSysPostService {

    @Autowired
    private IAgileSysUserPostService agileSysUserPostService;

    @Override
    public AgilePage<AgileSysPost> selectPostPage(AgilePageable<AgileSysPost> agilePageable) {
        return this.page(agilePageable, getSysPostQueryWrapper(agilePageable.getQueryCond()));
    }

    @Override
    public List<AgileSysPost> selectPostList(AgileSysPost agileSysPost) {
        return this.list(getSysPostQueryWrapper(agileSysPost));
    }

    @Override
    public AgileSysPost selectPostById(String postId) {
        return this.getById(postId);
    }

    @Override
    public AgileSysPost savePost(AgileSysPost agileSysPost) {
        //校验业务数据
        ValidateUtil.validateObject(agileSysPost);
        this.save(agileSysPost);
        return agileSysPost;
    }

    @Override
    public boolean updatePostById(AgileSysPost agileSysPost) {
        //校验业务数据
        ValidateUtil.validateObject(agileSysPost);
        return this.updateById(agileSysPost);
    }

    @Override
    public boolean deletePostById(String postId) {
        agileSysUserPostService.deleteByPostId(postId);
        return this.removeById(postId);
    }

    /**
     * 拼装查询条件
     */
    private QueryWrapper<AgileSysPost> getSysPostQueryWrapper(AgileSysPost agileSysPost) {
        QueryWrapper<AgileSysPost> queryWrapper = new QueryWrapper<>();
        if (agileSysPost != null) {
            //岗位名称
            if (StringUtil.isNotEmpty(agileSysPost.getPostName())) {
                queryWrapper.lambda().like(AgileSysPost::getPostName, agileSysPost.getPostName());
            }
            //岗位编码
            if (StringUtil.isNotEmpty(agileSysPost.getPostCode())) {
                queryWrapper.lambda().like(AgileSysPost::getPostCode, agileSysPost.getPostCode());
            }
            //岗位状态
            if (StringUtil.isNotEmpty(agileSysPost.getPostStatus())) {
                queryWrapper.lambda().eq(AgileSysPost::getPostStatus, agileSysPost.getPostStatus());
            }
        }
        return queryWrapper;
    }
}
