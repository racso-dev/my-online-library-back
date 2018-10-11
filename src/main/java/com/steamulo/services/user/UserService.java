package com.steamulo.services.user;

import com.steamulo.enums.UserRole;
import com.steamulo.exception.ApiException;
import com.steamulo.persistence.entity.User;
import com.steamulo.persistence.repository.UserRepository;
import com.steamulo.utils.PasswordUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service pour gérer les appels /user
 */
@Component
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creation d'un utilisateur
     * @param login
     * @param password
     * @param userRole
     */
    public Optional<User> createUser(String login, String password, UserRole userRole) {
        Optional<User> uLogin = userRepository.findByLogin(login);
        if(uLogin.isPresent()) {
            return Optional.empty();
        }

        User user = User.builder()
            .login(login)
            .password(PasswordUtils.PASSWORD_ENCODER.encode(password))
            .role(userRole)
            .build();

        return Optional.of(userRepository.save(user));
    }

    /**
     * Method de récupération user donné
     * @param idUser
     * @return
     */
    public Optional<User> getUserById(long idUser) {
        return userRepository.findById(idUser);
    }

    /**
     * Method de récupération user donné
     * @param login
     * @return
     */
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    /**
     * Service de suppression d'un user
     * @param idUser
     */
    public void deleteUser(long idUser)  {
        getUserById(idUser).ifPresent(user -> userRepository.delete(user));
    }
}
