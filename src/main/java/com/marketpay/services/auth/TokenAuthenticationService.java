package com.marketpay.services.auth;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketpay.api.auth.response.TokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security
    .authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static java.util.Collections.emptyList;
/**
 * Created by sgourio on 10/07/2017.
 */
@Component
public class TokenAuthenticationService {

    private final long EXPIRATIONTIME = 300_000; // 5min
    @Value("${jwt.secret}")
    private String SECRET;
    private final String COOKIE_NAME = "sid";

    /**
     * Service d'ajout d'une authentification
     * @param req
     * @param res
     * @param username
     * @throws IOException
     */
    public void addAuthentication(HttpServletRequest req, HttpServletResponse res, String username) throws IOException {
        res.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        res.getWriter().write(mapper.writeValueAsString(new TokenResponse(newToken(username))));
    }

    /**
     * Service de récupération de l'authentification à partir d'une request
     * @param request
     * @return
     */
    public Authentication getAuthentication(HttpServletRequest request) {
        if( request.getCookies() != null ) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    String token = cookie.getValue();
                    if (token != null) {
                        // parse the token.
                        String user = Jwts.parser()
                            .setSigningKey(SECRET)
                            .parseClaimsJws(token)
                            .getBody()
                            .getSubject();

                        return user != null ?
                            new UsernamePasswordAuthenticationToken(user, null, emptyList()) :
                            null;
                    }
                }
            }

        }
        return null;
    }

    /**
     * Méthod qui créé un nouveau token
     * @param username
     * @return
     */
    public String newToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
    }

}
