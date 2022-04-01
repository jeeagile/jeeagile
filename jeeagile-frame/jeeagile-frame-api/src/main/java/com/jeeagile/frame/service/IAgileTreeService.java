package com.jeeagile.frame.service;

import com.jeeagile.frame.entity.AgileTreeModel;

import java.io.Serializable;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileTreeService<T extends AgileTreeModel> extends IAgileBaseService<T> {

    /**
     * 根据父节点获取下级节点
     *
     * @param parentUuid
     * @return
     */
    List<T> selectChild(Serializable parentUuid);

    /**
     * 根据父节点获取所有下级节点
     *
     * @param parentUuid
     * @return
     */
    List<T> selectAllChild(Serializable parentUuid);

    /**
     * 获取下级节点个数
     *
     * @param parentUuid
     * @return
     */
    long countChild(Serializable parentUuid);

    /**
     * 是否存在下级节点
     *
     * @param parentId
     * @return
     */
    boolean hasChild(Serializable parentId);
}
