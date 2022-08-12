package com.jeeagile.process.service;

import com.jeeagile.frame.entity.system.*;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.process.entity.AgileProcessExpression;
import com.jeeagile.process.entity.AgileProcessScript;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2022-06-07
 * @description 流程设计
 */
public interface IAgileProcessDesignerService {
    /**
     * 分页查询用户列表
     *
     * @param agilePageable
     * @return
     */
    AgilePage<AgileSysUser> selectUserPage(AgilePageable<AgileSysUser> agilePageable);

    /**
     * 分页查询角色列表
     *
     * @param agilePageable
     * @return
     */
    AgilePage<AgileSysRole> selectRolePage(AgilePageable<AgileSysRole> agilePageable);

    /**
     * 分页查询部门列表
     *
     * @param agilePageable
     * @return
     */
    AgilePage<AgileSysDept> selectDeptPage(AgilePageable<AgileSysDept> agilePageable);

    /**
     * 分页查询岗位列表
     *
     * @param agilePageable
     * @return
     */
    AgilePage<AgileSysPost> selectPostPage(AgilePageable<AgileSysPost> agilePageable);

    /**
     * 分页查询用户分组列表
     *
     * @param agilePageable
     * @return
     */
    AgilePage<AgileSysGroup> selectGroupPage(AgilePageable<AgileSysGroup> agilePageable);

    /**
     * 分页查询流程表达式列表
     *
     * @param agilePageable
     * @return
     */
    AgilePage<AgileProcessExpression> selectExpressionPage(AgilePageable<AgileProcessExpression> agilePageable);

    /**
     * 分页查询流程脚本列表
     *
     * @param agilePageable
     * @return
     */
    AgilePage<AgileProcessScript> selectScriptPage(AgilePageable<AgileProcessScript> agilePageable);

    /**
     * 根据用户ID获取用户昵称
     *
     * @param userIds 逗号隔开
     * @return
     */
    List<String> detailUserNickName(List<String> userIds);

    /**
     * 根据角色编码获取角色名称
     *
     * @param roleIds 逗号隔开
     * @return
     */
    List<String> detailRoleName(List<String> roleIds);

    /**
     * 根据部门编码获取部门名称
     *
     * @param deptIds 逗号隔开
     * @return
     */
    List<String> detailDeptName(List<String> deptIds);


    /**
     * 根据岗位编码获取岗位名称
     *
     * @param postIds 逗号隔开
     * @return
     */
    List<String> detailPostName(List<String> postIds);


    /**
     * 根据用户分组编码获取用户分组名称
     *
     * @param groupIds 逗号隔开
     * @return
     */
    List<String> detailGroupName(List<String> groupIds);


    /**
     * 根据脚本编码获取脚本名称
     *
     * @param scriptCodes 逗号隔开
     * @return
     */
    List<String> detailScriptName(List<String> scriptCodes);
}
