package com.jeeagile.process.service;

import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.system.AgileSysUser;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.service.system.IAgileSysUserService;
import com.jeeagile.process.entity.AgileProcessInstance;
import com.jeeagile.process.mapper.AgileProcessInstanceMapper;
import com.jeeagile.process.support.IAgileProcessService;
import com.jeeagile.process.vo.AgileProcessHistory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2022-06-14
 * @description 流程实例
 */
@AgileService
public class AgileProcessInstanceServiceImpl extends AgileBaseServiceImpl<AgileProcessInstanceMapper, AgileProcessInstance> implements IAgileProcessInstanceService {
    @Autowired
    private IAgileProcessService agileProcessService;
    @Autowired
    private IAgileSysUserService agileSysUserService;

    @Override
    public AgileProcessInstance selectProcessInstanceInfo(String processInstanceId) {
        AgileProcessInstance agileProcessInstance = this.getById(processInstanceId);
        if (agileProcessInstance == null && agileProcessInstance.isEmptyPk()) {
            throw new AgileFrameException("流程实例已不存在！");
        }
        agileProcessInstance.setHighLineData(agileProcessService.getProcessInstanceHighLineData(agileProcessInstance.getDefinitionId(), agileProcessInstance.getInstanceId()));
        return agileProcessInstance;
    }

    @Override
    public List<AgileProcessHistory> selectProcessInstanceHistory(String processInstanceId) {
        AgileProcessInstance agileProcessInstance = this.getById(processInstanceId);
        if (agileProcessInstance == null && agileProcessInstance.isEmptyPk()) {
            throw new AgileFrameException("流程实例已不存在！");
        }
        List<AgileProcessHistory> agileProcessHistoryList = agileProcessService.getProcessInstanceHistoric(agileProcessInstance.getInstanceId());
        agileProcessHistoryList.forEach(agileProcessHistory -> {
            //执行人
            String assignee = agileProcessHistory.getAssignee();
            if (agileProcessHistory.getActivityType().equals("startEvent")) {
                assignee = agileProcessInstance.getStartUser();
            }
            if (AgileStringUtil.isNotEmpty(assignee)) {
                AgileSysUser agileSysUser = agileSysUserService.getById(assignee);
                if (agileSysUser != null && agileSysUser.isNotEmptyPk()) {
                    agileProcessHistory.setAssigneeName(agileSysUser.getNickName());
                }
            }
        });
        return agileProcessHistoryList;
    }

    @Override
    public boolean cancelProcessInstance(String processInstanceId) {
        AgileProcessInstance agileProcessInstance = this.getById(processInstanceId);
        if (agileProcessInstance == null && agileProcessInstance.isEmptyPk()) {
            throw new AgileFrameException("流程实例已不存在！");
        }
        agileProcessService.cancelProcessInstance(agileProcessInstance.getInstanceId(), "发起人撤销流程");
        agileProcessInstance.setInstanceStatus("0");
        agileProcessInstance.setEndTime(new Date());
        agileProcessInstance.setUpdateValue();
        return this.updateById(agileProcessInstance);
    }
}
