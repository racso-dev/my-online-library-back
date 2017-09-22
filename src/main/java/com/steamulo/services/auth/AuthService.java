package com.steamulo.services.auth;

import com.steamulo.api.auth.request.LoginRequest;
import com.steamulo.api.auth.response.LoginResponse;
import com.steamulo.exception.ApiException;
import com.steamulo.persistence.entity.User;
import com.steamulo.persistence.repository.UserRepository;
import com.steamulo.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    /**
     * Service de connexion
     * @param loginRequest
     * @return
     * @throws ApiException
     */
    public LoginResponse login(LoginRequest loginRequest) throws ApiException {
        if (loginRequest.getLogin() == null || loginRequest.getPassword() == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials");
        }
        User user = userRepository.findByLogin(loginRequest.getLogin()).orElseThrow(() ->
            new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials")
        );
        String password = loginRequest.getPassword();
        String pwd = user.getPassword();
        if (!PasswordUtils.PASSWORD_ENCODER.matches(password, pwd)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials");
        }
        return new LoginResponse(tokenAuthenticationService.createNewToken(loginRequest.getLogin()));
    }
}
