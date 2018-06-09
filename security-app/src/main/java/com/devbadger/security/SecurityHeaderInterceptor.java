package com.devbadger.security;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandles;
import java.util.Collections;

@Component
public class SecurityHeaderInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        handleRequestHeaders(request);
        handleResponseHeaders(response);
        return true;
    }

    private void handleRequestHeaders(HttpServletRequest request) {
        LOGGER.debug("SecurityHeaderInterceptor.handleRequestHeaders headers: {}",
                Collections.list(request.getHeaderNames()));

        LOGGER.debug("Authorization {}", request.getHeader("authorization"));
    }

    private void handleResponseHeaders(HttpServletResponse response) {

        LOGGER.debug("SecurityHeaderInterceptor.handleResponseHeaders headers: {}",
                Lists.newArrayList(response.getHeaderNames()));
    }
}

