package com.marketpay.api.auth;

import com.marketpay.annotation.Profile;
import com.marketpay.api.MarketPayInterceptor;
import com.marketpay.api.RequestContext;
import com.marketpay.api.auth.response.TokenResponse;
import com.marketpay.services.auth.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by etienne on 04/08/17.
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController extends MarketPayInterceptor {

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
}