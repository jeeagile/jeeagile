package com.jeeagile.frame.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.frame.entity.AgileBaseTreeModel;
import com.jeeagile.frame.mapper.AgileBaseTreeMapper;

import java.io.Serializable;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileBaseTreeServiceImpl<M extends AgileBaseTreeMapper<T>, T extends AgileBaseTreeModel> extends AgileBaseServiceImpl<M, T> implements IAgileBaseTreeService<T> {

    @Override
    public List<T> selectChildList(Serializable parentId) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if (parentId == null) {
            parentId = "0";
        }
        wrapper.eq("parent_id", parentId);
        return this.list(wrapper);
    }

    @Override
    public int countChild(Serializable parentId) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if (parentId == null) {
            parentId = "0";
        }
        wrapper.eq("parent_id", parentId);
        return this.count(wrapper);
    }
}
