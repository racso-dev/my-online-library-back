package com.steamulo.api.auth;

import com.steamulo.annotation.NotAuthenticated;
import com.steamulo.annotation.Profile;
import com.steamulo.api.ApiController;
import com.steamulo.api.RequestContext;
import com.steamulo.exception.ApiException;
import com.steamulo.filter.security.AccountCredentials;
import com.steamulo.services.auth.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by etienne on 04/08/17.
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController extends ApiController {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    /**
     * WS de déconnexion
     */
    @Profile({})
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout() {
        //Si on rentre dans le controller c'est qu'on a pas été rejeté donc le token est encore valide
        tokenAuthenticationService.logout(RequestContext.get().getToken());
    }

    /**
     * WS de déconnexion
     */
    @NotAuthenticated
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(HttpServletResponse response, @RequestBody AccountCredentials user) throws ApiException, IOException {
        tokenAuthenticationService.login(response, user);
    }
}
