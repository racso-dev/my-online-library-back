package com.steamulo.services.auth;

import com.steamulo.persistence.entity.User;
import com.steamulo.services.user.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service pour gérer les appels /auth
 */
@Component
public class AuthService {

    private final UserService userService;

    private final TokenAuthenticationService tokenAuthenticationService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserService userService, TokenAuthenticationService tokenAuthenticationService,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Optional<String> getToken(String login, String password) {
        return userService.getUserByLogin(login).flatMap(user -> {
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return Optional.of(tokenAuthenticationService.createNewToken(user.getId()));
            }
            return Optional.empty();
        });
    }

    public User getAuthUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }

    public Optional<User> getUser() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return Optional.of((User) SecurityContextHolder.getContext().getAuthentication().getDetails());
    }
}
