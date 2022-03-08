package com.jeeagile.system.controller;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.system.entity.AgileSysDept;
import com.jeeagile.system.service.IAgileSysDeptService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 部门管理 前端控制器
 */
@RestController
@RequestMapping("/system/dept")
@AgilePermissionsPrefix("system:dept")
@Api(value = "部门管理", tags = "部门管理")
public class AgileSysDeptController extends AgileCrudController<IAgileSysDeptService, AgileSysDept> {

}

