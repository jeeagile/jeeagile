package com.jeeagile.system.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.system.entity.AgileSysDictData;
import com.jeeagile.system.service.IAgileSysDictDataService;
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
 * @description 字典数据管理 前端控制器
 */
@RestController
@RequestMapping("/system/dict/data")
@Api(value = "字典数据管理", tags = "字典数据管理")
public class AgileSysDictDataController extends AgileBaseController {

    @AgileReference
    private IAgileSysDictDataService agileSysDictDataService;

    @PostMapping(value = "/selectPage")
    @ApiOperation(value = "分页查询字典数据列表", notes = "分页查询字典数据列表")
    @AgileLogger(title = "查询字典数据", type = AgileLoggerType.SELECT)
    public AgileResult<AgilePage<AgileSysDictData>> selectPage(@RequestBody AgilePageable<AgileSysDictData> agilePageable) {
        return this.rtnSuccess(agileSysDictDataService.selectDictDataPage(agilePageable));
    }

    @PostMapping(value = "/selectList")
    @ApiOperation(value = "查询字典数据列表", notes = "查询字典数据列表")
    @AgileLogger(title = "查询字典数据", type = AgileLoggerType.SELECT)
    public AgileResult<List<AgileSysDictData>> selectList(@RequestBody AgileSysDictData agileSysDictData) {
        return this.rtnSuccess(agileSysDictDataService.selectDictDataList(agileSysDictData));
    }

    @PostMapping("/getDictDataByDictType")
    @ApiOperation(value = "根据字典类型查询字典数据列表", notes = "根据字典类型查询字典数据列表")
    public AgileResult<AgileSysDictData> getByDictType(@SingleRequestBody String dictType) {
        return this.rtnSuccess(agileSysDictDataService.selectDictDataByDictType(dictType));
    }

    @PostMapping("/detail")
    @ApiOperation(value = "根据字典数据ID查询字典数据详细信息", notes = "根据字典数据ID查询字典数据详细信息")
    @AgileLogger(title = "查看字典数据", type = AgileLoggerType.DETAIL)
    public AgileResult<AgileSysDictData> detail(@SingleRequestBody String dictDataId) {
        return this.rtnSuccess(agileSysDictDataService.selectDictDataById(dictDataId));
    }

    @AgileDemo
    @PostMapping("/add")
    @ApiOperation(value = "新增字典数据", notes = "新增字典数据")
    @AgileLogger(title = "新增字典数据", type = AgileLoggerType.ADD)
    public AgileResult<AgileSysDictData> add(@RequestBody AgileSysDictData agileSysDictData) {
        return this.rtnSuccess(agileSysDictDataService.saveDictData(agileSysDictData));
    }

    @AgileDemo
    @PostMapping("/update")
    @ApiOperation(value = "更新字典数据", notes = "更新字典数据")
    @AgileLogger(title = "更新字典数据", type = AgileLoggerType.UPDATE)
    public AgileResult<String> update(@RequestBody AgileSysDictData agileSysDictData) {
        agileSysDictDataService.updateDictDataById(agileSysDictData);
        return this.rtnSuccess("字典数据信息更新成功！");
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除字典数据", notes = "删除字典数据")
    @AgileLogger(title = "删除字典数据", type = AgileLoggerType.DELETE)
    public AgileResult<String> delete(@SingleRequestBody String dictDataId) {
        agileSysDictDataService.deleteDictDataById(dictDataId);
        return this.rtnSuccess("字典数据信息删除成功！");
    }

}
