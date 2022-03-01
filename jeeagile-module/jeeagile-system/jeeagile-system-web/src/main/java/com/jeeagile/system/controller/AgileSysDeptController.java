package com.jeeagile.system.controller;

import com.jeeagile.core.protocol.annotation.AgileProvider;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.system.entity.AgileSysDept;
import com.jeeagile.system.service.IAgileSysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 部门管理 前端控制器
 */
@RestController
@RequestMapping("/system/dept")
@Api(value = "部门管理", tags = "部门管理")
public class AgileSysDeptController extends AgileBaseController {

    @AgileProvider
    private IAgileSysDeptService agileSysDeptService;

    @PostMapping("/selectList")
    @ApiOperation(value = "查询部门列表", notes = "查询部门列表")
    @AgileLogger(title = "查询部门列表", type = AgileLoggerType.SELECT)
    public AgileResult<List<AgileSysDept>> selectList(@RequestBody AgileSysDept agileSysDept) {
        return this.rtnSuccess(agileSysDeptService.selectDeptList(agileSysDept));
    }

    @PostMapping("/detail")
    @ApiOperation(value = "根据部门ID查询部门详细信息", notes = "根据部门ID查询部门详细信息")
    @AgileLogger(title = "查看部门信息", type = AgileLoggerType.DETAIL)
    public AgileResult<AgileSysDept> detail(@SingleRequestBody String deptId) {
        return this.rtnSuccess(agileSysDeptService.selectDeptById(deptId));
    }

    @AgileDemo
    @PostMapping("/add")
    @ApiOperation(value = "新增部门", notes = "新增部门")
    @AgileLogger(title = "新增部门", type = AgileLoggerType.ADD)
    @AgileRequiresPermissions("system:dept:add")
    public AgileResult<AgileSysDept> add(@RequestBody AgileSysDept agileSysDept) {
        return this.rtnSuccess(agileSysDeptService.saveDept(agileSysDept));
    }

    @AgileDemo
    @PostMapping("/update")
    @ApiOperation(value = "修改部门信息", notes = "修改部门信息")
    @AgileLogger(title = "修改部门信息", type = AgileLoggerType.UPDATE)
    @AgileRequiresPermissions("system:dept:update")
    public <T> AgileResult<T> update(@RequestBody AgileSysDept agileSysDept) {
        agileSysDeptService.updateDeptById(agileSysDept);
        return this.rtnSuccess();
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除部门信息", notes = "删除部门信息")
    @AgileLogger(title = "删除部门信息", type = AgileLoggerType.DELETE)
    @AgileRequiresPermissions("system:dept:delete")
    public AgileResult<AgileSysDept> delete(@SingleRequestBody String deptId) {
        agileSysDeptService.deleteDeptById(deptId);
        return this.rtnSuccess();
    }

}

