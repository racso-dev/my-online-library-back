package com.steamulo.controller.user;

import com.steamulo.controller.user.request.CreateUserRequest;
import com.steamulo.controller.user.response.UserResponse;
import com.steamulo.enums.UserRole;
import com.steamulo.exception.ApiException;
import com.steamulo.persistence.entity.User;
import com.steamulo.services.auth.AuthService;
import com.steamulo.services.user.UserService;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * WS concernant la gestion des users
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    /**
     * WS de récupération d'un user
     *
     * @param idUser l'identifiant du user
     * @return le user s'il existe, une erreur 400 sinon
     */
    @PreAuthorize("hasAuthority('USER_GET')")
    @GetMapping(value = "/{idUser}")
    public @ResponseBody
    UserResponse getUser(@PathVariable(value = "idUser") long idUser) {
        LOGGER.info("Récupération du user {}", idUser);
        return userService.getUserById(idUser)
            .map(user -> UserResponse.builder().login(user.getLogin()).role(user.getRole().name()).build())
            .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, String.format("Impossible de récupérer le user %d. User inconnu.", idUser)));
    }

    /**
     * WS de création d'un user
     *
     * @param request la demande de création du user
     */
    @PreAuthorize("hasAuthority('USER_CREATE')")
    @PostMapping(value = "/create")
    public void createUser(@RequestBody @Valid CreateUserRequest request) {
        String role = request.getRole().toUpperCase();
        if (!EnumUtils.isValidEnum(UserRole.class, role)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Role inexistant");
        }
        userService.createUser(request.getLogin(), request.getPassword(), UserRole.valueOf(role), request.getFirstName(), request.getLastName())
            .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Login déjà utilisé"));
        LOGGER.info("Creation du nouveau user");
    }

    /**
     * WS de suppression d'un user
     *
     * @param idUser l'identifiant du user
     */
    @PreAuthorize("hasAuthority('USER_DELETE') && @userService.hasRightToDeleteUser(principal, #idUser)")
    @DeleteMapping(value = "/{idUser}")
    public void deleteUser(@PathVariable(value = "idUser") long idUser) {
        LOGGER.info("Suppression du user {}", idUser);
        userService.deleteUser(idUser);
    }

    /**
     * WS de récupération du user connecté
     *
     * @return le user connecté
     */
    @PreAuthorize("hasAuthority('USER_GET_SELF')")
    @GetMapping(value = "/self")
    public @ResponseBody
    UserResponse getCurentUser() {
        User loggedInUser = authService.getAuthUser();
        LOGGER.info("Récupération du user connecté {}", loggedInUser.getId());
        return UserResponse.builder().login(loggedInUser.getLogin()).role(loggedInUser.getRole().name()).build();
    }

}
