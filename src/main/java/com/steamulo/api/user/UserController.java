package com.steamulo.api.user;

import com.steamulo.annotation.Profile;
import com.steamulo.api.ApiController;
import com.steamulo.api.RequestContext;
import com.steamulo.api.response.IdResponse;
import com.steamulo.api.user.request.EditMyPasswordRequest;
import com.steamulo.api.user.request.EditUserRequest;
import com.steamulo.api.user.response.EditMyResponse;
import com.steamulo.api.user.response.UserResponse;
import com.steamulo.exception.ApiException;
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
public class UserController extends ApiController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * WS de récupération d'un userInformation à du user connecté
     * @return
     */
    @Profile({})
    @RequestMapping(value = "/information", method = RequestMethod.GET)
    public @ResponseBody UserInformationResource getUserInformation() throws ApiException {
        //On récupère le userInformation
        LOGGER.info("Récupération du userInformation pour le user " + RequestContext.get().getUser().getId());
        return userService.getUserInformation(RequestContext.get().getUser());
    }

    /**
     * WS de récupération d'un user
     * @param idUser
     * @return
     */
    @Profile({USER_PROFILE.ADMIN_USER, USER_PROFILE.USER_MANAGER})
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
    @Profile({})
    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody IdResponse createUser(@RequestBody @Valid UserResource userResource) throws ApiException {
        long newIdUser = userService.createUser(userResource);
        LOGGER.info("Creation du nouveau user " + newIdUser);
        return new IdResponse(newIdUser);
    }


    /**
     * WS d'edition d'un user différent de celui connecté
     * @param editUserRequest
     * @return
     */
    @Profile({USER_PROFILE.ADMIN_USER, USER_PROFILE.USER_MANAGER})
    @RequestMapping(value = "/{idUser}", method = RequestMethod.POST)
    public @ResponseBody UserResponse editUser(@RequestBody @Valid EditUserRequest editUserRequest, @PathVariable(value = "idUser") long idUser) throws ApiException {
        LOGGER.info("Modification du user " + idUser);
        return new UserResponse(userService.editUser(idUser, editUserRequest));
    }

    /**
     * WS de suppression d'un user
     * @param idUser
     */
    @Profile({USER_PROFILE.ADMIN_USER, USER_PROFILE.USER_MANAGER})
    @RequestMapping(value = "/{idUser}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable(value = "idUser") long idUser) throws ApiException {
        LOGGER.info("Suppression du user " + idUser);
        userService.deleteUser(idUser, RequestContext.get().getUser().getId());
    }

    /**
     * WS de récupération du user connecté
     * @return
     */
    @Profile({})
    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public @ResponseBody UserResponse getMyUser() throws ApiException {
        LOGGER.info("Récupération du user connecté " + RequestContext.get().getUser().getId());
        return new UserResponse(userService.getUser(RequestContext.get().getUser().getId()));
    }

    /**
     * WS d'edition du user connecté par lui même
     * @return
     */
    @Profile({})
    @RequestMapping(value = "/my", method = RequestMethod.POST)
    public @ResponseBody EditMyResponse editMyUser(@RequestBody @Valid EditUserRequest editUserRequest) throws ApiException {
        LOGGER.info("Modification du user connecté " + RequestContext.get().getUser().getId());
        return userService.editMyUser(RequestContext.get().getUser().getId(), editUserRequest);
    }

    /**
     * WS d'edition du user connecté par lui même
     * @return
     */
    @Profile({})
    @RequestMapping(value = "/passwd", method = RequestMethod.POST)
    public void editMyPassword(@RequestBody @Valid EditMyPasswordRequest editMyPasswordRequest) throws ApiException {
        LOGGER.info("Modification du password du user connecté " + RequestContext.get().getUser().getId());
        userService.editMyPassword(RequestContext.get().getUser().getId(), editMyPasswordRequest);
    }

}
