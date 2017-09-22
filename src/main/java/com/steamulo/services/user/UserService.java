package com.steamulo.services.user;

import com.steamulo.api.user.request.EditMyPasswordRequest;
import com.steamulo.api.user.request.EditUserRequest;
import com.steamulo.api.user.response.EditMyResponse;
import com.steamulo.exception.ApiException;
import com.steamulo.exception.EntityNotFoundException;
import com.steamulo.persistence.entity.User;
import com.steamulo.persistence.repository.UserRepository;
import com.steamulo.services.auth.TokenAuthenticationService;
import com.steamulo.services.user.resource.UserInformationResource;
import com.steamulo.services.user.resource.UserResource;
import com.steamulo.utils.PasswordUtils;
import com.steamulo.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    /**
     * Génère une liste de UserResource à partir d'une liste de User
     * @param userList
     * @return la liste géneré de UserResource
     */
    private List<UserResource> generateUserResourceListFromUserList(List<User> userList) {
        return userList.stream()
            .map(UserResource::new)
            .collect(Collectors.toList());
    }

    /**
     * Service de création d'un user
     * @return
     * @throws ApiException
     */
    public Long createUser(UserResource userResource) throws ApiException {
        //On créé le user
        User user = new User();
        user.setProfile(userResource.getProfile());

        //On set le password avec une valeur aléatoire
        //Le mot de passe sera changé à la premier authentification
        user.setPassword(PasswordUtils.PASSWORD_ENCODER.encode(RandomUtils.getRandowString(40)));

        //On set les autres champs: email, login, firstName, lastName
        //Et si tout est ok on sauvegarde le user
        EditUserRequest request = new EditUserRequest();
        request.setLogin(userResource.getLogin());
        user = editUserEntity(user, request);

        //Tout est OK on sauvegarde le user
        user = userRepository.save(user);

        return user.getId();
    }

    /**
     * Service d'edition d'un user
     * @param idUser
     * @param request
     * @return
     */
    public UserResource editUser(long idUser, EditUserRequest request) throws ApiException {
        //On récupère le user
        User user = getUserEntity(idUser);

        //On met à jour le user
        user = editUserEntity(user, request);

        return getUserResource(user);
    }

    /**
     * Service de récupération d'un user
     * @param idUser
     * @return
     */
    public UserResource getUser(long idUser) throws EntityNotFoundException {
        //On récupère le user
        return getUserResource(getUserEntity(idUser));
    }

    /**
     * Method qui retourne un userResource à partir d'un user
     * @param user
     * @return
     * @throws EntityNotFoundException
     */
    public UserResource getUserResource(User user) throws EntityNotFoundException {
        UserResource resource = new UserResource(user);

        return resource;
    }

    /**
     * Method de récupération userEntity donné
     * @param idUser
     * @return
     * @throws EntityNotFoundException
     */
    private User getUserEntity(long idUser) throws EntityNotFoundException {
        return userRepository.findOne(idUser).orElseThrow(() ->
                new EntityNotFoundException(idUser, "user")
        );
    }

    /**
     * Method qui met à jour un userEntity à partir d'une request
     * @param user
     * @param request
     * @return
     * @throws ApiException
     */
    private User editUserEntity(User user, EditUserRequest request) throws ApiException {
        //On vérifie que le login n'existe pas déjà
        Optional<User> uLogin = userRepository.findByLogin(request.getLogin());
        if(uLogin.isPresent() && (user.getId() == null || !user.getId().equals(uLogin.get().getId()))){
            throw new ApiException(HttpStatus.IM_USED, "Login déjà utilisé", "login");
        }
        user.setLogin(request.getLogin());

        //Tout est OK on sauvegarde le user
        return userRepository.save(user);
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
        User user = getUserEntity(idUserToDelete);

        //On supprime le user
        userRepository.delete(user);
    }

    /**
     * Service d'edition du user connecté
     * @param idUser
     * @param request
     * @return
     */
    public EditMyResponse editMyUser(long idUser, EditUserRequest request) throws ApiException {
        EditMyResponse response = new EditMyResponse();

        //On récupère le user
        User user = getUserEntity(idUser);

        String previousLogin = user.getLogin();

        //On met à jour le user
        user = editUserEntity(user, request);

        response.setUser(getUserResource(user));
        return response;
    }

    /**
     * Service d'edition du password du user connecté
     * @param idUser
     * @param request
     * @return
     */
    public void editMyPassword(long idUser, EditMyPasswordRequest request) throws ApiException {
        //On récupère le user
        User user = getUserEntity(idUser);

        //On vérifie l'ancien password
        if(!PasswordUtils.PASSWORD_ENCODER.matches(request.getOldPassword(), user.getPassword())){
            throw new ApiException(HttpStatus.UNAUTHORIZED, "OldPassword incorrect");
        }

        //On change le password
        user.setPassword(PasswordUtils.PASSWORD_ENCODER.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
