package com.steamulo.api.auth;

import com.steamulo.annotation.NotAuthenticated;
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
public class AuthController {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    /**
     * WS de déconnexion
     */
    @NotAuthenticated
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(HttpServletResponse response, @RequestBody AccountCredentials user) throws ApiException, IOException {
        tokenAuthenticationService.login(response, user);
    }
}
