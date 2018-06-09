package com.devbadger.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import spock.lang.Specification
import spock.lang.Unroll

class SecurityValidationServiceImplSpec extends Specification{

    def testClaims(String sessionId, String sourceId, String userId, long expiration, long notBefore) {
        Claims claims = Jwts.claims()
        claims.put("sessionId", sessionId)
        claims.put("sourceId", sourceId)
        claims.put("userId", userId)
        claims.setExpiration(new Date(System.currentTimeMillis() + expiration))
        claims.setNotBefore(new Date(System.currentTimeMillis() - notBefore))
        return claims
    }

    def createJWT(Claims claims, String secret) {
        String jwt = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret.bytes)
                .compact()
        return jwt
    }

    @Unroll("Scenario #num - can validate jwt when secret=#secret, expiration=#expiration, notBefore=#notBefore")
    def "testCanValidateJWT"() {

        given:
        def claims = testClaims(sessionId, sourceId, userId, expiration, notBefore)
        def jwt = createJWT(claims, secret)

        when:
        def service = new SecurityValidationServiceImpl(
                secret: secret,
                expiration: expiration,
                notBefore: notBefore
        )

        def flexUser = service.validateAndReturnFlexUser(jwt)

        then:
        assert claims.entrySet().size() == 5
        println "Claims count " + claims.entrySet().size()

        assert flexUser.sessionId == claims.get("sessionId")
        println claims.get("sessionId")

        assert flexUser.sourceId == claims.get("sourceId")
        println claims.get("sourceId")

        assert flexUser.userId == claims.get("userId")
        println claims.get("userId")

        where:
        num | sessionId        | sourceId | userId | secret       | expiration | notBefore
        1   | "123-123-123-123"| "667"   | "8048"| "test-secret"| 864_000_000| 864_000_000
    }

    @Unroll("Scenario #num - jwt = null, secret=#secret, expiration=#expiration, notBefore=#notBefore")
    def "testWithNullJWT"() {
        given:
        String jwt = null

        when:
        def service = new SecurityValidationServiceImpl(
                secret: secret,
                expiration: expiration,
                notBefore: notBefore
        )
        service.validateAndReturnFlexUser(jwt)

        then:
        thrown(SecurityAuthorizationException)

         where:
         num | sessionId        | sourceId | userId | secret       | expiration | notBefore
         1   | "123-123-123-123"| "667"   | "8048"| "test-secret"| 864_000_000| 864_000_000
    }


    @Unroll("Scenario #num - jwt created with different secret, secret=#secret, expiration=#expiration, notBefore=#notBefore")
    def "testJWTCreatedWithDifferentSecret"() {
        given:
        def claims = testClaims(sessionId, sourceId, userId, expiration, notBefore)
        def jwt = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, "make-jwt-different-secret".bytes)
                .compact()

        when:
        def service = new SecurityValidationServiceImpl(
                secret: secret,
                expiration: expiration,
                notBefore: notBefore
        )
        service.validateAndReturnFlexUser(jwt)

        then:
        thrown(SecurityAuthorizationException)

        where:
        num | sessionId        | sourceId | userId | secret       | expiration | notBefore
        1   | "123-123-123-123"| "667"   | "8048"| "test-secret"| 864_000_000| 864_000_000
    }
}
