package com.jeeagile.frame.service.online;

import com.jeeagile.frame.entity.online.AgileOnlinePage;
import com.jeeagile.frame.service.IAgileBaseService;

/**
 * @author JeeAgile
 * @date 2023-07-31
 * @description 在线表单 表单页面 接口
 */
public interface IAgileOnlinePageService extends IAgileBaseService<AgileOnlinePage> {
    /**
     * 在线表单设计
     *
     * @param agileOnlinePage
     * @return
     */
    boolean designer(AgileOnlinePage agileOnlinePage);
}
