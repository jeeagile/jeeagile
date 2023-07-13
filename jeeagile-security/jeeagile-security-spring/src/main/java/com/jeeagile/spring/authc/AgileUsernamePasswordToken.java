package com.jeeagile.spring.authc;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AgileUsernamePasswordToken extends UsernamePasswordAuthenticationToken {
    public AgileUsernamePasswordToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public AgileUsernamePasswordToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    /**
     * 租户ID
     */
    @Getter
    @Setter
    private String tenantId;
    /**
     * 租户签名
     */
    @Getter
    @Setter
    private String tenantSign;
}
