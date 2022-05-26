package com.jeeagile.frame.controller.system;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.entity.system.AgileSysDictType;
import com.jeeagile.frame.service.system.IAgileSysDictTypeService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 字典类型管理 前端控制器
 */
@RestController
@AgileLogger("字典管理")
@RequestMapping("/system/dict/type")
@AgilePermissionsPrefix("system:dict:type")
@Api(value = "字典类型管理", tags = "字典管理")
public class AgileSysDictTypeController extends AgileCrudController<IAgileSysDictTypeService, AgileSysDictType> {
}
