package com.jeeagile.frame.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeeagile.frame.entity.AgileModel;
import com.jeeagile.frame.mapper.AgileBaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public abstract class AgileBaseServiceImpl<M extends AgileBaseMapper<T>, T extends AgileModel> extends ServiceImpl<M, T> implements IAgileBaseService<T> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
}
