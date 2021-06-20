package com.jeeagile.quartz.vo;

import com.jeeagile.frame.vo.AgileBaseVo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileUpdateStatus extends AgileBaseVo {

    /**
     * 表主键ID
     */
    @NotNull
    private String id;

    /**
     * 状态（0正常 1停用 )
     */
    @Pattern(regexp = "[01]", message = "状态必须为0或1（0正常 1停用）！")
    private String status;

}
