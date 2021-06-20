package com.jeeagile.system.service;

import com.jeeagile.frame.service.IAgileBaseTreeService;
import com.jeeagile.system.entity.AgileSysDept;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysDeptService extends IAgileBaseTreeService<AgileSysDept> {
    /**
     * 获取部门列表
     */
    List<AgileSysDept> selectDeptList(AgileSysDept agileSysDept);

    /**
     * 查看部门信息
     */
    AgileSysDept selectDeptById(String deptId);

    /**
     * 根据部门ID获取部门名称
     */
    String getDeptNameById(String deptId);

    /**
     * 保存部门信息
     */
    AgileSysDept saveDept(AgileSysDept agileSysDept);

    /**
     * 修改部门信息
     */
    boolean updateDeptById(AgileSysDept agileSysDept);

    /**
     * 删除部门信息
     */
    boolean deleteDeptById(String deptId);
}
