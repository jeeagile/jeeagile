package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.online.AgileOnlinePage;
import com.jeeagile.frame.entity.online.AgileOnlineTable;
import com.jeeagile.frame.entity.system.AgileSysUser;
import com.jeeagile.frame.mapper.online.AgileOnlineOperationMapper;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.service.online.IAgileOnlineOperationService;
import com.jeeagile.frame.service.online.IAgileOnlinePageService;
import com.jeeagile.frame.service.online.IAgileOnlineTableService;
import com.jeeagile.frame.service.system.IAgileSysUserService;
import com.jeeagile.frame.vo.online.OnlineFieldFilter;
import com.jeeagile.frame.vo.online.OnlineJoinTable;
import com.jeeagile.process.constants.ProcessFormType;
import com.jeeagile.process.constants.ProcessOrderStatus;
import com.jeeagile.process.entity.AgileProcessDefinition;
import com.jeeagile.process.entity.AgileProcessOrder;
import com.jeeagile.process.mapper.AgileProcessOrderMapper;
import com.jeeagile.process.support.IAgileProcessService;
import com.jeeagile.process.vo.AgileProcessHistory;
import com.jeeagile.process.vo.OnlineOrderQueryParam;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author JeeAgile
 * @date 2022-06-14
 * @description 流程实例
 */
@AgileService
public class AgileProcessOrderServiceImpl extends AgileBaseServiceImpl<AgileProcessOrderMapper, AgileProcessOrder> implements IAgileProcessOrderService {
    @Autowired
    private AgileOnlineOperationMapper agileOnlineOperationMapper;
    @Autowired
    private IAgileProcessDefinitionService agileProcessDefinitionService;
    @Autowired
    private IAgileProcessService agileProcessService;
    @Autowired
    private IAgileSysUserService agileSysUserService;
    @Autowired
    private IAgileOnlinePageService agileOnlinePageService;
    @Autowired
    private IAgileOnlineTableService agileOnlineTableService;
    @Autowired
    private IAgileOnlineOperationService agileOnlineOperationService;
    @Autowired
    private IAgileProcessOrderScopeService agileProcessOrderScopeService;


    @Override
    public boolean startProcess(String processDefinitionId, Map<String, Object> orderData) {
        AgileProcessDefinition agileProcessDefinition = agileProcessDefinitionService.selectModel(processDefinitionId);
        if (agileProcessDefinition == null || agileProcessDefinition.isEmptyPk()) {
            throw new AgileValidateException("流程定义已不存在！");
        }
        if (!agileProcessService.checkProcessDefinition(agileProcessDefinition.getId())) {
            throw new AgileValidateException("流程定义校验未通过，不能发起流程！");
        }
        if (ProcessFormType.PROCESS_FORM.equals(agileProcessDefinition.getFormType())) {
            String instanceId = agileProcessService.startProcess(agileProcessDefinition.getId(), orderData);
            return AgileStringUtil.isNotEmpty(instanceId);
        } else if (ProcessFormType.BUSINESS_FORM.equals(agileProcessDefinition.getFormType())) {
            return true;
        } else if (ProcessFormType.ONLINE_FORM.equals(agileProcessDefinition.getFormType())) {
            if (AgileStringUtil.isEmpty(orderData)) {
                throw new AgileValidateException("请填写表单数据！");
            }
            // 保存表单数据 并返回表单主键ID
            Object pageKey = agileOnlineOperationService.saveTableData((String) orderData.get("tableId"), (Map) orderData.get("masterData"), (Map) orderData.get("slaveData"));
            if (AgileStringUtil.isNotEmpty(pageKey)) {
                Map<String, Object> variablesMap = new HashMap();
                variablesMap.put("pageKey", pageKey);
                String instanceId = agileProcessService.startProcess(agileProcessDefinition.getId(), variablesMap);
                if (AgileStringUtil.isNotEmpty(instanceId)) {

                } else {
                    throw new AgileValidateException("流程启动异常！");
                }
            } else {
                throw new AgileValidateException("表单数据保存异常！");
            }
            return true;
        } else {
            throw new AgileValidateException("未知表单类型！");
        }
    }

