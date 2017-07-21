package com.marketpay.services.auth;

import com.marketpay.persistence.entity.User;
import com.marketpay.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by etienne on 03/07/17.
 */
@Component
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Permet de retrouver les informations d'un utilisateur via son login / mot de passe
     * @param login
     * @param password
     * @return un User ou null
     */
    public User findUserFromConnectInformation(String login, String password) {
        return userRepository.findUserByLoginAndPassword(login, password);
    }

}
