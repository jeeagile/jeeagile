package com.jeeagile.spring.crypto;

import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileStringUtil;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @author JeeAgile
 * @date 2021-07-09
 * @description
 */
public class AgilePasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return AgileSecurityUtil.encryptPassword(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || AgileStringUtil.isEmpty(rawPassword)) {
            return false;
        }
        if (this.encode(rawPassword).equals(encodedPassword)) {
            return true;
        }
        return false;
    }
}
