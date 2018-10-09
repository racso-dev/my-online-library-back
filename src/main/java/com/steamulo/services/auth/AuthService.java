package com.steamulo.services.auth;

import com.steamulo.persistence.entity.User;
import com.steamulo.services.user.UserService;
import com.steamulo.utils.PasswordUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service pour gérer les appels /auth
 */
@Component
public class AuthService {

    private final UserService userService;

    private final TokenAuthenticationService tokenAuthenticationService;

    public AuthService(UserService userService, TokenAuthenticationService tokenAuthenticationService) {
        this.userService = userService;
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    public Optional<String> getToken(String login, String password) {
        return userService.getUserByLogin(login).flatMap(user -> {
            if (PasswordUtils.PASSWORD_ENCODER.matches(password, user.getPassword())) {
                return Optional.of(tokenAuthenticationService.createNewToken(user.getId()));
            }
            return Optional.empty();
        });
    }

    public User getLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }
}
