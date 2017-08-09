package com.marketpay.services.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketpay.api.auth.response.TokenResponse;
import com.marketpay.exception.MarketPayException;
import com.marketpay.persistence.entity.UserToken;
import com.marketpay.persistence.repository.UserTokenRepository;
import com.marketpay.utils.DateUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static java.util.Collections.emptyList;
/**
 * Created by sgourio on 10/07/2017.
 */
@Component
public class TokenAuthenticationService {

    @Value("${jwt.expiration}")
    private long EXPIRATIONTIME;
    @Value("${jwt.secret}")
    private String SECRET;
    private final String COOKIE_NAME = "sid";

    @Autowired
    private UserTokenRepository userTokenRepository;

    /**
     * Service d'ajout d'une authentification
     * @param req
     * @param res
     * @param login
     * @throws IOException
     */
    public void addAuthentication(HttpServletRequest req, HttpServletResponse res, String login) throws IOException {
        //On connecte le user
        String newToken = connectUser(login);

        //On renvoit le token
        res.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        res.getWriter().write(mapper.writeValueAsString(new TokenResponse(newToken)));
    }

    /**
     * Service de connection d'un user
     * @param login
     * @return
     */
    public String connectUser(String login) {
        //On génère le nouveau token
        String newToken = newToken(login);

        //On ajoute le token en BDD avec la bonne date d'expiration
        UserToken userToken = new UserToken();
        userToken.setExpirationDateTime(DateUtils.getLocalDateTimeFromInstantMillis(System.currentTimeMillis() + EXPIRATIONTIME));
        userToken.setToken(newToken);
        userTokenRepository.save(userToken);

        return newToken;
    }

    /**
     * Service de récupération de l'authentification à partir d'une request
     * @param request
     * @return
     */
    public Authentication getAuthentication(HttpServletRequest request) throws MarketPayException {
        if( request.getCookies() != null ) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    String token = cookie.getValue();
                    if (!StringUtils.isEmpty(token)) {
                        // parse the token.
                        String user = Jwts.parser()
                            .setSigningKey(SECRET)
                            .parseClaimsJws(token)
                            .getBody()
                            .getSubject();

                        // On vérifie que le token n'est pas expiré
                        Optional<UserToken> userTokenOpt = userTokenRepository.findByToken(token);

                        if (userTokenOpt.isPresent()) {
                            if(userTokenOpt.get().getExpirationDateTime().isAfter(LocalDateTime.now())){
                                //S'il n'est pas expiré on met à jour sa date d'expiration
                                userTokenOpt.get().setExpirationDateTime(DateUtils.getLocalDateTimeFromInstantMillis(System.currentTimeMillis() + EXPIRATIONTIME));
                                userTokenRepository.save(userTokenOpt.get());
                            } else {
                                //S'il est expiré on le supprime de la BDD et on retourne un UNAUTHORIZED
                                userTokenRepository.delete(userTokenOpt.get());
                                throw new MarketPayException(HttpStatus.UNAUTHORIZED, "Token " + token + " expiré");
                            }
                        } else {
                            //Si le token n'est pas trouvé alors on rejète la request
                            throw new MarketPayException(HttpStatus.UNAUTHORIZED, "Token " + token + " inconnu");
                        }

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
            .setIssuedAt(DateUtils.toDateFromLocalDateTime(LocalDateTime.now()))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
    }

    /**
     * Service de déconnexion
     * @param token
     */
    public void logout(String token) {
        //On récupère le token
        Optional<UserToken> userTokenOpt = userTokenRepository.findByToken(token);
        if(userTokenOpt.isPresent()){
            //On le supprime pour la déconnexion
            userTokenRepository.delete(userTokenOpt.get());
        }
    }

}
