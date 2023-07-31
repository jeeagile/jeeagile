package com.jeeagile.frame.controller.online;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.entity.online.AgileOnlineColumn;
import com.jeeagile.frame.entity.online.AgileOnlineTable;
import com.jeeagile.frame.service.online.IAgileOnlineTableService;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.frame.vo.system.AgileJdbcTable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2023-07-25
 * @description 在线表单 表单数据模型 前端控制器
 */
@RestController
@AgileLogger("表单数据模型管理")
@RequestMapping("/online/table")
@AgilePermissionsPrefix("online:table")
@Api(value = "表单数据模型管理", tags = "表单数据模型管理")
public class AgileOnlineTableController extends AgileCrudController<IAgileOnlineTableService, AgileOnlineTable> {
    @PostMapping(value = "/addOnlineColumn")
    @ApiOperation(value = "修改表单状态", notes = "修改表单状态")
    public AgileResult<AgileOnlineColumn> addOnlineColumn(@RequestBody AgileOnlineColumn agileOnlineColumn) {
        return this.success(agileService.addOnlineColumn(agileOnlineColumn));
    }

    @PostMapping(value = "/refreshOnlineColumn")
    @ApiOperation(value = "修改表单状态", notes = "修改表单状态")
    public AgileResult<AgileOnlineColumn> refreshOnlineColumn(@RequestBody AgileOnlineColumn agileOnlineColumn) {
        return this.success(agileService.refreshOnlineColumn(agileOnlineColumn));
    }
}
