package com.jeeagile.frame.controller.system;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.entity.system.AgileSysDept;
import com.jeeagile.frame.entity.system.AgileSysJdbc;
import com.jeeagile.frame.service.system.IAgileSysDeptService;
import com.jeeagile.frame.service.system.IAgileSysJdbcService;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.frame.vo.system.AgileJdbcTable;
import com.jeeagile.frame.vo.system.AgileJdbcTableColumn;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 数据源管理 前端控制器
 */
@RestController
@AgileLogger("数据源管理")
@RequestMapping("/system/jdbc")
@AgilePermissionsPrefix("system:jdbc")
@Api(value = "数据源管理", tags = "数据源管理")
public class AgileSysJdbcController extends AgileCrudController<IAgileSysJdbcService, AgileSysJdbc> {
    @PostMapping(value = "/selectTableList")
    @ApiOperation(value = "获取数据库表信息", notes = "获取数据库表信息")
    public AgileResult<List<AgileJdbcTable>> selectTableList(@SingleRequestBody String id) {
        return this.success(agileService.selectTableList(id));
    }

    @PostMapping(value = "/selectTableColumnList")
    @ApiOperation(value = "获取数据库表字段信息", notes = "获取数据库表字段信息")
    public AgileResult<List<AgileJdbcTableColumn>> selectTableColumnList(@SingleRequestBody String id, @SingleRequestBody String tableName) {
        return this.success(agileService.selectTableColumnList(id, tableName));
    }
}

