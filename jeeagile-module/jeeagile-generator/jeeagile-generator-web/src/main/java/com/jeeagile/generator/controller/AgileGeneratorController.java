package com.jeeagile.generator.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.generator.entity.AgileGeneratorTable;
import com.jeeagile.generator.service.IAgileGeneratorTableService;
import com.jeeagile.generator.vo.AgileGeneratorTableInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2021-06-21
 * @description
 */
@RestController
@RequestMapping("/generator")
@Api(value = "代码生成", tags = "代码生成")
public class AgileGeneratorController extends AgileBaseController {
    @AgileReference
    private IAgileGeneratorTableService agileGeneratorTableService;

    @PostMapping(value = "/selectTablePage")
    @ApiOperation(value = "分页查询代码生成列表", notes = "分页查询代码生成列表")
    public AgileResult<AgilePage<AgileGeneratorTable>> selectTablePage(@RequestBody AgilePageable<AgileGeneratorTable> agilePageable) {
        return this.rtnSuccess(agileGeneratorTableService.selectTablePage(agilePageable));
    }

    @PostMapping(value = "/selectDbTablePage")
    @ApiOperation(value = "分页查询数据库表列表", notes = "分页查询数据库表列表")
    public AgileResult<AgilePage<AgileGeneratorTable>> selectDbTablePage(@RequestBody AgilePageable<AgileGeneratorTable> agilePageable) {
        return this.rtnSuccess(agileGeneratorTableService.selectDbTablePage(agilePageable));
    }

    @PostMapping("/importTable")
    @ApiOperation(value = "导入代码生成", notes = "导入代码生成")
    @AgileLogger(title = "导入代码生成",type = AgileLoggerType.IMPORT)
    public AgileResult<Object> importTable(@RequestBody List<String> tableNameList) {
        agileGeneratorTableService.importTable(tableNameList);
        return this.rtnSuccess("数据库表导入成功！");
    }

    @PostMapping("/detailTable")
    @ApiOperation(value = "查看代码生成表信息", notes = "查看代码生成表信息")
    public AgileResult<AgileGeneratorTableInfo> detailTable(@SingleRequestBody String agileGeneratorTableId) {
        return this.rtnSuccess(agileGeneratorTableService.selectTableInfoById(agileGeneratorTableId));
    }

    @PostMapping("/updateTable")
    @ApiOperation(value = "修改代码生成表信息", notes = "修改代码生成表信息")
    @AgileLogger(title = "修改代码生成表信息",type = AgileLoggerType.UPDATE)
    public AgileResult<Object> updateTable(@RequestBody AgileGeneratorTableInfo agileGeneratorTableInfo) {
        agileGeneratorTableService.updateTableInfoById(agileGeneratorTableInfo);
        return this.rtnSuccess("代码生成表信息修改成功！");
    }

    @PostMapping("/deleteTable")
    @ApiOperation(value = "删除代码生成表信息", notes = "删除代码生成表信息")
    @AgileLogger(title = "删除代码生成表信息",type = AgileLoggerType.DELETE)
    public AgileResult<Object> deleteTable(@SingleRequestBody String agileGeneratorTableId) {
        agileGeneratorTableService.deleteTableById(agileGeneratorTableId);
        return this.rtnSuccess("代码生成表信息删除成功！");
    }

    @PostMapping("/syncTable")
    @ApiOperation(value = "同步代码生成表信息", notes = "同步代码生成表信息")
    @AgileLogger(title = "同步代码生成表信息",type = AgileLoggerType.UPDATE)
    public AgileResult<Object> syncTable(@SingleRequestBody String agileGeneratorTableId) {
        agileGeneratorTableService.syncTableById(agileGeneratorTableId);
        return this.rtnSuccess("代码生成表信息同步成功！");
    }

    @PostMapping("/previewCode")
    @ApiOperation(value = "预览生成代码", notes = "预览生成代码")
    @AgileLogger(title = "预览生成代码",type = AgileLoggerType.GENERATOR)
    public AgileResult<Map<String,String>> previewCode(@SingleRequestBody String agileGeneratorTableId) {
        return this.rtnSuccess(agileGeneratorTableService.previewCode(agileGeneratorTableId));
    }

    @PostMapping("/downloadCode")
    @ApiOperation(value = "下载生成代码", notes = "下载生成代码")
    @AgileLogger(title = "下载生成代码",type = AgileLoggerType.GENERATOR)
    public AgileResult<Object> downloadCode(HttpServletResponse response, @RequestBody List<String> agileGeneratorTableIdList) {
        try {
            byte[] dataByte = agileGeneratorTableService.downloadCode(agileGeneratorTableIdList);
            response.reset();
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment; filename=JeeAgile.zip");
            response.addHeader("Content-Length", "" + dataByte.length);
            response.setContentType("application/octet-stream; charset=UTF-8");
            IOUtils.write(dataByte, response.getOutputStream());
            return null;
        } catch (Exception ex) {
            return this.rtnError(ex, "下载代码发生异常！");
        }
    }
}
