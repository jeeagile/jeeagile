package com.jeeagile.frame.service;

import com.jeeagile.frame.entity.AgileModel;
import com.jeeagile.frame.mapper.AgileMapper;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public abstract class AgileBaseServiceImpl<M extends AgileMapper<T>, T extends AgileModel> extends AgileServiceImpl<M, T> implements IAgileBaseService<T> {

}
