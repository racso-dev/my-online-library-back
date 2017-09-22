package com.steamulo.services.user;

import com.steamulo.api.user.request.CreateUserRequest;
import com.steamulo.api.user.response.UserResponse;
import com.steamulo.exception.ApiException;
import com.steamulo.exception.EntityNotFoundException;
import com.steamulo.persistence.entity.User;
import com.steamulo.persistence.repository.UserRepository;
import com.steamulo.services.auth.TokenAuthenticationService;
import com.steamulo.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    /**
     * Service de création d'un user
     * @param request
     * @return
     * @throws ApiException
     */
    public void createUser(CreateUserRequest request) throws ApiException {

        User user = new User();
        user.setLogin(request.getLogin());
        user.setPassword(PasswordUtils.PASSWORD_ENCODER.encode(request.getPassword()));
        user.setProfile(request.getProfile());
        Optional<User> uLogin = userRepository.findByLogin(request.getLogin());
        if(uLogin.isPresent()) {
            throw new ApiException(HttpStatus.IM_USED, "Login déjà utilisé", "login");
        }
        userRepository.save(user);
    }

    /**
     * Service de récupération d'un user
     * @param idUser
     * @return
     */
    public UserResponse getUserResponse(long idUser) throws EntityNotFoundException {
        //On récupère le user
        return new UserResponse(getUserById(idUser));
    }

    /**
     * Method de récupération user donné
     * @param idUser
     * @return
     * @throws EntityNotFoundException
     */
    private User getUserById(long idUser) throws EntityNotFoundException {
        return userRepository.findOne(idUser).orElseThrow(() ->
                new EntityNotFoundException(idUser, "user")
        );
    }

    /**
     * Method de récupération user donné
     * @param login
     * @return
     * @throws EntityNotFoundException
     */
    private User getUserByLogin(String login) throws ApiException {
        return userRepository.findByLogin(login).orElseThrow(() ->
            new ApiException(HttpStatus.NOT_FOUND, "Aucun utilisateur avec le login " + login, "login")
        );
    }

    /**
     * Service de suppression d'un user
     * @param idUserToDelete
     * @param idUserRequester
     */
    public void deleteUser(long idUserToDelete, long idUserRequester) throws ApiException {
        //On ne peut pas supprimer son propre user
        if(idUserRequester == idUserToDelete){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Impossible de supprimer son propre user");
        }

        //On récupère le user
        User user = getUserById(idUserToDelete);

        //On supprime le user
        userRepository.delete(user);
    }
}
