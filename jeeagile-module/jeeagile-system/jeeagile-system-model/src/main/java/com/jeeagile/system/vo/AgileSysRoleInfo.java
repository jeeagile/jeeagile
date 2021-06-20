package com.jeeagile.system.vo;

import com.jeeagile.system.entity.AgileSysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileSysRoleInfo extends AgileSysRole {
    /**
     * 角色菜单ID集合
     */
    private List<String> menuIdList = new ArrayList<>();

    /**
     * 角色部门ID集合
     */
    private List<String> deptIdList = new ArrayList<>();
}
