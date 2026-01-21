package com.jeeagile.frame.controller.system;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.entity.system.AgileSysLoginLogger;
import com.jeeagile.core.constants.AgileOperateType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.system.IAgileSysLoginLoggerService;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 登录日志管理 前端控制器
 */
@RestController
@AgileLogger("登录日志")
@RequestMapping("/system/login/logger")
@Api(value = "登录日志", tags = "登录日志")
public class AgileSysLoginLoggerController extends AgileBaseController {
    @AgileReference
    private IAgileSysLoginLoggerService agileSysLoginService;

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询登录日志列表", notes = "分页查询登录日志列表")
    public AgileResult<AgilePage<AgileSysLoginLogger>> selectPage(@RequestBody AgilePageable<AgileSysLoginLogger> agilePageable) {
        return this.success(agileSysLoginService.selectPage(agilePageable));
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除登录日志记录", notes = "删除登录日志记录")
    @AgileLogger(notes = "删除登录日志记录", type = AgileOperateType.DELETE)
    public AgileResult<String> delete(@SingleRequestBody String id) {
        agileSysLoginService.deleteModel(id);
        return this.success("登录日志记录删除成功！");
    }


    @AgileDemo
    @PostMapping("/clear")
    @ApiOperation(value = "清空登录日志记录", notes = "清空登录日志记录")
    @AgileLogger(notes = "清空登录日志记录", type = AgileOperateType.CLEAR)
    public AgileResult clear() {
        agileSysLoginService.clearRecord();
        return this.success("登录日志记录清空成功！");
    }

    @PostMapping(value = "/export")
    @AgileRequiresPermissions("export")
    @AgileLogger(notes = "导出数据", type = AgileOperateType.EXPORT)
    @ApiOperation(value = "导出数据", notes = "导出数据接口")
    public void exportExcel(@RequestBody AgileSysLoginLogger agileSysLoginLogger) {
        String excelName = agileSysLoginLogger.getExcelName();
        if (AgileStringUtil.isEmpty(excelName)) {
            excelName = "导出数据";
        }
        List<AgileSysLoginLogger> dataList = agileSysLoginService.selectExportData(agileSysLoginLogger);
        this.exportExcel(dataList, excelName, AgileSysLoginLogger.class);
    }
}
