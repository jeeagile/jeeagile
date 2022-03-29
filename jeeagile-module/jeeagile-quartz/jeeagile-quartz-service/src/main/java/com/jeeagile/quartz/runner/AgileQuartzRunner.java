package com.jeeagile.quartz.runner;

import com.jeeagile.quartz.entity.AgileQuartzJob;
import com.jeeagile.quartz.schedule.AgileScheduleUtil;
import com.jeeagile.quartz.service.IAgileQuartzJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AgileQuartzRunner implements ApplicationRunner {
    @Autowired
    private IAgileQuartzJobService agileQuartzJobService;

    @Override
    public void run(ApplicationArguments applicationArguments) {
        AgileScheduleUtil.clear();
        List<AgileQuartzJob> agileQuartzJobList = agileQuartzJobService.list();
        agileQuartzJobList.forEach(agileQuartzJob -> AgileScheduleUtil.createScheduleJob(agileQuartzJob));
    }
}
