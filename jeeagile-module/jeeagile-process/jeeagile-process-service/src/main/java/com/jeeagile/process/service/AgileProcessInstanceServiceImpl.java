package com.jeeagile.process.service;

import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.process.entity.AgileProcessInstance;
import com.jeeagile.process.mapper.AgileProcessInstanceMapper;

/**
 * @author JeeAgile
 * @date 2022-06-14
 * @description 流程实例
 */
@AgileService
public class AgileProcessInstanceServiceImpl extends AgileBaseServiceImpl<AgileProcessInstanceMapper, AgileProcessInstance> implements IAgileProcessInstanceService {

}
