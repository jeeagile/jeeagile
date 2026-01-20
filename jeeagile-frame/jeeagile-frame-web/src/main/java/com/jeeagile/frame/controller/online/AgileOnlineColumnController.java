package com.jeeagile.frame.controller.online;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.entity.online.AgileOnlineColumn;
import com.jeeagile.frame.service.online.IAgileOnlineColumnService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2023-07-27
 * @description 在线表单 表单数据表字段 前端控制器
 */
@RestController
@AgileLogger("字段管理")
@RequestMapping("/online/column")
@AgilePermissionsPrefix("online:column")
@Api(value = "字段管理", tags = "字段管理")
public class AgileOnlineColumnController extends AgileCrudController<IAgileOnlineColumnService, AgileOnlineColumn> {

}
