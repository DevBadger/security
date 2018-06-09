package com.devbadger.security.config;

import com.devbadger.security.security.JWTAuthorizationFilter;
import com.devbadger.security.service.SecurityValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@EnableWebSecurity
@Configuration
public class FlexSecurityAuthorizationConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SecurityValidationService validationService;

    JWTAuthorizationFilter jwtAuthorizationFilter;

    public JWTAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        if (jwtAuthorizationFilter == null) {
            jwtAuthorizationFilter = new JWTAuthorizationFilter(authenticationManager(), validationService);
        }
        return jwtAuthorizationFilter;
    }

    @Bean
    public FilterRegistrationBean registration() throws Exception {
        FilterRegistrationBean registration = new FilterRegistrationBean(jwtAuthorizationFilter());
        registration.setEnabled(false);
        return registration;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                .addFilter(jwtAuthorizationFilter())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Disable session creation on Spring Security
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/health");
    }
}