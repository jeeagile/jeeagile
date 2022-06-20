package com.jeeagile.process.service;


import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.process.entity.AgileProcessTask;

/**
 * @author JeeAgile
 * @date 2022-06-10
 * @description 我的事务
 */
public interface IAgileProcessTaskService  extends IAgileBaseService<AgileProcessTask> {
    /**
     * 查询我的代办
     *
     * @param agilePageable
     * @return
     */
    AgilePage<AgileProcessTask> selectTodo(AgilePageable<AgileProcessTask> agilePageable);

    /**
     * 查询我的代办
     *
     * @param agilePageable
     * @return
     */
    AgilePage<AgileProcessTask> selectDone(AgilePageable<AgileProcessTask> agilePageable);

}
