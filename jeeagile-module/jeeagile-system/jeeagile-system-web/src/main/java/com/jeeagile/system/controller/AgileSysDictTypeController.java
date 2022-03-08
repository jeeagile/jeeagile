package com.jeeagile.system.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.system.entity.AgileSysDictType;
import com.jeeagile.system.service.IAgileSysDictTypeService;
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
 * @description 字典类型管理 前端控制器
 */
@RestController
@RequestMapping("/system/dict/type")
@Api(value = "字典类型管理", tags = "字典类型管理")
public class AgileSysDictTypeController extends AgileBaseController {
    @AgileReference
    private IAgileSysDictTypeService agileSysDictTypeService;

    @PostMapping(value = "/selectPage")
    @ApiOperation(value = "分页查询字典类型列表", notes = "分页查询字典类型列表")
    @AgileLogger(title = "查询字典类型", type = AgileLoggerType.SELECT)
    public AgileResult<AgilePage<AgileSysDictType>> selectDictTypePage(@RequestBody AgilePageable<AgileSysDictType> agilePageable) {
        return this.success(agileSysDictTypeService.selectDictTypePage(agilePageable));
    }

    @PostMapping(value = "/selectList")
    @ApiOperation(value = "查询字典类型列表", notes = "查询字典类型列表")
    @AgileLogger(title = "查询字典类型", type = AgileLoggerType.SELECT)
    public AgileResult<List<AgileSysDictType>> selectDictTypeList(@RequestBody AgileSysDictType agileSysDictType) {
        return this.success(agileSysDictTypeService.selectDictTypeList(agileSysDictType));
    }

    @PostMapping("/detail")
    @ApiOperation(value = "根据字典类型ID获取字典类型详细信息", notes = "根据字典类型ID获取字典类型详细信息")
    @AgileLogger(title = "查看字典类型", type = AgileLoggerType.DETAIL)
    public AgileResult<AgileSysDictType> detail(@SingleRequestBody String dictTypeId) {
        return this.success(agileSysDictTypeService.selectDictTypeById(dictTypeId));
    }

    @AgileDemo
    @PostMapping("/add")
    @ApiOperation(value = "新增字典类型", notes = "新增字典类型")
    @AgileLogger(title = "新增字典类型", type = AgileLoggerType.ADD)
    @AgileRequiresPermissions("system:dict:add")
    public AgileResult<AgileSysDictType> add(@RequestBody AgileSysDictType agileSysDictType) {
        return this.success(agileSysDictTypeService.saveDictType(agileSysDictType));
    }

    @AgileDemo
    @PostMapping("/update")
    @ApiOperation(value = "修改字典类型", notes = "修改字典类型")
    @AgileLogger(title = "修改字典类型", type = AgileLoggerType.UPDATE)
    @AgileRequiresPermissions("system:dict:update")
    public AgileResult<String> update(@RequestBody AgileSysDictType agileSysDictType) {
        agileSysDictTypeService.updateDictTypeById(agileSysDictType);
        return this.success("字典类型信息更新成功");
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除字典类型", notes = "删除字典类型")
    @AgileLogger(title = "删除字典类型", type = AgileLoggerType.DELETE)
    @AgileRequiresPermissions("system:dict:delete")
    public AgileResult<String> delete(@SingleRequestBody String dictTypeId) {
        agileSysDictTypeService.deleteDictTypeById(dictTypeId);
        return this.success("字典类型信息删除成功");
    }
}
