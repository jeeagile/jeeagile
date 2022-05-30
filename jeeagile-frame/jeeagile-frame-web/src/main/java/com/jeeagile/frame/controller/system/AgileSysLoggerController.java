package com.jeeagile.frame.controller.system;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.entity.system.AgileSysLogger;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.system.IAgileSysLoggerService;
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
 * @description 操作日志管理 前端控制器
 */
@RestController
@AgileLogger("操作日志")
@RequestMapping("/system/logger")
@Api(value = "操作日志", tags = "操作日志")
public class AgileSysLoggerController extends AgileBaseController {
    @AgileReference
    private IAgileSysLoggerService agileSysLoggerService;

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询操作日志", notes = "分页查询操作日志")
    public AgileResult<AgilePage<AgileSysLogger>> selectPage(@RequestBody AgilePageable<AgileSysLogger> agilePageable) {
        return this.success(agileSysLoggerService.selectPage(agilePageable));
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除操作日志记录", notes = "删除操作日志记录")
    @AgileLogger(notes = "删除操作日志记录", type = AgileLoggerType.DELETE)
    public AgileResult<String> delete(@SingleRequestBody String configId) {
        agileSysLoggerService.deleteModel(configId);
        return this.success("操作日志记录删除成功！");
    }

    @AgileDemo
    @PostMapping("/clear")
    @ApiOperation(value = "清空操作日志记录", notes = "清空操作日志记录")
    @AgileLogger(notes = "清空操作日志记录", type = AgileLoggerType.CLEAR)
    public AgileResult clear() {
        agileSysLoggerService.clearRecord();
        return this.success("操作日志记录清空成功！");
    }

    @PostMapping(value = "/export")
    @AgileRequiresPermissions("export")
    @AgileLogger(notes = "导出数据", type = AgileLoggerType.EXPORT)
    @ApiOperation(value = "导出数据", notes = "导出数据接口")
    public void exportExcel(@RequestBody AgileSysLogger agileSysLogger) {
        String excelName = agileSysLogger.getExcelName();
        if (AgileStringUtil.isEmpty(excelName)) {
            excelName = "导出数据";
        }

        List<AgileSysLogger> dataList = agileSysLoggerService.selectExportData(agileSysLogger);
        this.exportExcel(dataList, excelName, AgileSysLogger.class);
    }
}
