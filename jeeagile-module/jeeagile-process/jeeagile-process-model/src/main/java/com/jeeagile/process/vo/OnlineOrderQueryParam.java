package com.jeeagile.process.vo;

import com.jeeagile.frame.vo.online.OnlineQueryParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OnlineOrderQueryParam extends OnlineQueryParam {
    @ApiModelProperty(value = "在线工单页面ID")
    private String orderPageId;
    @ApiModelProperty(value = "模型ID")
    private String processId;
    @ApiModelProperty(value = "流程实例状态")
    private String instanceStatus;
    @ApiModelProperty(value = "流程实例创建时间起")
    private Date createTimeStart;
    @ApiModelProperty(value = "流程实例创建时间止")
    private Date createTimeEnd;
}
