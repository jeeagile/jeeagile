package com.jeeagile.logger.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.logger.entity.AgileLoggerOperate;
import com.jeeagile.logger.service.IAgileLoggerOperateService;
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
@AgileLogger("操作日志管理")
@RequestMapping("/logger/operate")
@Api(value = "操作日志管理", tags = "操作日志管理")
public class AgileLoggerOperateController extends AgileBaseController {
    @AgileReference
    private IAgileLoggerOperateService agileLoggerOperateService;

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询操作日志", notes = "分页查询操作日志")
    public AgileResult<AgilePage<AgileLoggerOperate>> selectPage(@RequestBody AgilePageable<AgileLoggerOperate> agilePageable) {
        return this.success(agileLoggerOperateService.selectPage(agilePageable));
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除操作日志记录", notes = "删除操作日志记录")
    @AgileLogger(notes = "删除操作日志记录", type = AgileLoggerType.DELETE)
    public AgileResult<String> delete(@SingleRequestBody String configId) {
        agileLoggerOperateService.deleteModel(configId);
        return this.success("操作日志记录删除成功！");
    }

    @AgileDemo
    @PostMapping("/clear")
    @ApiOperation(value = "清空操作日志记录", notes = "清空操作日志记录")
    @AgileLogger(notes = "清空操作日志记录", type = AgileLoggerType.CLEAR)
    public AgileResult clear() {
        agileLoggerOperateService.clearRecord();
        return this.success("操作日志记录清空成功！");
    }

    @PostMapping(value = "/export")
    @AgileRequiresPermissions("export")
    @AgileLogger(notes = "导出数据", type = AgileLoggerType.EXPORT)
    @ApiOperation(value = "导出数据", notes = "导出数据接口")
    public void exportExcel(@RequestBody AgileLoggerOperate agileLoggerOperate) {
        String excelName = agileLoggerOperate.getExcelName();
        if (AgileStringUtil.isEmpty(excelName)) {
            excelName = "导出数据";
        }

        List<AgileLoggerOperate> dataList = agileLoggerOperateService.selectExportData(agileLoggerOperate);
        this.exportExcel(dataList, excelName, AgileLoggerOperate.class);
    }
}
