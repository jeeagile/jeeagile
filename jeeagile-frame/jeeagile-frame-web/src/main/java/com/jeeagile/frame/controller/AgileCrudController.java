package com.jeeagile.frame.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.entity.AgileModel;
import com.jeeagile.frame.entity.AgileValidateModel;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileService;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2022-03-02
 * @description
 */
public abstract class AgileCrudController<I extends IAgileService<T>, T extends AgileModel> extends AgileBaseController {

    @Getter
    @AgileReference
    private I agileService;

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @AgileLogger(title = "分页查询", type = AgileLoggerType.SELECT)
    @AgileRequiresPermissions("page")
    public AgileResult<AgilePage<T>> selectPage(@RequestBody AgilePageable<T> agilePageable) {
        return this.rtnSuccess(agileService.selectPage(agilePageable));
    }

    @PostMapping(value = "/list")
    @ApiOperation(value = "查询列表", notes = "查询列表")
    @AgileLogger(title = "查询列表", type = AgileLoggerType.SELECT)
    @AgileRequiresPermissions("list")
    public AgileResult<List<T>> selectList(@RequestBody T agileModel) {
        return this.rtnSuccess(agileService.selectList(agileModel));
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "导入数据", notes = "导入数据")
    @AgileLogger(title = "导入数据", type = AgileLoggerType.IMPORT)
    @AgileRequiresPermissions("import")
    public AgileResult importExcel(T agileModel) {
        return this.rtnSuccess();
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出数据", notes = "导出数据")
    @AgileLogger(title = "导出数据", type = AgileLoggerType.EXPORT)
    @AgileRequiresPermissions("export")
    public AgileResult exportExcel(T agileModel) {
        return this.rtnSuccess();
    }

    @PostMapping("/detail")
    @ApiOperation(value = "查看明细", notes = "查看明细")
    @AgileLogger(title = "查看明细", type = AgileLoggerType.DETAIL)
    @AgileRequiresPermissions("detail")
    public AgileResult<T> detail(@SingleRequestBody String id) {
        return this.rtnSuccess(agileService.selectModel(id));
    }

    @AgileDemo
    @PostMapping("/add")
    @ApiOperation(value = "新增数据", notes = "新增数据")
    @AgileLogger(title = "新增数据", type = AgileLoggerType.ADD)
    @AgileRequiresPermissions("add")
    public AgileResult<T> add(@RequestBody T agileModel) {
        this.validateModel(agileModel);
        return this.rtnSuccess(agileService.saveModel(agileModel));
    }

    @AgileDemo
    @PostMapping("/update")
    @ApiOperation(value = "更新数据", notes = "更新数据")
    @AgileLogger(title = "更新数据", type = AgileLoggerType.UPDATE)
    @AgileRequiresPermissions("update")
    public AgileResult update(@RequestBody T agileModel) {
        this.validateModel(agileModel);
        if (agileService.updateModel(agileModel)) {
            return this.rtnSuccess("数据更新成功！");
        } else {
            return this.rtnError(AgileResultCode.FAIL_OPS_UPDATE, "数据更新失败！");
        }
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除数据", notes = "删除数据")
    @AgileLogger(title = "删除数据", type = AgileLoggerType.DELETE)
    @AgileRequiresPermissions("delete")
    public AgileResult delete(@SingleRequestBody Serializable id) {
        if (agileService.deleteModel(id)) {
            return this.rtnSuccess("数据删除成功！");
        } else {
            return this.rtnError(AgileResultCode.FAIL_OPS_DELETE, "数据删除失败！");
        }
    }

    /**
     * 验证实体数据
     *
     * @param agileModel
     */
    protected void validateModel(T agileModel) {
        if (agileModel instanceof AgileValidateModel) {
            ((AgileValidateModel) agileModel).validate();
        }
    }
}
