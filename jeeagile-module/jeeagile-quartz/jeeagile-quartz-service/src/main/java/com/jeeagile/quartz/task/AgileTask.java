package com.jeeagile.quartz.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Slf4j
@Component("agileTask")
public class AgileTask {
    public void text() {
        log.debug("定时任务测试！");
    }

    public void text(int ss) {
        log.debug("定时任务测试！" + ss);
    }

    public void text(int ss, String ssss) {
        log.debug("定时任务测试！" + ss + ":" + ssss);
    }

    public void text(int ss, String ssss, Map param) {
        log.debug("定时任务测试！" + ss + ":" + ssss + ":" + param.toString());
    }
}
