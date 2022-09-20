package com.steamulo.services.user;

import com.steamulo.enums.UserRole;
import com.steamulo.persistence.entity.User;
import com.steamulo.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service pour gérer les appels /user
 */
@Component
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Creation d'un utilisateur
     *
     * @param login    le login de l'utilisateur
     * @param password le mot de passe de l'utilisateur
     * @param userRole le role de l'utilisateur
     */
    public Optional<User> createUser(String login, String password, UserRole userRole, String firstName,
            String lastName) {
        Optional<User> uLogin = userRepository.findByLogin(login);
        if (uLogin.isPresent()) {
            return Optional.empty();
        }

        User user = User.builder()
                .login(login)
                .password(bCryptPasswordEncoder.encode(password))
                .role(userRole)
                .firstName(firstName)
                .lastName(lastName)
                .build();

        return Optional.of(userRepository.save(user));
    }

    /**
     * Method de récupération user donné
     *
     * @param idUser l'identifiant de l'utilisateur
     * @return le user correspondant s'il existe, empty sinon
     */
    public Optional<User> getUserById(long idUser) {
        return userRepository.findById(idUser);
    }

    /**
     * Method de récupération user donné
     *
     * @param login le login de l'utilisateur
     * @return l'utilisateur s'il existe, empty sinon
     */
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    /**
     * Service de suppression d'un user
     *
     * @param idUser l'identifiant de l'utilisateur à supprimer
     */
    public void deleteUser(long idUser) {
        getUserById(idUser).ifPresent(userRepository::delete);
    }

    /**
     * Détermine si on a l'utilisateur passé en paramètre possède le droit de
     * supprimer le user associé à l'identifiant
     *
     * @param user           l'utilisateur demandant la suppression
     * @param idUserToDelete l'identifiant de l'utilisateur que l'on souhaite
     *                       supprimer
     * @return true si c'est le cas, false sinon
     */
    public boolean hasRightToDeleteUser(User user, long idUserToDelete) {
        boolean isSameUser = !user.getId().equals(idUserToDelete);
        if (isSameUser) {
            LOGGER.error("L'utilisateur {} a demandé la suppression de son user : opération impossible.", user.getId());
        }

        return !isSameUser;
    }

    public void updateUser(User user, String login, Optional<String> password, String firstName, String lastName) {
        if (password.isPresent()) {
            user.setPassword(bCryptPasswordEncoder.encode(password.get()));
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userRepository.save(user);
    }

}
