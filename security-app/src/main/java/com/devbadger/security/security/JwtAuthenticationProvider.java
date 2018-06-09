package com.devbadger.security.security;

import com.devbadger.model.AuthUser;
import com.devbadger.security.model.JwtAuthenticationToken;
import com.devbadger.security.model.JwtUserDetails;
import com.devbadger.security.service.APPSecurityValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {


    @Autowired
    private APPSecurityValidationService service;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) usernamePasswordAuthenticationToken;
        String jwt = jwtAuthenticationToken.getToken();

        AuthUser authUser = service.validateAndMore(jwt);

        if (authUser == null) {
            throw new RuntimeException("JWT Token is incorrect");
        }

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("admin");
        return new JwtUserDetails(authUser.getSessionId(), authUser.getSourceId(),
                jwt,
                grantedAuthorities);


//
//  TODO from example....
//        FlexUser authUser = validator.validate(token);
//
//        if (authUser == null) {
//            throw new RuntimeException("JWT Token is incorrect");
//        }
//
//        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
//                .commaSeparatedStringToAuthorityList(authUser.getRole());
//        return new JwtUserDetails(authUser.getSessionId(), authUser.getRepId(),
//                token,
//                grantedAuthorities);

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
    }
}
