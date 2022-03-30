package com.jeeagile.frame.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.AgileTreeModel;
import com.jeeagile.frame.mapper.AgileTreeMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileTreeServiceImpl<M extends AgileTreeMapper<T>, T extends AgileTreeModel> extends AgileBaseServiceImpl<M, T> implements IAgileTreeService<T> {

    @Override
    public List<T> selectChildList(Serializable parentId) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (AgileStringUtil.isEmpty(parentId)) {
            parentId = "0";
        }
        queryWrapper.eq("parent_id", parentId);
        return this.list(queryWrapper);
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
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (parentId == null) {
            parentId = "0";
        }
        queryWrapper.eq("parent_id", parentId);
        return this.count(queryWrapper);
    }

    @Override
    public boolean hasChild(Serializable parentId) {
        return this.countChild(parentId) > 0 ? true : false;
    }
}
