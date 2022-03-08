package com.jeeagile.system.controller;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.system.entity.AgileSysDictData;
import com.jeeagile.system.service.IAgileSysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 字典数据管理 前端控制器
 */
@RestController
@RequestMapping("/system/dict/data")
@AgilePermissionsPrefix("system:dict:data")
@Api(value = "字典数据管理", tags = "字典数据管理")
public class AgileSysDictDataController extends AgileCrudController<IAgileSysDictDataService, AgileSysDictData> {
    @PostMapping("/getDictDataByDictType")
    @ApiOperation(value = "根据字典类型查询字典数据列表", notes = "根据字典类型查询字典数据列表")
    public AgileResult<AgileSysDictData> getByDictType(@SingleRequestBody String dictType) {
        return this.success(this.getAgileService().selectDictDataByDictType(dictType));
    }
}
