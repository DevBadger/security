package com.devbadger.security.service;

import com.devbadger.model.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SecurityProviderServiceImpl implements SecurityProviderService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private long expiration;

    @Value("${security.jwt.notBefore}")
    private long notBefore;


    @Override
    public String generateJWT(AuthUser authUser) {

        Claims claims = createClaims(authUser);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }

    private Claims createClaims(AuthUser authUser){

        Claims claims = createPublicClaims();

        claims.put("sessionId", authUser.getSessionId());
        claims.put("sourceId", authUser.getSourceId());
        claims.put("userId",  authUser.getUserId());

        return claims;
    }

    private Claims createPublicClaims() {
        Claims claims = Jwts.claims();
        claims.setExpiration(new Date(System.currentTimeMillis() + expiration));
        claims.setNotBefore(new Date(System.currentTimeMillis() - notBefore));

        return claims;
    }
}
