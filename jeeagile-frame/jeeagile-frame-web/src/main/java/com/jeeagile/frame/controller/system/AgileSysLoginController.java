package com.jeeagile.frame.controller.system;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.entity.system.AgileSysLogin;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.system.IAgileSysLoginService;
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
@AgileLogger("登录日志管理")
@RequestMapping("/system/login")
@Api(value = "登录日志管理", tags = "登录日志管理")
public class AgileSysLoginController extends AgileBaseController {
    @AgileReference
    private IAgileSysLoginService agileSysLoginService;

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询登录日志列表", notes = "分页查询登录日志列表")
    public AgileResult<AgilePage<AgileSysLogin>> selectPage(@RequestBody AgilePageable<AgileSysLogin> agilePageable) {
        return this.success(agileSysLoginService.selectPage(agilePageable));
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除登录日志记录", notes = "删除登录日志记录")
    @AgileLogger(notes = "删除登录日志记录", type = AgileLoggerType.DELETE)
    public AgileResult<String> delete(@SingleRequestBody String id) {
        agileSysLoginService.deleteModel(id);
        return this.success("登录日志记录删除成功！");
    }


    @AgileDemo
    @PostMapping("/clear")
    @ApiOperation(value = "清空登录日志记录", notes = "清空登录日志记录")
    @AgileLogger(notes = "清空登录日志记录", type = AgileLoggerType.CLEAR)
    public AgileResult clear() {
        agileSysLoginService.clearRecord();
        return this.success("登录日志记录清空成功！");
    }

    @PostMapping(value = "/export")
    @AgileRequiresPermissions("export")
    @AgileLogger(notes = "导出数据", type = AgileLoggerType.EXPORT)
    @ApiOperation(value = "导出数据", notes = "导出数据接口")
    public void exportExcel(@RequestBody AgileSysLogin agileSysLogin) {
        String excelName = agileSysLogin.getExcelName();
        if (AgileStringUtil.isEmpty(excelName)) {
            excelName = "导出数据";
        }
        List<AgileSysLogin> dataList = agileSysLoginService.selectExportData(agileSysLogin);
        this.exportExcel(dataList, excelName, AgileSysLogin.class);
    }
}
