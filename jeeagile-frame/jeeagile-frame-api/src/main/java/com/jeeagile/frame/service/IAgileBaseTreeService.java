package com.jeeagile.frame.service;

import com.jeeagile.frame.entity.AgileBaseTreeModel;

import java.io.Serializable;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileBaseTreeService<T extends AgileBaseTreeModel> extends IAgileBaseService<T> {

    /**
     * 根据父节点获取下级节点
     * @param parentUuid
     * @return
     */
    List<T> selectChildList(Serializable parentUuid);

    /**
     * 根据父节点获取下级节点
     * @param parentUuid
     * @return
     */
    long countChild(Serializable parentUuid);
}
