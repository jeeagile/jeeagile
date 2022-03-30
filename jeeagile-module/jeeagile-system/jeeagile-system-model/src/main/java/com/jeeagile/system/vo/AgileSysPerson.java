package com.jeeagile.system.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jeeagile.system.entity.AgileSysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 个人用户信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileSysPerson extends AgileSysUser {
    /**
     * 用户已分配角色名称
     */
    @ApiModelProperty(value = "用户已分配角色名称", position = 15)
    @TableField(exist = false)
    private List<String> roleNameList = new ArrayList<>();
    /**
     * 用户已分配岗位名称
     */
    @ApiModelProperty(value = "用户已分配岗位名称", position = 16)
    @TableField(exist = false)
    private List<String> postNameList = new ArrayList<>();
}
