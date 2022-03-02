package com.jeeagile.frame.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.frame.entity.AgileBaseModel;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 接口基类
 */
public interface IAgileBaseService<T extends AgileBaseModel> extends IAgileService<T> {


}
