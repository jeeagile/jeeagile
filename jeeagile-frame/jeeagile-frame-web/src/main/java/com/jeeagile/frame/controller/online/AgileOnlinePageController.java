package com.jeeagile.frame.controller.online;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.entity.online.AgileOnlinePage;
import com.jeeagile.frame.service.online.IAgileOnlinePageService;
import com.jeeagile.frame.vo.AgileBaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2023-07-31
 * @description 在线表单 表单页面 前端控制器
 */
@RestController
@AgileLogger("表单页面")
@RequestMapping("/online/page")
@AgilePermissionsPrefix("online:page")
@Api(value = "表单页面", tags = "表单页面")
public class AgileOnlinePageController extends AgileCrudController<IAgileOnlinePageService, AgileOnlinePage> {
    @PostMapping(value = "/designer")
    @ApiOperation(value = "表单页面设计", notes = "表单页面设计")
    public AgileResult<AgileOnlinePage> designer(@RequestBody AgileOnlinePage agileOnlinePage) {
        agileService.designer(agileOnlinePage);
        return AgileResult.success();
    }
}
