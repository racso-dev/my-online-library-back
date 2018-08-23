package com.steamulo.services.auth;

import com.steamulo.conf.properties.JwtProperties;
import com.steamulo.exception.ApiException;
import com.steamulo.utils.DateUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;

import static java.util.Collections.emptyList;

/**
 * Service de gestion du JWT Token
 */
@Component
public class TokenAuthenticationService {

    @Autowired
    private JwtProperties jwtProperties;

    private final String TOKEN_PREFIX = "Bearer";
    private final String HEADER_STRING = "Authorization";

    /**
     * Service de récupération de l'authentification à partir d'une request
     * @param request
     * @return
     */
    public Authentication getAuthentication(HttpServletRequest request) throws ApiException {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String user = Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();

            return user != null ?
                new UsernamePasswordAuthenticationToken(user, null, emptyList()) :
                null;
        }
        return null;
    }

    /**
     * Méthod qui créé un nouveau token
     * @param login
     * @return
     */
    public String createNewToken(String login) {
        return Jwts.builder()
            .setSubject(login)
            .setIssuedAt(DateUtils.toDateFromLocalDateTime(LocalDateTime.now()))
            .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
            .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
            .compact();
    }
}
