package com.steamulo.api.user;

import com.steamulo.annotation.NotAuthenticated;
import com.steamulo.annotation.Permission;
import com.steamulo.api.RequestContext;
import com.steamulo.api.user.request.CreateUserRequest;
import com.steamulo.api.user.response.UserResponse;
import com.steamulo.exception.ApiException;
import com.steamulo.references.PERMISSION;
import com.steamulo.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * WS de récupération d'un user
     * @param idUser
     * @return
     */
    @Permission(PERMISSION.USER_GET)
    @RequestMapping(value = "/{idUser}", method = RequestMethod.GET)
    public @ResponseBody UserResponse getUser(@PathVariable(value = "idUser") long idUser) throws ApiException {
        LOGGER.info("Récupération du user " + idUser);
        return userService.getUserResponse(idUser);
    }

    /**
     * WS de création d'un user
     * @param request
     * @return
     */
    @NotAuthenticated
    @Permission(PERMISSION.USER_CREATE)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createUser(@RequestBody @Valid CreateUserRequest request) throws ApiException {
        userService.createUser(request);
        LOGGER.info("Creation du nouveau user");
    }

    /**
     * WS de suppression d'un user
     * @param idUser
     */
    @Permission(PERMISSION.USER_DELETE)
    @RequestMapping(value = "/{idUser}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable(value = "idUser") long idUser) throws ApiException {
        LOGGER.info("Suppression du user " + idUser);
        userService.deleteUser(idUser, RequestContext.get().getUser().getId());
    }

    /**
     * WS de récupération du user connecté
     * @return
     */
    @Permission(PERMISSION.USER_GET_CONNECTED)
    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public @ResponseBody UserResponse getMyUser() throws ApiException {
        LOGGER.info("Récupération du user connecté " + RequestContext.get().getUser().getId());
        return userService.getUserResponse(RequestContext.get().getUser().getId());
    }

}
