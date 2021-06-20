package com.jeeagile.system.vo;

import com.jeeagile.frame.vo.AgileBaseVo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
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
