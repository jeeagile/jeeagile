package com.jeeagile.frame.vo.online;

import com.jeeagile.frame.entity.online.AgileOnlineColumn;
import com.jeeagile.frame.entity.online.AgileOnlineDict;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OnlineTableColumn extends AgileOnlineColumn {
    /**
     * 表字段ID
     */
    @ApiModelProperty("表字段ID")
    private String columnId;
    /**
     * 字典数据信息
     */
    @ApiModelProperty("字典数据信息")
    private AgileOnlineDict onlineDict;

    /**
     * 校验规则
     */
    @ApiModelProperty("校验规则")
    private List<Object> ruleList;

}
