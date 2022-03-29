package com.jeeagile.boot.method;


import com.jeeagile.boot.access.AgilePermissionEvaluator;
import com.jeeagile.boot.expression.AgileMethodSecurityExpressionRoot;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

/**
 * @author JeeAgile
 * @date 2021-07-09
 * @description
 */
public class AgileMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    private AgilePermissionEvaluator permissionEvaluator;

    public void setPermissionEvaluator(AgilePermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
        super.setPermissionEvaluator(permissionEvaluator);
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        AgileMethodSecurityExpressionRoot methodSecurityExpressionRoot = new AgileMethodSecurityExpressionRoot(authentication);
        methodSecurityExpressionRoot.setDefaultRolePrefix(null);
        methodSecurityExpressionRoot.setPermissionEvaluator(this.permissionEvaluator);
        methodSecurityExpressionRoot.setTrustResolver(this.trustResolver);
        methodSecurityExpressionRoot.setRoleHierarchy(getRoleHierarchy());
        return methodSecurityExpressionRoot;
    }
}
