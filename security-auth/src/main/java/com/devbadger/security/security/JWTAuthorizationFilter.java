package com.devbadger.security.security;

import com.devbadger.config.SecurityProperties;
import com.devbadger.security.exception.SecurityAuthorizationException;
import com.devbadger.security.service.SecurityValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StopWatch;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    private SecurityValidationService validationService;

    public JWTAuthorizationFilter(AuthenticationManager authManager, SecurityValidationService validationService) {
        super(authManager);
        this.validationService = validationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String jwt = getFlexJWTFromRequest(req);
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            validationService.validateAndReturnFlexUser(jwt);

            stopWatch.stop();
            log.info("methodName=\"doFilterInternal\" arguments=\"jwt=---jwt is not logged, for obvious reasons.---\" executionTime=\"{}\"", stopWatch.getTotalTimeMillis());

        } catch (SecurityAuthorizationException ex) {
            log.error("methodNameThatThrew=\"doFilterInternal\" arguments=\"jwt={}\" exceptionMessage=\"{}\"", jwt, ex.toString());

            // Setting the jwt to null will cause a 403 Unauthorized response to be returned (spring-security in action).
            // See details where creating the JwtAuthenticationToken with null "credentials" means not authorization.
            jwt = null;
        }
        validationService.setJWTOnSecurityContext(jwt);
        chain.doFilter(req, res);
    }

    private String getFlexJWTFromRequest(HttpServletRequest request) {

        String token = null;
        String header = request.getHeader(SecurityProperties.HEADER_AUTHORIZATION);
        if (header != null && header.startsWith(SecurityProperties.TOKEN_PREFIX)) {
            token = request.getHeader(SecurityProperties.HEADER_AUTHORIZATION);
            if (token != null) {
               token = token.replace(SecurityProperties.TOKEN_PREFIX, "");
            }
        }
        return token;
    }
}
