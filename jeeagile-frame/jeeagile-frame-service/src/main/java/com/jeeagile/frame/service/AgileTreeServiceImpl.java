package com.jeeagile.frame.service;

import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.frame.entity.AgileBaseTreeModel;
import com.jeeagile.frame.mapper.AgileTreeMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public abstract class AgileTreeServiceImpl<M extends AgileTreeMapper<T>, T extends AgileBaseTreeModel> extends AgileBaseServiceImpl<M, T> implements IAgileTreeService<T> {

    @Override
    public List<T> selectChild(Serializable parentId) {
        return this.baseMapper.selectChild(parentId);
    }

    @Override
    public long countChild(Serializable parentId) {
        Long count = this.baseMapper.countChild(parentId);
        return count == null ? 0 : count;
    }

    @Override
    public boolean hasChild(Serializable parentId) {
        return this.countChild(parentId) > 0 ? true : false;
    }

    @Override
    public List<T> selectAllChild(Serializable parentId) {
        List<T> allChildList = new ArrayList<>();
        List<T> childList = selectChild(parentId);
        if (AgileCollectionUtil.isNotEmpty(childList)) {
            allChildList.addAll(childList);
            childList.forEach(t -> {
                allChildList.addAll(selectAllChild(t.getId()));
            });
        }
        return allChildList;
    }
}
