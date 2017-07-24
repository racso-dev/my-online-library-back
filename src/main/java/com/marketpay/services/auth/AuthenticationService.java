package com.marketpay.services.auth;

import com.marketpay.persistence.entity.User;
import com.marketpay.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by etienne on 03/07/17.
 */
@Component
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Service de login avec login password
     * @param login
     * @param password
     * @return un User ou null
     */
    public Optional<User> login(String login, String password) {

        //On hash le password
        //TODO ETI

        //On récupère le user
        //TODO ETI

        //Si on a bien le user on connect, donc on génère le token
        //TODO ETI

        //On retourne le user
        //TODO ETI
        return userRepository.findUserByLoginAndPassword(login, password);
    }



}
