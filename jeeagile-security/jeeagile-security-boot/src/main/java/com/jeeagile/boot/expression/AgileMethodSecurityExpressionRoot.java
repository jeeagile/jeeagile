package com.jeeagile.boot.expression;

import com.jeeagile.boot.access.AgilePermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * @author JeeAgile
 * @date 2021-07-09
 * @description
 */
public class AgileMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private AgilePermissionEvaluator permissionEvaluator;

    private Object filterObject;

    private Object returnObject;

    private Object target;

    public boolean hasPermission(String permission) {
        return this.permissionEvaluator.hasPermission(this.authentication, permission);
    }

    public void setPermissionEvaluator(AgilePermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
        super.setPermissionEvaluator(permissionEvaluator);
    }

    public AgileMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    public void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

}
