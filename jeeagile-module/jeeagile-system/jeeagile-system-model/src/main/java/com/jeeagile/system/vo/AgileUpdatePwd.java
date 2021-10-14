package com.jeeagile.system.vo;

import com.jeeagile.frame.vo.AgileBaseVo;
import jakarta.validation.constraints.NotNull;
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
public class AgileUpdatePwd extends AgileBaseVo {
    /**
     * 用户ID
     */
    @NotNull
    private String userId;
    /**
     * 用户老密码
     */
    private String oldPwd;
    /**
     * 用户密码
     */
    @NotNull
    private String newPwd;
}
