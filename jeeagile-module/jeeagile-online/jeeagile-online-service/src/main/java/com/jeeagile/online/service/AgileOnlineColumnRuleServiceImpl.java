package com.jeeagile.online.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.online.entity.AgileOnlineColumnRule;
import com.jeeagile.online.entity.AgileOnlineRule;
import com.jeeagile.online.mapper.AgileOnlineColumnRuleMapper;
import com.jeeagile.online.vo.OnlineColumnRule;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2023-08-21
 * @description 在线表单 表单数据表字段规则配置 接口实现
 */
@AgileService
public class AgileOnlineColumnRuleServiceImpl extends AgileBaseServiceImpl<AgileOnlineColumnRuleMapper, AgileOnlineColumnRule> implements IAgileOnlineColumnRuleService {

    @Autowired
    private IAgileOnlineRuleService agileOnlineRuleService;

    @Override
    public List<OnlineColumnRule> columnRuleList(String columnId) {
        LambdaQueryWrapper<AgileOnlineColumnRule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileOnlineColumnRule::getColumnId, columnId);
        List<AgileOnlineColumnRule> onlineTableColumnRuleList = this.list(lambdaQueryWrapper);
        List<OnlineColumnRule> onlineColumnRuleList = new ArrayList<>();
        for (AgileOnlineColumnRule agileOnlineColumnRule : onlineTableColumnRuleList) {
            OnlineColumnRule onlineColumnRule = new OnlineColumnRule();
            BeanUtils.copyProperties(agileOnlineColumnRule, onlineColumnRule);
            AgileOnlineRule agileOnlineRule = agileOnlineRuleService.getById(onlineColumnRule.getRuleId());
            if (agileOnlineRule == null || agileOnlineRule.isEmptyPk()) {
                continue;
            }
            BeanUtils.copyProperties(agileOnlineRule, onlineColumnRule);
            onlineColumnRule.setId(agileOnlineColumnRule.getId());
            onlineColumnRuleList.add(onlineColumnRule);
        }
        return onlineColumnRuleList;
    }
}
