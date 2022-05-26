package com.jeeagile.frame.entity.system;

import com.jeeagile.frame.entity.AgileBaseModel;
import jakarta.validation.constraints.NotEmpty;
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
public class AgileSysUserPost extends AgileBaseModel<AgileSysUserPost> {
    /**
     * 用户ID
     */
    @NotEmpty(message = "用户ID不能为空！")
    private String userId;
    /**
     * 岗位ID
     */
    @NotEmpty(message = "岗位ID不能为空！")
    private String postId;

}
