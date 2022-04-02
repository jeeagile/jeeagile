package com.jeeagile.frame.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.entity.AgileModel;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.excel.AgileExcelListener;
import com.jeeagile.frame.excel.AgileDefaultExcelListener;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2022-03-02
 * @description
 */
@Slf4j
public abstract class AgileCrudController<I extends IAgileBaseService<T>, T extends AgileModel> extends AgileBaseController {

    @Getter
    @AgileReference
    protected I agileBaseService;

    @RequestMapping(value = "/init", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "初始化", notes = "提供页面初始化数据接口")
    public AgileResult<Object> init() {
        return AgileResult.success(this.initData());
    }

    /**
     * 初始化数据
     */
    protected Object initData() {
        return agileBaseService.initData();
    }

    @PostMapping(value = "/page")
    @AgileRequiresPermissions("page")
    @AgileLogger(notes = "根据条件分页查询数据", type = AgileLoggerType.SELECT)
    @ApiOperation(value = "分页查询", notes = "分页查询数据接口")
    public AgileResult<AgilePage<T>> selectPage(@RequestBody AgilePageable<T> agilePageable) {
        return AgileResult.success(agileBaseService.selectPage(agilePageable));
    }

    @PostMapping(value = "/list")
    @AgileRequiresPermissions("list")
    @AgileLogger(notes = "根据条件查询数据", type = AgileLoggerType.SELECT)
    @ApiOperation(value = "查询列表", notes = "根据条件查询数据")
    public AgileResult<List<T>> selectList(@RequestBody T agileModel) {
        return AgileResult.success(agileBaseService.selectList(agileModel));
    }

    @PostMapping("/detail")
    @AgileRequiresPermissions("detail")
    @AgileLogger(notes = "根据主键查看明细", type = AgileLoggerType.DETAIL)
    @ApiOperation(value = "查看明细", notes = "根据主键查看明细")
    public AgileResult<T> detail(@SingleRequestBody Serializable id) {
        return AgileResult.success(agileBaseService.selectModel(id));
    }

    @GetMapping("/{id}")
    @AgileRequiresPermissions("detail")
    @AgileLogger(notes = "根据主键查看明细", type = AgileLoggerType.DETAIL)
    @ApiOperation(value = "查看明细", notes = "根据主键查看明细")
    public AgileResult<T> detailInfo(@PathVariable Serializable id) {
        return AgileResult.success(agileBaseService.selectModel(id));
    }

    @AgileDemo
    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.PUT})
    @AgileRequiresPermissions("add")
    @AgileLogger(notes = "新增数据", type = AgileLoggerType.ADD)
    @ApiOperation(value = "新增数据", notes = "新增数据")
    public AgileResult<T> add(@RequestBody T agileModel) {
        this.saveModelValidate(agileModel);
        return AgileResult.success(agileBaseService.saveModel(agileModel));
    }

    @AgileDemo
    @PostMapping("/update")
    @AgileRequiresPermissions("update")
    @AgileLogger(notes = "根据主键更新数据", type = AgileLoggerType.UPDATE)
    @ApiOperation(value = "更新数据", notes = "根据主键更新数据")
    public AgileResult update(@RequestBody T agileModel) {
        this.updateModelValidate(agileModel);
        if (agileBaseService.updateModel(agileModel)) {
            return AgileResult.success("数据更新成功！");
        } else {
            return AgileResult.error(AgileResultCode.FAIL_OPS_UPDATE, "数据更新失败！");
        }
    }

    @AgileDemo
    @PostMapping("/delete")
    @AgileRequiresPermissions("delete")
    @AgileLogger(notes = "根据主键删除数据", type = AgileLoggerType.DELETE)
    @ApiOperation(value = "删除数据", notes = "删除业务数据")
    public AgileResult delete(@SingleRequestBody Serializable id) {
        if (agileBaseService.deleteModel(id)) {
            return AgileResult.success("数据删除成功！");
        } else {
            return AgileResult.error(AgileResultCode.FAIL_OPS_DELETE, "数据删除失败！");
        }
    }

    @AgileDemo
    @DeleteMapping("/{id}")
    @AgileRequiresPermissions("delete")
    @AgileLogger(notes = "根据主键删除数据", type = AgileLoggerType.DELETE)
    @ApiOperation(value = "删除数据", notes = "根据主键删除数据")
    public AgileResult deleteInfo(@PathVariable Serializable id) {
        if (agileBaseService.deleteModel(id)) {
            return AgileResult.success("数据删除成功！");
        } else {
            return AgileResult.error(AgileResultCode.FAIL_OPS_DELETE, "数据删除失败！");
        }
    }

    @AgileDemo
    @PostMapping(value = "/import")
    @AgileRequiresPermissions("import")
    @AgileLogger(notes = "导入数据", type = AgileLoggerType.IMPORT)
    @ApiOperation(value = "导入数据", notes = "导入数据接口")
    public AgileResult importExcel(@RequestParam("importExcel") MultipartFile multipartFile) {
        try {
            AgileExcelListener agileExcelListener = this.getAgileExcelListener();
            EasyExcel.read(multipartFile.getInputStream(), agileBaseService.getEntityClass(), agileExcelListener).sheet().doRead();
            List<T> agileModelList = agileExcelListener.getAgileModelList();
            if (AgileCollectionUtil.isNotEmpty(agileModelList)) {
                return AgileResult.success(agileBaseService.importData(agileModelList));
            } else {
                return AgileResult.error(AgileResultCode.FAIL_OPS_IMPORT, "没有读取到任务数据！");
            }
        } catch (IOException ex) {
            log.error("数据导入异常！", ex);
        }
        return AgileResult.error(AgileResultCode.FAIL_OPS_IMPORT, "数据导入异常！");
    }

    protected AgileExcelListener<T> getAgileExcelListener() {
        return new AgileDefaultExcelListener();
    }

    @PostMapping(value = "/export")
    @AgileRequiresPermissions("export")
    @AgileLogger(notes = "导出数据", type = AgileLoggerType.EXPORT)
    @ApiOperation(value = "导出数据", notes = "导出数据接口")
    public void exportExcel(HttpServletResponse httpServletResponse, @RequestBody T agileModel) {
        String excelName = agileModel.getExcelName();
        if (AgileStringUtil.isEmpty(excelName)) {
            excelName = "导出数据";
        }
        try {
            List<T> data = agileBaseService.selectExportData(agileModel);
            httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            httpServletResponse.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(excelName, "UTF-8").replaceAll("\\+", "%20");
            httpServletResponse.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");
            EasyExcel.write(httpServletResponse.getOutputStream(), agileModel.getClass()).sheet(excelName).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).doWrite(data);
        } catch (Exception ex) {
            throw new AgileFrameException(AgileResultCode.FAIL_OPS_EXPORT, excelName + "数据导出失败！", ex);
        }

    }

    /**
     * 保存数据校验方法
     *
     * @param agileModel
     */
    protected void saveModelValidate(T agileModel) {
        this.validateModel(agileModel);
    }

    /**
     * 更新数据校验方法
     *
     * @param agileModel
     */
    protected void updateModelValidate(T agileModel) {
        this.validateModel(agileModel);
    }

    /**
     * 验证实体数据
     *
     * @param agileModel
     */
    protected void validateModel(T agileModel) {
        agileModel.validate();
    }
}
