package com.jeeagile.process.support.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

public class AgileActivitiEventListener implements ActivitiEventListener {
    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        switch (activitiEvent.getType()) {
            //流程开始
            case PROCESS_STARTED:
                System.out.println("流程开始");
                break;
            //流程结束
            case PROCESS_COMPLETED:
                System.out.println("流程结束");
                break;
            //任务开始
            case TASK_CREATED:
                System.out.println("任务开始");
                break;
            //任务完成
            case TASK_COMPLETED:
                System.out.println("任务完成");
                break;
            //进程取消，删除实例
            case PROCESS_CANCELLED:
                System.out.println("进程取消，删除实例");
            default:
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
