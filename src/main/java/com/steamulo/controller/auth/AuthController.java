package com.steamulo.controller.auth;

import com.steamulo.controller.auth.request.LoginRequest;
import com.steamulo.controller.auth.request.RegisterRequest;
import com.steamulo.controller.auth.response.LoginResponse;
import com.steamulo.controller.auth.response.RegisterResponse;
import com.steamulo.enums.UserRole;
import com.steamulo.exception.ApiException;
import com.steamulo.services.auth.AuthService;
import com.steamulo.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * WS concernant l'authentification
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
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

    /**
     * WS de connection
     */
    @PostMapping(value = "/register")
    public @ResponseBody RegisterResponse register(@RequestBody RegisterRequest request) {
        if (request.getLogin() == null || request.getPassword() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Bad request, login and password are required");
        }

        userService
                .createUser(request.getLogin(), request.getPassword(), UserRole.valueOf("USER"), request.getFirstName(),
                        request.getLastName())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Email déjà utilisé"));

        return authService.getToken(request.getLogin(), request.getPassword())
                .map(token -> RegisterResponse.builder().token(token).build())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials"));

    }
}
