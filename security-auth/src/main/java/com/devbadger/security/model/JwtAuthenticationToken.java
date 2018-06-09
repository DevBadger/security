package com.devbadger.security.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken{

    private String token;

    public JwtAuthenticationToken() {
        super(null, null);
        this.token = null;
    }

    public JwtAuthenticationToken(String token) {
        super(null, null, new ArrayList<>());
        this.token = token;
    }

    public String getToken() {
        return token;
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

}
