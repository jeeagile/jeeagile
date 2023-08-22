package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.constants.SysNormalDisable;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.entity.system.AgileSysPost;
import com.jeeagile.frame.entity.system.AgileSysUserPost;
import com.jeeagile.frame.mapper.system.AgileSysPostMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

import static com.jeeagile.core.constants.AgileConstants.SYS_NORMAL_DISABLE;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysPostServiceImpl extends AgileBaseServiceImpl<AgileSysPostMapper, AgileSysPost> implements IAgileSysPostService {

    @Autowired
    private IAgileSysUserPostService agileSysUserPostService;
    @Autowired
    private IAgileSysDictDataService agileSysDictDataService;

    @Override
    public LambdaQueryWrapper<AgileSysPost> queryWrapper(AgileSysPost agileSysPost) {
        LambdaQueryWrapper<AgileSysPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysPost != null) {
            //岗位名称
            if (AgileStringUtil.isNotEmpty(agileSysPost.getPostName())) {
                lambdaQueryWrapper.like(AgileSysPost::getPostName, agileSysPost.getPostName());
            }
            //岗位编码
            if (AgileStringUtil.isNotEmpty(agileSysPost.getPostCode())) {
                lambdaQueryWrapper.like(AgileSysPost::getPostCode, agileSysPost.getPostCode());
            }
            //岗位状态
            if (AgileStringUtil.isNotEmpty(agileSysPost.getPostStatus())) {
                lambdaQueryWrapper.eq(AgileSysPost::getPostStatus, agileSysPost.getPostStatus());
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public List<AgileSysPost> selectExportData(AgileSysPost agileSysPost) {
        List<AgileSysPost> agileSysPostList = this.selectList(agileSysPost);
        agileSysPostList.forEach(item -> item.setPostStatus(SysNormalDisable.getDesc(item.getPostStatus())));
        return agileSysPostList;
    }

    @Override
    public void saveModelValidate(AgileSysPost agileSysPost) {
        this.validateData(agileSysPost);
    }

    @Override
    public void updateModelValidate(AgileSysPost agileSysPost) {
        this.validateData(agileSysPost);
    }

    @Override
    public boolean deleteModel(Serializable postId) {
        this.deleteUserPost(postId);
        return super.deleteModel(postId);
    }

    /**
     * 将该岗位从已分配的用户中删除
     *
     * @param postId
     * @return
     */
    private boolean deleteUserPost(Serializable postId) {
        if (AgileStringUtil.isNotEmpty(postId)) {
            LambdaQueryWrapper<AgileSysUserPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysUserPost::getPostId, postId);
            return agileSysUserPostService.remove(lambdaQueryWrapper);
        } else {
            return true;
        }
    }

    /**
     * 校验岗位编码、岗位名称是否存在
     */
    private void validateData(AgileSysPost agileSysPost) {
        LambdaQueryWrapper<AgileSysPost> queryWrapper = new LambdaQueryWrapper<>();
        if (agileSysPost.getId() != null) {
            queryWrapper.ne(AgileSysPost::getId, agileSysPost.getId());
        }
        queryWrapper.and(wrapper ->
                wrapper.eq(AgileSysPost::getPostCode, agileSysPost.getPostCode()).or().eq(AgileSysPost::getPostName, agileSysPost.getPostName())
        );
        if (this.count(queryWrapper) > 0) {
            throw new AgileValidateException("岗位编码或岗位名称已存在！");
        }
    }
}
