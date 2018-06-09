package com.devbadger.security.service;

import com.devbadger.model.AuthUser;

public interface AppSecurityValidationService {
    AuthUser validateAndMore(String jwt);
}
