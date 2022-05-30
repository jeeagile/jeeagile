package com.jeeagile.frame.controller.monitor;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.user.AgileOnlineUser;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageUtil;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 在线用户监控
 */
@RestController
@RequestMapping("/monitor/online")
@Api(value = "在线用户", tags = "在线用户")
public class AgileMonitorOnlineController extends AgileBaseController {

    @PostMapping(value = "/user")
    @ApiOperation(value = "在线用户列表", notes = "在线用户列表")
    @AgileRequiresPermissions("monitor:online:list")
    public AgileResult<AgilePage<AgileOnlineUser>> onlineUser(AgilePageable<Map> agilePageable) {
        return this.success(getOnlineUserList(agilePageable));
    }

    @AgileDemo
    @PostMapping(value = "/forceLogout")
    @ApiOperation(value = "强剔用户", notes = "强剔用户")
    @AgileRequiresPermissions("monitor:online:forceLogout")
    public AgileResult<String> forceLogout(@SingleRequestBody String userToken) {
        AgileSecurityUtil.forceLogout(userToken);
        return this.success();
    }

    @AgileDemo
    @PostMapping(value = "/batchLogout")
    @ApiOperation(value = "批量强剔用户", notes = "批量强剔用户")
    @AgileRequiresPermissions("monitor:online:batchLogout")
    public AgileResult<String> batchLogout(@SingleRequestBody List<String> userTokenList) {
        for (String userToken : userTokenList) {
            AgileSecurityUtil.forceLogout(userToken);
        }
        return this.success();
    }

    /**
     * 获取在线用户
     */
    private AgilePage getOnlineUserList(AgilePageable<Map> agilePageable) {
        String loginIp = "";
        String userName = "";
        if (agilePageable.getQueryCond() != null) {
            loginIp = (String) agilePageable.getQueryCond().get("ipAddr");
            userName = (String) agilePageable.getQueryCond().get("userName");
        }
        List<AgileOnlineUser> onlineUserAllList = AgileSecurityUtil.getOnlineUserList();
        List<AgileOnlineUser> onlineUserList = new ArrayList<>();
        for (AgileOnlineUser agileOnlineUser : onlineUserAllList) {
            if (AgileStringUtil.isEmpty(agileOnlineUser.getUserToken())
                    || agileOnlineUser.getUserToken().equals(AgileSecurityContext.getUserToken())) {
                agileOnlineUser.setUserName(agileOnlineUser.getUserName() + "(当前用户)");
            }
            if (AgileStringUtil.isNotEmpty(loginIp) && AgileStringUtil.isNotEmpty(userName)) {
                if (loginIp.equals(agileOnlineUser.getLoginIp()) && userName.equals(agileOnlineUser.getUserName())) {
                    onlineUserList.add(agileOnlineUser);
                }
            } else if (AgileStringUtil.isNotEmpty(loginIp)) {
                if (loginIp.equals(agileOnlineUser.getLoginIp())) {
                    onlineUserList.add(agileOnlineUser);
                }
            } else if (AgileStringUtil.isNotEmpty(userName)) {
                if (userName.equals(agileOnlineUser.getUserName())) {
                    onlineUserList.add(agileOnlineUser);
                }
            } else {
                onlineUserList.add(agileOnlineUser);
            }
        }

        return AgilePageUtil.startPage(onlineUserList, agilePageable);
    }
}
