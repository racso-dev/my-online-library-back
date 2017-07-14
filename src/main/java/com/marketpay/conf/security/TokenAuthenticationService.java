package com.marketpay.conf.security;
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
import java.util.Date;

import static java.util.Collections.emptyList;
/**
 * Created by sgourio on 10/07/2017.
 */
@Component
public class TokenAuthenticationService {

    private final long EXPIRATIONTIME = 86_400_000; // 1 day
    @Value("${jwt.secret}")
    private String SECRET;
    private final String COOKIE_NAME = "sid";

    public void addAuthentication(HttpServletRequest req, HttpServletResponse res, String username) {
        String JWT = Jwts.builder()
            .setPayload("{\"sub\":\"" + username +"\", \"profile\":\"test\", \"exp\":"+new Date(System.currentTimeMillis() + EXPIRATIONTIME).getTime()+"}")
            //.setSubject(username)
            //.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
        Cookie sessionCookie = new Cookie(COOKIE_NAME, JWT );
        res.addCookie(sessionCookie);
    }

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
}
