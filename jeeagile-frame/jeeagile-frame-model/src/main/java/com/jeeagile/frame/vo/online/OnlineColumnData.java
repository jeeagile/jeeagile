package com.jeeagile.frame.vo.online;

import com.jeeagile.frame.entity.online.AgileOnlineColumn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OnlineColumnData extends AgileOnlineColumn {
    /**
     * 字段值
     */
    @ApiModelProperty(value = "字段值")
    private Object columnValue;
}
