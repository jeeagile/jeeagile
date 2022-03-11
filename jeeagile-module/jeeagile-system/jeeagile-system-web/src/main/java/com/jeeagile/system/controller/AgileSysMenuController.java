package com.jeeagile.system.controller;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.system.entity.AgileSysMenu;
import com.jeeagile.system.service.IAgileSysMenuService;
import com.jeeagile.system.vo.AgileUpdateSort;
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
 * @description 菜单管理 前端控制器
 */
@RestController
@AgileLogger("菜单管理")
@RequestMapping("/system/menu")
@AgilePermissionsPrefix("system:menu")
@Api(value = "菜单管理", tags = "菜单管理")
public class AgileSysMenuController extends AgileCrudController<IAgileSysMenuService, AgileSysMenu> {

    @AgileDemo
    @PostMapping("/updateSort")
    @AgileRequiresPermissions("sort")
    @AgileLogger(notes = "修改菜单排序", type = AgileLoggerType.UPDATE)
    @ApiOperation(value = "修改菜单排序", notes = "修改菜单排序")
    public AgileResult<String> updateSort(@RequestBody List<AgileUpdateSort> agileSortList) {
        this.getAgileService().updateMenuSort(agileSortList);
        return this.success("菜单排序修改成功！");
    }
}

