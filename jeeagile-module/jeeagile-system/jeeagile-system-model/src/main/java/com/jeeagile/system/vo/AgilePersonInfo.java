package com.jeeagile.system.vo;

import com.jeeagile.system.entity.AgileSysUser;
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
public class AgilePersonInfo extends AgileSysUser {
    /**
     * 分配角色列表
     */
    private List<String> roleNameList = new ArrayList<>();
    /**
     * 分配岗位列表
     */
    private List<String> postNameList = new ArrayList<>();
}
