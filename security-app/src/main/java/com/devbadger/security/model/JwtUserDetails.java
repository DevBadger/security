package com.devbadger.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class JwtUserDetails implements UserDetails {

    private String sessionId;
    private String token;
    private String sourceId;
    private Collection<? extends GrantedAuthority> authorities;


    public JwtUserDetails(String sessionId, String sourceId, String token, List<GrantedAuthority> grantedAuthorities) {

        this.sessionId = sessionId;
        this.sourceId = sourceId;
        this.token= token;
        this.authorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
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


    public String getSessionId() {
        return sessionId;
    }

    public String getToken() {
        return token;
    }


    public String getStoreId() {
        return sourceId;
    }

}
