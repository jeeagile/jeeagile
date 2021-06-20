package com.jeeagile.system.controller;

import com.jeeagile.core.cache.constants.AgileCacheConstants;
import com.jeeagile.core.cache.util.AgileCacheUtil;
import com.jeeagile.core.kaptcha.AgileKaptchaInfo;
import com.jeeagile.core.kaptcha.AgileKaptchaUtil;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.user.AgileUserData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@RestController
@Api(value = "验证码", tags = "验证码")
@RequestMapping("/system/kaptcha")
public class AgileSysKaptchaController extends AgileBaseController {
    @PostMapping("/image")
    @ApiOperation(value = "生成验证码", notes = "生成验证码")
    public AgileResult<AgileUserData> image() {
        AgileKaptchaInfo agileKaptchaInfo = AgileKaptchaUtil.createImage();
        if (agileKaptchaInfo == null || StringUtil.isEmpty(agileKaptchaInfo.getImage())) {
            return this.rtnError(AgileResultCode.FAIL_KCAPTCHA_EXCEPTION, "验证码生成错误！");
        }
        //放入缓存
        AgileCacheUtil.put(AgileCacheConstants.AGILE_CACHE_KAPTCHA_NAME, agileKaptchaInfo.getUuid(), agileKaptchaInfo.getCode());
        AgileResult agileResult = AgileResult.success();
        agileResult.put("uuid", agileKaptchaInfo.getUuid());
        agileResult.put("image", agileKaptchaInfo.getImage());
        return agileResult;
    }

    @RequestMapping(value = "/valid", method = RequestMethod.POST)
    @ApiOperation(value = "效验验证码", notes = "效验验证码")
    public AgileResult valid(@RequestBody AgileKaptchaInfo agileKaptchaInfo) {
        String code = (String) AgileCacheUtil.get(AgileCacheConstants.AGILE_CACHE_KAPTCHA_NAME, agileKaptchaInfo.getUuid());
        if (StringUtil.isEmpty(code)) {
            return this.rtnError(AgileResultCode.FAIL_KCAPTCHA_EXPIRE, "验证码已失效！");
        }
        if(!code.equals(agileKaptchaInfo.getCode())){
            return this.rtnError(AgileResultCode.FAIL_KCAPTCHA_ERROR, "验证码错误！");
        }
        return this.rtnSuccess();
    }
}
