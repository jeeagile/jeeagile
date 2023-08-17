package com.jeeagile.online.vo;

import com.jeeagile.online.entity.AgileOnlineForm;
import com.jeeagile.online.entity.AgileOnlinePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OnlinePageRender {
    /**
     * 表单信息
     */
    @ApiModelProperty("表单信息")
    private AgileOnlineForm onlineForm;
    /**
     * 表单页面信息
     */
    @ApiModelProperty("表单页面信息")
    private AgileOnlinePage onlinePage;
    /**
     * 表单数据表信息
     */
    @ApiModelProperty("表单页面数据表信息")
    private List<OnlinePageTable> pageTableList;
}
