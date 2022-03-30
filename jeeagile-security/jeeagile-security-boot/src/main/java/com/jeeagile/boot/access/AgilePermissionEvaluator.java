package com.jeeagile.boot.access;

import com.jeeagile.boot.userdetails.AgileUserDetails;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.util.AgileStringUtil;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2021-07-09
 * @description
 */
public class AgilePermissionEvaluator implements PermissionEvaluator {

    public boolean hasPermission(Authentication authentication, Object permission) {
        return checkPermission(authentication, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object o, Object permission) {
        return checkPermission(authentication, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object permission) {
        return checkPermission(authentication, permission);
    }

    private boolean checkPermission(Authentication authentication, Object permission) {
        if (AgileStringUtil.isEmpty(permission)) {
            return false;
        }
        AgileUserDetails agileUserDetails = (AgileUserDetails) authentication.getPrincipal();
        AgileBaseUser userData = null;
        if (agileUserDetails != null) {
            userData = agileUserDetails.getUserData();
        }

        if (AgileStringUtil.isEmpty(userData) || CollectionUtils.isEmpty(userData.getUserPerm())) {
            return false;
        }

        return userData.getUserPerm().contains("*:*:*") || userData.getUserPerm().contains(permission);
    }
}
