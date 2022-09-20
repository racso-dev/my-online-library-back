package com.steamulo.services.auth;

import com.steamulo.conf.properties.JwtProperties;
import com.steamulo.services.user.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

/**
 * Service de gestion du JWT Token
 */
@Component
public class TokenAuthenticationService {

    private final JwtProperties jwtProperties;
    private final UserService userService;

    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer";

    public TokenAuthenticationService(JwtProperties jwtProperties, UserService userService) {
        this.jwtProperties = jwtProperties;
        this.userService = userService;
    }

    /**
     * Service de récupération de l'authentification à partir d'une request
     * 
     * @param request
     * @return
     */
    public Optional<UserAuthentication> getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(HEADER_STRING);
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return Optional.empty();
        }

        String userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();

        if (userId == null) {
            return Optional.empty();
        }

        return userService.getUserById(Long.valueOf(userId)).map(UserAuthentication::new);
    }

    /**
     * Méthod qui créé un nouveau token
     * 
     * @param idUser
     * @return
     */
    String createNewToken(Long idUser) {
        return Jwts.builder()
                .setSubject(idUser.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }
}
