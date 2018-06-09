package com.devbadger.security.service;

import com.devbadger.model.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AppSecurityValidationServiceImpl implements AppSecurityValidationService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Override
    public AuthUser validateAndMore(String jwt) {

        AuthUser authUser = null;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(jwt)
                    .getBody();

            authUser = getUser(claims);
        } catch (Exception e) {
            System.out.println(e);
        }

        return authUser;
    }

    private AuthUser getUser(Claims claims) {
        AuthUser authUser = new AuthUser();

        authUser.setSessionId((String) claims.get("sessionId"));
        authUser.setSourceId((String) claims.get("sourceId"));
        authUser.setUserId((String) claims.get("userId"));

        return authUser;
    }
}
