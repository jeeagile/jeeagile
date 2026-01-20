package com.jeeagile.frame.service.online;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.frame.entity.online.AgileOnlineColumnRule;
import com.jeeagile.frame.vo.online.OnlineColumnRule;

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
