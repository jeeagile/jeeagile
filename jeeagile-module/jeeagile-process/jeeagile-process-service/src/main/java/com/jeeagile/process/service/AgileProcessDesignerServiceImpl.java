package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.AgileUtil;
import com.jeeagile.frame.entity.system.AgileSysUser;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.system.IAgileSysUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2022-06-07
 * @description 流程设计
 */
@AgileService
public class AgileProcessDesignerServiceImpl implements IAgileProcessDesignerService {

    @Autowired
    private IAgileSysUserService agileSysUserService;

    @Override
    public AgilePage<AgileSysUser> selectUserPage(AgilePageable<AgileSysUser> agilePageable) {
        AgilePage<AgileSysUser> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileSysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileSysUser::getId, AgileSysUser::getUserName, AgileSysUser::getNickName);
        AgileSysUser agileSysUser = agilePageable.getQueryCond();
        if (agileSysUser != null) {
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserName())) {
                lambdaQueryWrapper.eq(AgileSysUser::getUserName, agileSysUser.getUserName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getNickName())) {
                lambdaQueryWrapper.like(AgileSysUser::getNickName, agileSysUser.getNickName());
            }
        }
        lambdaQueryWrapper.eq(AgileSysUser::getUserStatus, "0");
        lambdaQueryWrapper.ne(AgileSysUser::getUserName, AgileUtil.getSuperAdmin());
        lambdaQueryWrapper.orderByAsc(AgileSysUser::getUserSort);
        return agileSysUserService.page(agilePage, lambdaQueryWrapper);
    }

    @Override
    public List<String> detailUserNickName(String userIds) {
        List<String> nickNameList = new ArrayList<>();
        if (AgileStringUtil.isNotEmpty(userIds)) {
            LambdaQueryWrapper<AgileSysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.select(AgileSysUser::getNickName);
            lambdaQueryWrapper.in(AgileSysUser::getId, userIds.split(","));
            agileSysUserService.list(lambdaQueryWrapper).forEach(agileSysUser -> {
                nickNameList.add(agileSysUser.getNickName());
            });
        }
        return nickNameList;
    }


}
