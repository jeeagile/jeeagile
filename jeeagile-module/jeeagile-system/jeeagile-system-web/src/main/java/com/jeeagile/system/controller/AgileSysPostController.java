package com.jeeagile.system.controller;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.system.entity.AgileSysPost;
import com.jeeagile.system.service.IAgileSysPostService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 岗位管理 前端控制器
 */
@RestController
@AgileLogger("岗位管理")
@RequestMapping("/system/post")
@AgilePermissionsPrefix("system:post")
@Api(value = "岗位管理", tags = "岗位管理")
public class AgileSysPostController extends AgileCrudController<IAgileSysPostService, AgileSysPost> {

}
