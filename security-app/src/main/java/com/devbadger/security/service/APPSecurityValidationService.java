package com.devbadger.security.service;

import com.devbadger.model.AuthUser;

public interface APPSecurityValidationService {
    AuthUser validateAndMore(String jwt);
}
