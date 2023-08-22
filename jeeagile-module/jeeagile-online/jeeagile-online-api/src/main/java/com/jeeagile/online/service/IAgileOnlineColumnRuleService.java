package com.jeeagile.online.service;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.online.entity.AgileOnlineColumnRule;
import com.jeeagile.online.vo.OnlineColumnRule;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2023-08-21
 * @description 在线表单 表单数据表字段规则配置 接口
 */
public interface IAgileOnlineColumnRuleService extends IAgileBaseService<AgileOnlineColumnRule> {
    /**
     * 获取字段配置列表
     *
     * @param columnId
     * @return
     */
    List<OnlineColumnRule> columnRuleList(String columnId);
}
