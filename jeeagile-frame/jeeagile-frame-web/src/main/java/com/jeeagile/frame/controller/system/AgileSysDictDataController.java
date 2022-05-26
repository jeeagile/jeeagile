package com.jeeagile.frame.controller.system;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.entity.system.AgileSysDictData;
import com.jeeagile.frame.service.system.IAgileSysDictDataService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 字典数据管理 前端控制器
 */
@RestController
@AgileLogger("字典管理")
@RequestMapping("/system/dict/data")
@AgilePermissionsPrefix("system:dict:data")
@Api(value = "字典数据管理", tags = "字典管理")
public class AgileSysDictDataController extends AgileCrudController<IAgileSysDictDataService, AgileSysDictData> {

}
