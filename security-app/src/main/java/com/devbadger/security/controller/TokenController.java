package com.devbadger.security.controller;

import com.devbadger.model.AuthUser;
import com.devbadger.security.service.SecurityProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

    private static final Logger log = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    SecurityProviderService securityService;

    public TokenController(SecurityProviderService securityService) {
        this.securityService = securityService;
    }

    @PostMapping
    public ResponseEntity<String> generateJWT(@RequestBody final AuthUser authUser) {
        ResponseEntity<String> tokenResponse;

        try {
            String jwt = securityService.generateJWT(authUser);
            tokenResponse = new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("jwt NOT created due to exception", ex);

            // Good security practice is not to give any hint that an error occurred.
            // The 404 response is sufficient to notify client of the error, rather than a 500, etc.
            tokenResponse = new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }

        return tokenResponse;
    }
}
