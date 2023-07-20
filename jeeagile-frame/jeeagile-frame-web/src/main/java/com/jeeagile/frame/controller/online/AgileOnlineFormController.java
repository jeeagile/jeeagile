package com.jeeagile.frame.controller.online;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.entity.online.AgileOnlineForm;
import com.jeeagile.frame.service.online.IAgileOnlineFormService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2023-07-13
 * @description 在线表单 字典管理 前端控制器
 */
@RestController
@AgileLogger("表单管理")
@RequestMapping("/online/form")
@AgilePermissionsPrefix("online:form")
@Api(value = "表单管理", tags = "表单管理")
public class AgileOnlineFormController extends AgileCrudController<IAgileOnlineFormService, AgileOnlineForm> {

}
