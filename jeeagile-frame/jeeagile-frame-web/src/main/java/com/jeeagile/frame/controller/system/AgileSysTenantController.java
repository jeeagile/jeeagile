package com.jeeagile.frame.controller.system;

import com.jeeagile.core.enums.AgileAuditStatus;
import com.jeeagile.core.enums.AgileEnableStatus;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresGuest;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.entity.system.AgileSysTenant;
import com.jeeagile.core.constants.SysOperateType;
import com.jeeagile.frame.service.system.IAgileSysTenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 参数管理 前端控制器
 */
@RestController
@AgileLogger("租户管理")
@RequestMapping("/system/tenant")
@AgilePermissionsPrefix("system:tenant")
@Api(value = "租户管理", tags = "租户管理")
public class AgileSysTenantController extends AgileCrudController<IAgileSysTenantService, AgileSysTenant> {
    @PostMapping("/audit")
    @AgileRequiresGuest
    @AgileLogger(notes = "租户审核", type = SysOperateType.UPDATE)
    @ApiOperation(value = "租户审核", notes = "租户审核")
    public AgileResult<AgileSysTenant> audit(@RequestBody AgileSysTenant agileSysTenant) {
        if (agileSysTenant.getTenantType().equals("1")) {
            throw new AgileFrameException("正在努力建设中，敬请期待！");
        }
        return AgileResult.success(this.agileService.audit(agileSysTenant));
    }

    @PostMapping("/info")
    @AgileRequiresGuest
    @ApiOperation(value = "查看明细", notes = "根据主键查看明细")
    public AgileResult<AgileSysTenant> info(@RequestBody Map param) {
        String tenantId = (String) param.get("tenantId");
        String tenantSign = (String) param.get("tenantSign");
        if (AgileStringUtil.isEmpty(tenantId)) {
            return AgileResult.error(AgileResultCode.WARN_VALIDATE_PASSED, "租户模式，非法访问！");
        }
        if (AgileStringUtil.isEmpty(tenantSign)) {
            return AgileResult.error(AgileResultCode.WARN_VALIDATE_PASSED, "租户模式，非法访问！");
        }
        AgileSysTenant agileSysTenant = this.agileService.agileSysTenantInfo(tenantId);
        if (agileSysTenant == null || AgileStringUtil.isEmpty(agileSysTenant.getId())) {
            return AgileResult.error(AgileResultCode.WARN_VALIDATE_PASSED, "租户模式，非法访问！");
        }
        if (!agileSysTenant.getTenantSign().equals(tenantSign)) {
            return AgileResult.error(AgileResultCode.WARN_VALIDATE_PASSED, "非法租户签名！");
        }
        if (!AgileEnableStatus.ENABLE.getCode().equals(agileSysTenant.getEnableStatus())) {
            return AgileResult.error(AgileResultCode.WARN_VALIDATE_PASSED, "租户已被停用！");
        }
        if (!AgileAuditStatus.PASS.getCode().equals(agileSysTenant.getAuditStatus())) {
            return AgileResult.error(AgileResultCode.WARN_VALIDATE_PASSED, "租户未审核通过，不能使用！");
        }
        if (agileSysTenant.getExpirationDate() != null) {
            if (new Date().after(agileSysTenant.getExpirationDate())) {
                return AgileResult.error(AgileResultCode.WARN_VALIDATE_PASSED, "租户已过使用期！");
            }
        }
        return AgileResult.success(agileSysTenant);
    }
}
