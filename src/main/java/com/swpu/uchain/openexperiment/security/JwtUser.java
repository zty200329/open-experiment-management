package com.swpu.uchain.openexperiment.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 实现UserDetails
 */
public class JwtUser implements UserDetails {

    private String userCode;
    private String password;
    private List<String> urls;

    public JwtUser(String userCode, String password, List urls) {
            this.userCode = userCode;
            this.password = password;
            this.urls = urls;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        for (String s : urls) {
            auths.add(new SimpleGrantedAuthority(s));
        }
        return auths;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userCode;
    }

    /**
     * 默认有效账户
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 默认账户没有被锁
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 默认凭证有效
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 默认账户可用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }


}
