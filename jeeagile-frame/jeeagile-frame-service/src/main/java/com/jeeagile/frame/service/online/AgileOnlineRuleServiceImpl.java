package com.jeeagile.frame.service.online;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.entity.online.AgileOnlineRule;
import com.jeeagile.frame.mapper.online.AgileOnlineRuleMapper;

/**
 * @author JeeAgile
 * @date 2023-07-19
 * @description 在线表单 规则配置 接口实现
 */
@AgileService
public class AgileOnlineRuleServiceImpl extends AgileBaseServiceImpl<AgileOnlineRuleMapper, AgileOnlineRule> implements IAgileOnlineRuleService {

    @Override
    public LambdaQueryWrapper<AgileOnlineRule> queryWrapper(AgileOnlineRule agileOnlineRule) {
        LambdaQueryWrapper<AgileOnlineRule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileOnlineRule != null) {
            if (AgileStringUtil.isNotEmpty(agileOnlineRule.getRuleName())) {
                lambdaQueryWrapper.like(AgileOnlineRule::getRuleName, agileOnlineRule.getRuleName());
            }
            if (AgileStringUtil.isNotEmpty(agileOnlineRule.getRuleType())) {
                lambdaQueryWrapper.eq(AgileOnlineRule::getRuleType, agileOnlineRule.getRuleType());
            }
        }
        return lambdaQueryWrapper;
    }

}
