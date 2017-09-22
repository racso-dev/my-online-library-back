package com.steamulo.services.auth;

import com.steamulo.exception.ApiException;
import com.steamulo.filter.security.AccountCredentials;
import com.steamulo.utils.DateUtils;
import com.steamulo.utils.PasswordUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;

import static java.util.Collections.emptyList;
/**
 * Created by sgourio on 10/07/2017.
 */
@Component
public class TokenAuthenticationService {

    @Autowired
    private ApiUserDetailsService apiUserDetailsService;

    @Value("${jwt.expiration}")
    private long EXPIRATIONTIME;
    @Value("${jwt.secret}")
    private String SECRET;
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    /**
     * Service d'ajout d'une authentification
     * @param res
     * @param login
     */
    public void addAuthentication(HttpServletResponse res, String login) {
        //On connecte le user
        String newToken = newToken(login);

        //On renvoit le token
        res.setContentType("application/json");
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + newToken);
    }

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
                .setSigningKey(SECRET)
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
     * @param username
     * @return
     */
    public String newToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(DateUtils.toDateFromLocalDateTime(LocalDateTime.now()))
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
    }

    /**
     * Service de connexion
     */
    public void login(HttpServletResponse res, AccountCredentials user) throws ApiException {
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials");
        }
        UserDetails userDetails;
        try {
            userDetails = apiUserDetailsService.loadUserByUsername(user.getUsername());
        } catch(UsernameNotFoundException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials");
        }
        String password = user.getPassword();
        String pwd = userDetails.getPassword();
        if (!PasswordUtils.PASSWORD_ENCODER.matches(password, pwd)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials");
        }
        addAuthentication(res, user.getUsername());
    }
}
