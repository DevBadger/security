package com.devbadger.security.service;

import com.devbadger.model.AuthUser;

public interface SecurityProviderService {

    String generateJWT(AuthUser authUser);
}
