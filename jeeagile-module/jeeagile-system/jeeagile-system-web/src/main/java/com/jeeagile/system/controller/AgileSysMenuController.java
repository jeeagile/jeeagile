package com.jeeagile.system.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
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
@RequestMapping("/system/menu")
@Api(value = "菜单管理", tags = "菜单管理")
public class AgileSysMenuController extends AgileBaseController {

    @AgileReference
    private IAgileSysMenuService agileSysMenuService;

    @PostMapping("/selectList")
    @ApiOperation(value = "获取菜单列表", notes = "获取菜单列表")
    @AgileLogger(title = "查询菜单列表", type = AgileLoggerType.SELECT)
    public AgileResult<List<AgileSysMenu>> selectList(@RequestBody AgileSysMenu agileSysMenu) {
        return this.rtnSuccess(agileSysMenuService.selectMenuList(agileSysMenu));
    }

    @PostMapping("/detail")
    @ApiOperation(value = "根据菜单ID获取菜单详细信息", notes = "根据菜单ID获取菜单详细信息")
    @AgileLogger(title = "查看菜单信息", type = AgileLoggerType.DETAIL)
    public AgileResult<AgileSysMenu> detail(@SingleRequestBody String menuId) {
        return this.rtnSuccess(agileSysMenuService.selectMenuById(menuId));
    }

    @AgileDemo
    @PostMapping("/add")
    @ApiOperation(value = "新增菜单", notes = "新增菜单")
    @AgileLogger(title = "新增菜单", type = AgileLoggerType.ADD)
    @AgileRequiresPermissions("system:menu:add")
    public AgileResult<AgileSysMenu> add(@RequestBody AgileSysMenu agileSysMenu) {
        return this.rtnSuccess(agileSysMenuService.saveMenu(agileSysMenu));
    }

    @AgileDemo
    @PostMapping("/update")
    @ApiOperation(value = "修改菜单信息", notes = "修改菜单信息")
    @AgileLogger(title = "修改菜单信息", type = AgileLoggerType.UPDATE)
    @AgileRequiresPermissions("system:menu:update")
    public AgileResult<String> update(@RequestBody AgileSysMenu agileSysMenu) {
        agileSysMenuService.updateMenuById(agileSysMenu);
        return this.rtnSuccess("菜单信息修改成功！");
    }

    @AgileDemo
    @PostMapping("/updateSort")
    @ApiOperation(value = "修改菜单排序", notes = "修改菜单排序")
    @AgileLogger(title = "修改菜单排序", type = AgileLoggerType.UPDATE)
    @AgileRequiresPermissions("system:menu:sort")
    public AgileResult<String> updateSort(@RequestBody List<AgileUpdateSort> agileSortList) {
        agileSysMenuService.updateMenuSort(agileSortList);
        return this.rtnSuccess("菜单排序修改成功！");
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除菜单信息", notes = "删除菜单信息")
    @AgileLogger(title = "删除菜单信息", type = AgileLoggerType.DELETE)
    @AgileRequiresPermissions("system:menu:delete")
    public AgileResult<String> delete(@SingleRequestBody String menuId) {
        agileSysMenuService.deleteMenuById(menuId);
        return this.rtnSuccess("菜单信息删除成功");
    }
}

