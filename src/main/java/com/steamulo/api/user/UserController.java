package com.steamulo.api.user;

import com.steamulo.annotation.Permission;
import com.steamulo.api.RequestContext;
import com.steamulo.api.response.IdResponse;
import com.steamulo.api.user.request.EditMyPasswordRequest;
import com.steamulo.api.user.request.EditUserRequest;
import com.steamulo.api.user.response.EditMyResponse;
import com.steamulo.api.user.response.UserResponse;
import com.steamulo.exception.ApiException;
import com.steamulo.references.PERMISSION;
import com.steamulo.references.USER_PROFILE;
import com.steamulo.services.user.UserService;
import com.steamulo.services.user.resource.UserInformationResource;
import com.steamulo.services.user.resource.UserResource;
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
        return new UserResponse(userService.getUser(idUser));
    }

    /**
     * WS de création d'un user
     * @param userResource
     * @return
     */
    @Permission(PERMISSION.USER_CREATE)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody IdResponse createUser(@RequestBody @Valid UserResource userResource) throws ApiException {
        long newIdUser = userService.createUser(userResource);
        LOGGER.info("Creation du nouveau user " + newIdUser);
        return new IdResponse(newIdUser);
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
        return new UserResponse(userService.getUser(RequestContext.get().getUser().getId()));
    }

}
