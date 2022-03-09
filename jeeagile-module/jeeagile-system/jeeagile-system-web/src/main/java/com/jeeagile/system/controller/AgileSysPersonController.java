package com.jeeagile.system.controller;

import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.system.service.IAgileSysPersonService;
import com.jeeagile.system.vo.AgilePersonInfo;
import com.jeeagile.system.vo.AgileUpdatePerson;
import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresUser;
import com.jeeagile.core.util.AgileUtil;
import com.jeeagile.core.util.file.AgileFileUtil;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.system.vo.AgileUpdatePwd;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@RestController
@Api(value = "个人中心", tags = "个人中心")
@RequestMapping("/system/person")
public class AgileSysPersonController extends AgileBaseController {
    @AgileReference
    private IAgileSysPersonService agilePersonService;


    @AgileRequiresUser
    @PostMapping("/getPersonInfo")
    @ApiOperation(value = "获取个人信息", notes = "获取个人信息")
    public AgileResult<AgilePersonInfo> getPersonInfo() {
        return this.success(agilePersonService.getPersonInfo());
    }

    @AgileDemo
    @AgileRequiresUser
    @PostMapping("/updateInfo")
    @ApiOperation(value = "更新个人信息", notes = "更新个人信息")
    @AgileLogger(title = "更新个人信息", type = AgileLoggerType.UPDATE)
    public AgileResult<Object> updateInfo(@RequestBody AgileUpdatePerson agileUpdatePerson) {
        agilePersonService.updatePersonInfo(agileUpdatePerson);
        return this.success("个人信息更新成功！");
    }

    @AgileDemo
    @AgileRequiresUser
    @PostMapping("/updatePwd")
    @ApiOperation(value = "更新个人密码", notes = "更新个人密码")
    @AgileLogger(title = "更新个人密码", type = AgileLoggerType.UPDATE)
    public AgileResult<Object> updatePwd(@RequestBody AgileUpdatePwd agileUpdatePwd) {
        agilePersonService.updatePersonPwd(agileUpdatePwd);
        return this.success("个人密码修改成功！");
    }


    @AgileRequiresUser
    @PostMapping("/uploadAvatar")
    @ApiOperation(value = "上传头像", notes = "上传头像")
    @AgileLogger(title = "上传头像", type = AgileLoggerType.UPDATE)
    public AgileResult<String> uploadAvatar(@RequestParam("userAvatar") MultipartFile multipartFile) {
        String userAvatarBasePath = AgileUtil.getUploadPath() + AgileFileUtil.getFileSeparator() + "avatar";
        String userAvatarFilePath = AgileFileUtil.upload(multipartFile, userAvatarBasePath);
        //置换为资源路径
        String userAvatar = userAvatarFilePath.replace(AgileUtil.getUploadPath(), AgileConstants.AGILE_RESOURCE_PREFIX);
        userAvatar = userAvatar.replace(AgileFileUtil.getFileSeparator(), "/");
        agilePersonService.updateUserAvatar(userAvatar);
        return this.success((Object) userAvatar);
    }

}
