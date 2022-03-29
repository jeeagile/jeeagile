package com.jeeagile.boot.userdetails;

import com.jeeagile.core.security.user.AgileBaseUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-07-09
 * @description
 */
@Data
public class AgileUserDetails implements UserDetails {

    /**
     * 用户信息
     */
    private AgileBaseUser userData;

    /**
     * 用户角色
     */
    private List<SimpleGrantedAuthority> authorities;

    @Override
    public String getPassword() {
        if (userData != null) {
            return userData.getPassword();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if (userData != null) {
            return userData.getUserName();
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
}