package com.jeeagile.process.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jeeagile.frame.entity.AgileBaseTenantModel;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2022-06-06
 * @description 流程模型
 */
@Data
public class AgileProcessOrder extends AgileBaseTenantModel<AgileProcessOrder> {
    /**
     * 流程编码
     */
    private String processId;
    /**
     * 流程编码
     */
    private String processCode;
    /**
     * 流程流程名称
     */
    private String processName;
    /**
     * 流程设计XMl
     */
    private String processXml;
    /**
     * 流程定义ID
     */
    private String definitionId;
    /**
     * 流程实例ID
     */
    private String instanceId;
    /**
     * 流程表单类型（1:流程表单 2:业务表单）
     */
    private String formType;
    /**
     * 流程表单名称
     */
    private String formName;
    /**
     * 流程表单配置
     */
    private String formConf;
    /**
     * 流程表单字段
     */
    private String formFields;
    /**
     * 流程表单地址
     */
    private String formUrl;
    /**
     * 流程表单数据
     */
    private String formData;
    /**
     * 在线表单页面ID
     */
    private String pageId;
    /**
     * 在线表单页面主键值
     */
    private String pageKey;
    /**
     * 工单状态
     */
    private String orderStatus;
    /**
     * 流程实例发起人员
     */
    private String startUser;
    /**
     * 流程实例发起人员名称
     */
    private String startUserName;
    /**
     * 流程实例启动时间
     */
    private Date startTime;
    /**
     * 流程实例结束时间
     */
    private Date endTime;

    /**
     * 流程在线表单数据
     */
    @TableField(exist = false)
    private Map<String, Object> pageData;

    /**
     * 流程实例高亮线
     */
    @TableField(exist = false)
    private Map<String, Object> highLineData;

}
