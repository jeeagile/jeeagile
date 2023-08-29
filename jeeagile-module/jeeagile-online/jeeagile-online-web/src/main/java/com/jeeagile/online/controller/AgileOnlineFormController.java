package com.jeeagile.online.controller;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.online.entity.AgileOnlineForm;
import com.jeeagile.online.service.IAgileOnlineFormService;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.online.vo.OnlinePageRender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2023-07-13
 * @description 在线表单 表单信息 前端控制器
 */
@RestController
@AgileLogger("表单管理")
@RequestMapping("/online/form")
@AgilePermissionsPrefix("online:form")
@Api(value = "表单管理", tags = "表单管理")
public class AgileOnlineFormController extends AgileCrudController<IAgileOnlineFormService, AgileOnlineForm> {
    @PostMapping(value = "/publish")
    @ApiOperation(value = "表单发布", notes = "表单发布")
    public AgileResult<Object> publish(@SingleRequestBody String id, @SingleRequestBody String publishStatus) {
        return this.success(agileService.publish(id, publishStatus));
    }

    @PostMapping(value = "/changeStatus")
    @ApiOperation(value = "修改表单状态", notes = "修改表单状态")
    public AgileResult<Object> changeFormStatus(@SingleRequestBody String id, @SingleRequestBody String formStatus) {
        return this.success(agileService.changeStatus(id, formStatus));
    }

    @PostMapping(value = "/render")
    @ApiOperation(value = "表单加载", notes = "表单加载")
    public AgileResult<OnlinePageRender> render(@SingleRequestBody String pageId) {
        return this.success(agileService.render(pageId));
    }

    @PostMapping(value = "/formPageList")
    @ApiOperation(value = "获取表单页面列表", notes = "获取表单页面列表")
    public AgileResult<Object> formPageList() {
        return this.success(agileService.selectFormPageList());
    }

}
