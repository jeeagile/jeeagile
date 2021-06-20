package com.jeeagile.frame.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeeagile.frame.entity.AgileModel;
import com.jeeagile.frame.mapper.AgileMapper;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public abstract class AgileServiceImpl<M extends AgileMapper<T>, T extends AgileModel> extends ServiceImpl<M, T> implements IAgileService<T> {

}
