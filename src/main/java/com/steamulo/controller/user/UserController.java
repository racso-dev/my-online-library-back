package com.steamulo.controller.user;

import com.steamulo.controller.user.request.CreateUserRequest;
import com.steamulo.controller.user.request.UpdateUserRequest;
import com.steamulo.controller.user.response.UserResponse;
import com.steamulo.enums.UserRole;
import com.steamulo.exception.ApiException;
import com.steamulo.persistence.entity.User;
import com.steamulo.services.auth.AuthService;
import com.steamulo.services.user.UserService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

/**
 * WS concernant la gestion des users
 */
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    /**
     * WS de récupération des users
     *
     * @return les users
     */
    @PreAuthorize("hasAuthority('USER_GET')")
    @GetMapping()
    public List<User> getUsers(@RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "activated", required = false) Boolean activated) {
        return userService.getUsersByCriteria(email, firstName, lastName, activated);
    }

    /**
     * WS de récupération d'un user
     *
     * @param idUser l'identifiant du user
     * @return le user s'il existe, une erreur 400 sinon
     */
    @PreAuthorize("hasAuthority('USER_GET')")
    @GetMapping(value = "/{idUser}")
    public @ResponseBody UserResponse getUser(@PathVariable(value = "idUser") long idUser) {
        log.info("Récupération du user {}", idUser);
        return userService.getUserById(idUser)
                .map(user -> UserResponse.builder().login(user.getLogin()).role(user.getRole().name()).build())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                        String.format("Impossible de récupérer le user %d. User inconnu.", idUser)));
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
        userService
                .createUser(request.getLogin(), request.getPassword(), UserRole.valueOf(role), request.getFirstName(),
                        request.getLastName())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Login déjà utilisé"));
        log.info("Creation du nouveau user");
    }

    /**
     * WS de suppression d'un user
     *
     * @param idUser l'identifiant du user
     */
    @PreAuthorize("hasAuthority('USER_DELETE') && @userService.hasRightToDeleteUser(principal, #idUser)")
    @DeleteMapping(value = "/{idUser}")
    public void deleteUser(@PathVariable(value = "idUser") long idUser) {
        log.info("Suppression du user {}", idUser);
        userService.deleteUser(idUser);
    }

    /**
     * WS de récupération du user connecté
     *
     * @return le user connecté
     */
    @PreAuthorize("hasAuthority('USER_GET_SELF')")
    @GetMapping(value = "/self")
    public ResponseEntity<UserResponse> getCurentUser() {
        User loggedInUser = authService.getAuthUser();
        log.info("Récupération du user connecté {}", loggedInUser.getId());
        return ResponseEntity.ok()
                .body(UserResponse.builder().login(loggedInUser.getLogin()).role(loggedInUser.getRole().name())
                        .firstName(loggedInUser.getFirstName())
                        .lastName(loggedInUser.getLastName()).build());
    }

    /**
     * WS de modification du user connecté
     *
     * @param request la demande de modification du user
     */
    @PreAuthorize("hasAuthority('USER_UPDATE_SELF')")
    @PutMapping(value = "/self")
    public ResponseEntity<Void> updateCurentUser(@RequestBody @Valid UpdateUserRequest request) {
        User loggedInUser = authService.getAuthUser();
        log.info("Modification du user connecté {}", loggedInUser.getId());
        Optional<String> password = Optional.ofNullable(request.getPassword());
        userService.updateUser(loggedInUser, request.getLogin(), password, request.getFirstName(),
                request.getLastName());
        return ResponseEntity.noContent().build();
    }

    /**
     * WS d'activation ou de désactivation d'un user
     *
     * @param idUser l'identifiant du user
     */
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @PostMapping(value = "/activate/{idUser}")
    public ResponseEntity<Void> updateUser(@PathVariable(value = "idUser") long idUser) {
        User user = userService.getUserById(idUser)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "User inconnu"));
        log.info("Modification du user {} par admin", user.getId());
        if (user.getActivated().booleanValue()) {
            userService.deactivateUser(user);
        } else {
            userService.activateUser(user);
        }
        return ResponseEntity.noContent().build();
    }

}
