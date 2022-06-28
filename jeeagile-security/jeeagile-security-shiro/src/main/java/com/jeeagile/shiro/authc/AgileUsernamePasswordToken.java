package com.jeeagile.shiro.authc;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.UsernamePasswordToken;

public class AgileUsernamePasswordToken extends UsernamePasswordToken {
    /**
     *
     * @param username
     * @param password
     */
    public AgileUsernamePasswordToken(String username, String password) {
        super(username, password);
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
