package com.jeeagile.frame.service;

import com.jeeagile.frame.entity.AgileBaseModel;
import com.jeeagile.frame.mapper.AgileBaseMapper;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public abstract class AgileBaseServiceImpl<M extends AgileBaseMapper<T>, T extends AgileBaseModel> extends AgileServiceImpl<M, T> implements IAgileBaseService<T> {

}
