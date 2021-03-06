package com.jeeagile.frame.mapper;

import com.jeeagile.frame.entity.AgileBaseTreeModel;

import java.io.Serializable;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface AgileTreeMapper<T extends AgileBaseTreeModel> extends AgileBaseMapper<T> {
    /**
     * 返回叶子节点
     *
     * @param parentId
     * @return
     */
    List<T> selectChild(Serializable parentId);

    /**
     * 返回叶子节点个数
     *
     * @param parentId
     * @return
     */
    Long countChild(Serializable parentId);
}
