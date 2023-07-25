package com.jeeagile.frame.controller.online;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.entity.online.AgileOnlineTable;
import com.jeeagile.frame.service.online.IAgileOnlineTableService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2023-07-25
 * @description 在线表单 表单数据模型 前端控制器
 */
@RestController
@AgileLogger("表单管理")
@RequestMapping("/online/table")
@AgilePermissionsPrefix("online:table")
@Api(value = "表单管理", tags = "表单管理")
public class AgileOnlineTableController extends AgileCrudController<IAgileOnlineTableService, AgileOnlineTable> {

}
