package com.marketpay.filter.security;

import com.marketpay.services.auth.TokenAuthenticationService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sgourio on 10/07/2017.
 */
public class JWTAuthenticationFilter extends GenericFilterBean {

    private final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private final TokenAuthenticationService tokenAuthenticationService;

    public JWTAuthenticationFilter(TokenAuthenticationService tokenAuthenticationService) {
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            Authentication authentication = tokenAuthenticationService.getAuthentication((HttpServletRequest) request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            //Si le token est expiré on retourne un unauthorized
            LOGGER.info("Authentication Unauthorized : " + e.getMessage());
            ((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
