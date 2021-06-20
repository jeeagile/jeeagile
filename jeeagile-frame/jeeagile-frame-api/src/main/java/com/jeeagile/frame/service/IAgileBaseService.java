package com.jeeagile.frame.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 接口基类
 */
public interface IAgileBaseService<T> extends IAgileService<T> {
    default AgilePage<T> page(AgilePageable<?> agilePageable, Wrapper<T> queryWrapper) {
        AgilePage<T> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        return this.page(agilePage, queryWrapper);
    }
}
