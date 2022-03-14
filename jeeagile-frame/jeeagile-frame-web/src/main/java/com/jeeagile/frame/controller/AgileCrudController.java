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
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/init", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "初始化", notes = "提供页面初始化数据接口")
    public AgileResult<Object> init() {
        return AgileResult.success(this.initData());
    }

    /**
     * 初始化数据
     */
    protected Object initData() {
        return agileService.initData();
    }

    @PostMapping(value = "/page")
    @AgileRequiresPermissions("page")
    @AgileLogger(notes = "根据条件分页查询数据", type = AgileLoggerType.SELECT)
    @ApiOperation(value = "分页查询", notes = "分页查询数据接口")
    public AgileResult<AgilePage<T>> selectPage(@RequestBody AgilePageable<T> agilePageable) {
        return AgileResult.success(agileService.selectPage(agilePageable));
    }

    @PostMapping(value = "/list")
    @AgileRequiresPermissions("list")
    @AgileLogger(notes = "根据条件查询数据", type = AgileLoggerType.SELECT)
    @ApiOperation(value = "查询列表", notes = "根据条件查询数据")
    public AgileResult<List<T>> selectList(@RequestBody T agileModel) {
        return AgileResult.success(agileService.selectList(agileModel));
    }

    @PostMapping("/detail")
    @AgileRequiresPermissions("detail")
    @AgileLogger(notes = "根据主键查看明细", type = AgileLoggerType.DETAIL)
    @ApiOperation(value = "查看明细", notes = "根据主键查看明细")
    public AgileResult<T> postDetail(@SingleRequestBody Serializable id) {
        return AgileResult.success(agileService.selectModel(id));
    }

    @GetMapping("/{id}")
    @AgileRequiresPermissions("detail")
    @AgileLogger(notes = "根据主键查看明细", type = AgileLoggerType.DETAIL)
    @ApiOperation(value = "查看明细", notes = "根据主键查看明细")
    public AgileResult<T> getDetail(@PathVariable Serializable id) {
        return AgileResult.success(agileService.selectModel(id));
    }

    @AgileDemo
    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.PUT})
    @AgileRequiresPermissions("add")
    @AgileLogger(notes = "新增数据", type = AgileLoggerType.ADD)
    @ApiOperation(value = "新增数据", notes = "新增数据")
    public AgileResult<T> add(@RequestBody T agileModel) {
        this.saveModelValidate(agileModel);
        agileModel = agileService.saveModel(agileModel);
        return AgileResult.success(agileModel);
    }

    @AgileDemo
    @PostMapping("/update")
    @AgileRequiresPermissions("update")
    @AgileLogger(notes = "根据主键更新数据", type = AgileLoggerType.UPDATE)
    @ApiOperation(value = "更新数据", notes = "根据主键更新数据")
    public AgileResult update(@RequestBody T agileModel) {
        this.updateModelValidate(agileModel);
        if (agileService.updateModel(agileModel)) {
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
        if (agileService.deleteModel(id)) {
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
        if (agileService.deleteModel(id)) {
            return AgileResult.success("数据删除成功！");
        } else {
            return AgileResult.error(AgileResultCode.FAIL_OPS_DELETE, "数据删除失败！");
        }
    }

    @PostMapping(value = "/import")
    @AgileRequiresPermissions("import")
    @AgileLogger(notes = "导入数据", type = AgileLoggerType.IMPORT)
    @ApiOperation(value = "导入数据", notes = "导入数据接口")
    public AgileResult importExcel(T agileModel) {
        return AgileResult.error(AgileResultCode.FAIL_OPS_IMPORT,"正在紧急建设中，敬请期待!");
    }

    @PostMapping(value = "/export")
    @AgileRequiresPermissions("export")
    @AgileLogger(notes = "导出数据", type = AgileLoggerType.EXPORT)
    @ApiOperation(value = "导出数据", notes = "导出数据接口")
    public AgileResult exportExcel(T agileModel) {
        return AgileResult.error(AgileResultCode.FAIL_OPS_EXPORT,"正在紧急建设中，敬请期待!");
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
        if (agileModel instanceof AgileValidateModel) {
            ((AgileValidateModel) agileModel).validate();
        }
    }
}
