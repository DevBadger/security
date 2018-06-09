package com.devbadger.security.service;
import com.devbadger.model.AuthUser;
import com.devbadger.security.exception.SecurityAuthorizationException;

public interface SecurityValidationService {
    AuthUser validateAndReturnFlexUser(String jwt) throws SecurityAuthorizationException;
    void setJWTOnSecurityContext(String token);
    String getJWTFromSecurityContext();
    }
