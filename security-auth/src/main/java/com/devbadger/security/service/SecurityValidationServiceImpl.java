package com.devbadger.security.service;

import com.devbadger.model.AuthUser;
import com.devbadger.security.exception.SecurityAuthorizationException;
import com.devbadger.security.model.JwtAuthenticationToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityValidationServiceImpl implements SecurityValidationService {

    private static final Logger log = LoggerFactory.getLogger(SecurityValidationServiceImpl.class);

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private long expiration;

    @Value("${security.jwt.notBefore}")
    private long notBefore;


    /**
     * Important note: the Jwts.parser() conducts jwt validation!
     * There isn't a need to manually validate the secret, expiration & notBefore, and so on.
     * However, if Consensus introduces validation on private claims then, at that time, validation is required.
     */
    @Override
    public AuthUser validateAndReturnFlexUser(String jwt) throws SecurityAuthorizationException {

        AuthUser authUser;

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(jwt)
                    .getBody();

            authUser = getUser(claims);
        } catch (Exception e) {
             throw new SecurityAuthorizationException("JWT could not be parsed. Exception : " + e);
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

    @Override
    public void setJWTOnSecurityContext(String token) {
        JwtAuthenticationToken jwtAuthenticationToken;
        if(token != null) {
            jwtAuthenticationToken = new JwtAuthenticationToken(token);
        } else {
            // Default constructor informs spring-security call is NOT authorized.
            jwtAuthenticationToken = new JwtAuthenticationToken();
        }
        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
    }

    @Override
    public String getJWTFromSecurityContext() {

            String jwt = "";

            try {
                JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
                jwt = jwtAuthenticationToken.getToken();
                if (jwt == null || jwt.isEmpty())
                    throw new SecurityAuthorizationException("jwt missing from the SecurityContextHolder context.");

            } catch (Exception e) {
                log.error("module='security-authorization' method='getJWTFromSecurityContext' ", e);
            }

        return jwt;
    }
}
