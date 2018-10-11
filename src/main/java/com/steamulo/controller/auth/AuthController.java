package com.steamulo.controller.auth;

import com.steamulo.controller.auth.request.LoginRequest;
import com.steamulo.controller.auth.response.LoginResponse;
import com.steamulo.exception.ApiException;
import com.steamulo.services.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * WS concernant l'authentification
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * WS de connection
     */
    @PostMapping(value = "/login")
    public @ResponseBody LoginResponse login(@RequestBody LoginRequest user) {
        if (user.getLogin() == null || user.getPassword() == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials");
        }

        return authService.getToken(user.getLogin(), user.getPassword())
            .map(token -> LoginResponse.builder().token(token).build())
            .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials"));

    }
}
