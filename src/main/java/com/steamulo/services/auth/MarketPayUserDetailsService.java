package com.steamulo.services.auth;

import com.steamulo.persistence.entity.User;
import com.steamulo.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by etienne on 03/07/17.
 */
@Component
public class MarketPayUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //On récupère le user par son login
        Optional<User> userOpt = userRepository.findByLogin(username);
        if(!userOpt.isPresent()){
            throw new UsernameNotFoundException("No user for login " + username);
        }

        return new MarketPayUserDetails(userOpt.get());
    }
}