    @Override
    public AgilePage<AgileProcessOrder> selectOrderPage(AgilePageable<AgileProcessOrder> agilePageable) {
        AgilePage<AgileProcessOrder> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileProcessOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileProcessOrder.class, i -> !"processXml".contains(i.getProperty()) || !"formFields".contains(i.getProperty()) || !"formConfig".contains(i.getProperty()));
        AgileProcessOrder agileProcessOrder = agilePageable.getQueryCond();
        if (agileProcessOrder != null) {
            if (AgileStringUtil.isNotEmpty(agileProcessOrder.getProcessCode())) {
                lambdaQueryWrapper.eq(AgileProcessOrder::getProcessCode, agileProcessOrder.getProcessCode());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessOrder.getOrderStatus())) {
                lambdaQueryWrapper.eq(AgileProcessOrder::getOrderStatus, agileProcessOrder.getOrderStatus());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessOrder.getProcessName())) {
                lambdaQueryWrapper.like(AgileProcessOrder::getProcessName, agileProcessOrder.getProcessName());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessOrder.getFormName())) {
                lambdaQueryWrapper.like(AgileProcessOrder::getFormName, agileProcessOrder.getFormName());
            }
        }
        lambdaQueryWrapper.eq(AgileProcessOrder::getStartUser, AgileSecurityContext.getUserId());
        lambdaQueryWrapper.orderByDesc(AgileProcessOrder::getStartTime);
        return this.page(agilePage, lambdaQueryWrapper);
    }


    @Override
    public AgileProcessOrder selectOrderInfo(String orderId) {
        AgileProcessOrder agileProcessOrder = this.getById(orderId);
        if (agileProcessOrder == null || agileProcessOrder.isEmptyPk()) {
            throw new AgileFrameException("工单已不存在！");
        }
        if (ProcessFormType.ONLINE_FORM.equals(agileProcessOrder.getFormType())) {
            AgileOnlinePage agileOnlinePage = agileOnlinePageService.selectModel(agileProcessOrder.getPageId());
            agileProcessOrder.setPageData(agileOnlineOperationService.selectOneData(agileOnlinePage.getTableId(), agileProcessOrder.getPageKey()));
        }
        agileProcessOrder.setHighLineData(agileProcessService.getProcessInstanceHighLineData(agileProcessOrder.getDefinitionId(), agileProcessOrder.getInstanceId()));
        return agileProcessOrder;
    }

    @Override
    public List<AgileProcessHistory> selectOrderHistory(String orderID) {
        AgileProcessOrder agileProcessOrder = this.getById(orderID);
        if (agileProcessOrder == null || agileProcessOrder.isEmptyPk()) {
            throw new AgileFrameException("工单已不存在！");
        }
        List<AgileProcessHistory> agileProcessHistoryList = agileProcessService.getProcessInstanceHistoric(agileProcessOrder.getInstanceId());
        agileProcessHistoryList.forEach(agileProcessHistory -> {
            //执行人
            String assignee = agileProcessHistory.getAssignee();
            if (agileProcessHistory.getActivityType().equals("startEvent")) {
                assignee = agileProcessOrder.getStartUser();
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
    public boolean cancelOrder(String orderID) {
        AgileProcessOrder agileProcessOrder = this.getById(orderID);
        if (agileProcessOrder == null || agileProcessOrder.isEmptyPk()) {
            throw new AgileFrameException("流程实例已不存在！");
        }
        agileProcessService.cancelProcessInstance(agileProcessOrder.getInstanceId(), "发起人撤销流程");
        agileProcessOrder.setOrderStatus(ProcessOrderStatus.CANCEL);
        agileProcessOrder.setEndTime(new Date());
        agileProcessOrder.setUpdateNullValue();
        return this.updateById(agileProcessOrder);
    }

    @Override
    public AgilePage<Map> selectOnlineOrderList(AgilePageable<OnlineOrderQueryParam> agilePageable) {
        OnlineOrderQueryParam onlineOrderQueryParam = agilePageable.getQueryCond();
        if (onlineOrderQueryParam == null || AgileStringUtil.isEmpty(onlineOrderQueryParam.getOrderPageId())) {
            throw new AgileValidateException("在线工单页面ID不能为空！");
        }
        if (AgileStringUtil.isEmpty(onlineOrderQueryParam.getProcessId())) {
            throw new AgileValidateException("流程ID不能为空！");
        }
        AgileOnlinePage agileOnlinePage = agileOnlinePageService.getById(onlineOrderQueryParam.getOrderPageId());
        if (agileOnlinePage == null || agileOnlinePage.isEmptyPk()) {
            throw new AgileValidateException("在线工单页面已不存在！");
        }
        onlineOrderQueryParam.setTableId(agileOnlinePage.getTableId());
        AgileOnlineTable agileOnlineTable = agileOnlineTableService.getById(agileOnlinePage.getTableId());
        if (agileOnlineTable == null || agileOnlineTable.isEmptyPk()) {
            throw new AgileValidateException("数据表已不存在！");
        }
        Map<String, Object> queryParam = agileOnlineOperationService.makeQueryParam(agileOnlineTable, onlineOrderQueryParam);
        String selectFields = (String) queryParam.get("selectFields");
        String orderBy = (String) queryParam.get("orderBy");
        List<OnlineJoinTable> joinTableList = (List<OnlineJoinTable>) queryParam.get("joinTableList");
        StringBuilder selectFieldsBuilder = new StringBuilder();
        // 添加查询字段
        selectFieldsBuilder.append(selectFields).append(",");
        selectFieldsBuilder.append("agile_process_order.id orderId").append(",");
        selectFieldsBuilder.append("agile_process_order.form_name formName").append(",");
        selectFieldsBuilder.append("agile_process_order.page_id pageId").append(",");
        selectFieldsBuilder.append("agile_process_order.page_key pageKey").append(",");
        selectFieldsBuilder.append("agile_process_order.order_status orderStatus").append(",");
        selectFieldsBuilder.append("agile_process_order.task_id taskId").append(",");
        selectFieldsBuilder.append("agile_process_order.start_user_name startUserName").append(",");
        selectFieldsBuilder.append("agile_process_order.start_time startTime");
        // 添加 流程实例关联表
        OnlineJoinTable onlineJoinTable = new OnlineJoinTable();
        onlineJoinTable.setLeftJoin(true);
        onlineJoinTable.setJoinTableName("agile_process_order");
        onlineJoinTable.setJoinCondition("agile_process_order.page_key = " + agileOnlineTable.getTableName() + "." + agileOnlineTable.getPrimaryColumnName());
        joinTableList.add(onlineJoinTable);

        List<OnlineFieldFilter> onlineFieldFilterList = this.makeFieldFilter(onlineOrderQueryParam);

        if (AgileStringUtil.isEmpty(orderBy)) {
            orderBy = "agile_process_order.start_time desc";
        } else {
            orderBy = "agile_process_order.start_time desc," + orderBy;
        }

        AgilePage<Map> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        return agileOnlineOperationMapper.getPageData(agilePage, agileOnlineTable.getTableName(), selectFieldsBuilder.toString(), joinTableList, onlineFieldFilterList, orderBy);
    }

    /**
     * 构建流程相关查询条件
     *
     * @param onlineOrderQueryParam
     * @return
     */
    private List<OnlineFieldFilter> makeFieldFilter(OnlineOrderQueryParam onlineOrderQueryParam) {
        List<OnlineFieldFilter> onlineFieldFilterList = onlineOrderQueryParam.getFilterList();
        if (AgileStringUtil.isEmpty(onlineFieldFilterList)) {
            onlineFieldFilterList = new ArrayList<>();
        }
        // 添加 流程查询条件
        OnlineFieldFilter onlineFieldFilter_processId = new OnlineFieldFilter();
        onlineFieldFilter_processId.setTableName("agile_process_order");
        onlineFieldFilter_processId.setColumnName("process_id");
        onlineFieldFilter_processId.setColumnValue(onlineOrderQueryParam.getProcessId());
        onlineFieldFilterList.add(onlineFieldFilter_processId);

        // 添加流程实例状态查询条件
        if (AgileStringUtil.isNotEmpty(onlineOrderQueryParam.getInstanceStatus())) {
            OnlineFieldFilter onlineFieldFilter_instanceStatus = new OnlineFieldFilter();
            onlineFieldFilter_instanceStatus.setTableName("agile_process_order");
            onlineFieldFilter_instanceStatus.setColumnName("order_status");
            onlineFieldFilter_instanceStatus.setColumnValue(onlineOrderQueryParam.getInstanceStatus());
            onlineFieldFilterList.add(onlineFieldFilter_instanceStatus);
        }
        // 添加流程创建时间查询条件
        if (AgileStringUtil.isNotEmpty(onlineOrderQueryParam.getCreateTimeStart())) {
            OnlineFieldFilter onlineFieldFilter_createTime = new OnlineFieldFilter();
            onlineFieldFilter_createTime.setTableName("agile_process_order");
            onlineFieldFilter_createTime.setColumnName("start_time");
            onlineFieldFilter_createTime.setColumnValueStart(onlineOrderQueryParam.getCreateTimeStart());
            onlineFieldFilter_createTime.setColumnValueEnd(onlineOrderQueryParam.getCreateTimeEnd());
            onlineFieldFilter_createTime.setFilterType("03");
            onlineFieldFilterList.add(onlineFieldFilter_createTime);
        }
        
        // 添加权限过滤：非超级管理员需按权限关联表过滤
        if (!AgileSecurityContext.getUserData().isSuperAdmin()) {
            List<String> visibleOrderIds = agileProcessOrderScopeService.getUserVisibleOrderIds();
            if (AgileCollectionUtil.isNotEmpty(visibleOrderIds)) {
                OnlineFieldFilter onlineFieldFilter_orderId = new OnlineFieldFilter();
                onlineFieldFilter_orderId.setTableName("agile_process_order");
                onlineFieldFilter_orderId.setColumnName("id");
                onlineFieldFilter_orderId.setFilterType("05"); // IN 类型
                onlineFieldFilter_orderId.setColumnValueList(new HashSet<>(visibleOrderIds));
                onlineFieldFilterList.add(onlineFieldFilter_orderId);
            } else {
                // 无权限时返回空结果
                OnlineFieldFilter onlineFieldFilter_empty = new OnlineFieldFilter();
                onlineFieldFilter_empty.setTableName("agile_process_order");
                onlineFieldFilter_empty.setColumnName("id");
                onlineFieldFilter_empty.setFilterType("05");
                onlineFieldFilter_empty.setColumnValueList(new HashSet<>(Arrays.asList("NONE")));
                onlineFieldFilterList.add(onlineFieldFilter_empty);
            }
        }
        
        return onlineFieldFilterList;
    }
}
