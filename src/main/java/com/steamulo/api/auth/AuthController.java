package com.steamulo.api.auth;

import com.steamulo.annotation.NotAuthenticated;
import com.steamulo.api.auth.request.LoginRequest;
import com.steamulo.api.auth.response.LoginResponse;
import com.steamulo.exception.ApiException;
import com.steamulo.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by etienne on 04/08/17.
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * WS de déconnexion
     */
    @NotAuthenticated
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody LoginResponse login(@RequestBody LoginRequest user) throws ApiException, IOException {
        return authService.login(user);
    }
}
