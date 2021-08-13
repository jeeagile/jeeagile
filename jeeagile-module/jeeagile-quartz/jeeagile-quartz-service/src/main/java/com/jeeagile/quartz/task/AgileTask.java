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

    public void task() {
        log.debug("定时任务测试！");
    }

    public void task(int param) {
        log.debug("定时任务测试，参数：" + param);
    }

    public void task(int paramOne, boolean paramTwo) {
        log.debug("定时任务测试，参数：" + paramOne + "----" + paramTwo);
    }

    public void task(int paramOne, double paramTwo, Map paramThree) {
        log.debug("定时任务测试，参数：" + paramOne + "----" + paramTwo + "-----" + paramThree.toString());
    }
}
