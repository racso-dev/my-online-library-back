package com.steamulo.conf.technical;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steamulo.controller.JWTAuthenticationFilter;
import com.steamulo.services.auth.TokenAuthenticationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenAuthenticationService tokenAuthenticationService;
    private final ObjectMapper objectMapper;

    public WebSecurityConfig(TokenAuthenticationService tokenAuthenticationService, ObjectMapper objectMapper) {
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
            .anyRequest().authenticated()
            .and()
            // And filter other requests to check the presence of JWT in header
            .addFilterBefore(new JWTAuthenticationFilter(tokenAuthenticationService, objectMapper), UsernamePasswordAuthenticationFilter.class);
    }
}
