package com.jeeagile.online.controller;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.online.entity.AgileOnlineDict;
import com.jeeagile.online.service.IAgileOnlineDictService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2023-07-13
 * @description 在线表单 字典管理 前端控制器
 */
@RestController
@AgileLogger("字典管理")
@RequestMapping("/online/dict")
@AgilePermissionsPrefix("online:dict")
@Api(value = "字典管理", tags = "字典管理")
public class AgileOnlineDictController extends AgileCrudController<IAgileOnlineDictService, AgileOnlineDict> {

}
