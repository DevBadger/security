package com.devbadger.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Validator {

    private static final Logger log = LoggerFactory.getLogger(Validator.class);

    private String secret;

    public void initValidator(String secret) {
        this.secret = secret;
    }

    /**
     * Important note: the Jwts.parser() conducts validation on the expiration & notBefore, so need to manually validate.
     */
    public boolean isValidJWT(String jwt) {
        boolean isValid = false;
        try {
            Claims claims = getClaims(jwt);
            isValid = containsValidPrivateFlexClaims(claims);
        } catch (Exception e) {
            log.error("module='security-common' method='isValidJWT' ", e);
        }
        return isValid;
    }

    public Claims getClaims(String jwt) {
        Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(jwt)
                    .getBody();

        return claims;
    }

    private boolean containsValidPrivateFlexClaims (Claims claims){
        boolean isValid = false;
        String userId = claims.get("userId").toString();
        String sourceId = claims.get("sourceId").toString();
        String sessionId = claims.get("sessionId").toString();

        if( !userId.isEmpty() && !sourceId.isEmpty() && !sessionId.isEmpty()) isValid = true;

        return isValid;
    }
}
