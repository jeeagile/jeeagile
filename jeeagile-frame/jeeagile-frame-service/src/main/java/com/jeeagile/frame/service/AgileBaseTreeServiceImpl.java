package com.jeeagile.frame.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.frame.entity.AgileBaseTreeModel;
import com.jeeagile.frame.mapper.AgileBaseTreeMapper;

import java.io.Serializable;
import java.util.ArrayList;
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
    public List<T> selectAllChildList(Serializable parentUuid) {
        List<T> allChildList = new ArrayList<>();
        List<T> childList = selectChildList(parentUuid);
        if (AgileCollectionUtil.isNotEmpty(childList)) {
            allChildList.addAll(childList);
            childList.forEach(t -> {
                allChildList.addAll(selectAllChildList(t.getId()));
            });
        }
        return allChildList;
    }

    @Override
    public long countChild(Serializable parentId) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if (parentId == null) {
            parentId = "0";
        }
        wrapper.eq("parent_id", parentId);
        return this.count(wrapper);
    }
    @Override
    public boolean hasChild(Serializable parentId) {
        return this.countChild(parentId) > 0 ? true : false;
    }
}
