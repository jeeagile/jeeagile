package com.jeeagile.process.service;

import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.process.entity.AgileProcessTask;

/**
 * @author JeeAgile
 * @date 2022-06-10
 * @description 我的事务
 */
@AgileService
public class AgileProcessTaskServiceImpl implements IAgileProcessTaskService {


    @Override
    public AgilePage<AgileProcessTask> selectTodo(AgilePageable<AgileProcessTask> agilePageable) {
        return null;
    }
}
