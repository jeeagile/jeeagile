package com.jeeagile.frame.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeeagile.frame.entity.AgileBaseModel;
import com.jeeagile.frame.entity.AgileModel;
import com.jeeagile.frame.mapper.AgileBaseMapper;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public abstract class AgileBaseServiceImpl<M extends AgileBaseMapper<T>, T extends AgileModel> extends ServiceImpl<M, T> implements IAgileBaseService<T> {

}
