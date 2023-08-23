package com.jeeagile.frame.controller.system;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.core.constants.AgileOperateType;
import com.jeeagile.frame.entity.system.AgileSysMenu;
import com.jeeagile.frame.service.system.IAgileSysMenuService;
import com.jeeagile.frame.vo.system.AgileMenuSort;
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
    @AgileLogger(notes = "修改菜单排序", type = AgileOperateType.UPDATE)
    @ApiOperation(value = "修改菜单排序", notes = "修改菜单排序")
    public AgileResult<String> updateSort(@RequestBody List<AgileMenuSort> agileMenuSortList) {
        this.agileService.updateMenuSort(agileMenuSortList);
        return this.success("菜单排序修改成功！");
    }
}

