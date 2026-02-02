package com.jeeagile.process.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.process.entity.AgileProcessOrder;
import com.jeeagile.process.vo.AgileProcessHistory;
import com.jeeagile.process.vo.OnlineOrderQueryParam;

import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2022-06-14
 * @description 流程实例
 */
public interface IAgileProcessOrderService extends IAgileBaseService<AgileProcessOrder> {
    /**
     * 启动流程
     *
     * @param processDefinitionId 流程定义ID
     * @param formData            表单数据
     * @return
     */
    boolean startProcess(String processDefinitionId, Map<String, Object> orderData);

    /**
     * 查询当前用户发起的流程
     *
     * @param agilePageable
     * @return
     */
    AgilePage<AgileProcessOrder> selectOrderPage(AgilePageable<AgileProcessOrder> agilePageable);

    /**
     * 查询流程实例信息
     *
     * @param orderId
     * @return
     */
    AgileProcessOrder selectOrderInfo(String orderId);

    /**
     * 查询流程实例审批历史
     *
     * @param orderId
     * @return
     */
    List<AgileProcessHistory> selectOrderHistory(String orderId);


    /**
     * 撤销流程实例
     *
     * @param orderId
     * @return
     */
    boolean cancelOrder(String orderId);

    /**
     * 获取流程实例在线表单列表数据
     *
     * @param agilePageable
     * @return
     */
    AgilePage<Map> selectOnlineOrderList(AgilePageable<OnlineOrderQueryParam> agilePageable);
}
