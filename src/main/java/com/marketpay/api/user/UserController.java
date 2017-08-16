package com.marketpay.api.user;

import com.marketpay.annotation.Profile;
import com.marketpay.api.MarketPayController;
import com.marketpay.api.RequestContext;
import com.marketpay.api.response.IdResponse;
import com.marketpay.api.user.request.EditMyPasswordRequest;
import com.marketpay.api.user.request.EditUserRequest;
import com.marketpay.api.user.response.EditMyResponse;
import com.marketpay.api.user.response.ShopUserListResponse;
import com.marketpay.api.user.response.UserResponse;
import com.marketpay.exception.MarketPayException;
import com.marketpay.references.USER_PROFILE;
import com.marketpay.services.user.UserService;
import com.marketpay.services.user.resource.UserInformationResource;
import com.marketpay.services.user.resource.UserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/user")
public class UserController extends MarketPayController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * WS de récupération d'un userInformation à du user connecté
     * @return
     */
    @Profile({})
    @RequestMapping(value = "/information", method = RequestMethod.GET)
    public @ResponseBody UserInformationResource getUserInformation() throws MarketPayException {
        //On récupère le userInformation
        LOGGER.info("Récupération du userInformation pour le user " + RequestContext.get().getUser().getId());
        return userService.getUserInformation(RequestContext.get().getUser());
    }

    /**
     * WS de récupération des shop user sur lesquels le user connecté à les droits
     * @param idBu (si l'utilisateur est un admin user)
     * @return
     */
    @Profile({USER_PROFILE.ADMIN_USER, USER_PROFILE.USER_MANAGER})
    @RequestMapping(value = "/shop", method = RequestMethod.GET)
    public @ResponseBody ShopUserListResponse getShopUserList(@RequestParam(value = "idBu", required = false) Long idBu) throws MarketPayException {

        Long idBuForRequest = null;
        Long idShopForRequest = null;
        // MarketPayException
        if (RequestContext.get().getUserProfile().equals(USER_PROFILE.ADMIN_USER)) {
            //Si admin
            if (idBu != null) {
                idBuForRequest = idBu;
            } else {
                throw new MarketPayException(HttpStatus.BAD_REQUEST, "L'idBu est obligatoire lorsque c'est le user admin qui fait la request");
            }
        } else {
            //Sinon userManager
            if(RequestContext.get().getIdShopList().size() == 1){
                idShopForRequest = RequestContext.get().getIdShopList().get(0);
            } else {
                throw new MarketPayException(HttpStatus.INTERNAL_SERVER_ERROR, "Aucun ou plusieurs idShop pour le userManager " + RequestContext.get().getUser().getId());
            }
        }

        LOGGER.info("Récupération de la liste de shop associé avec ces utilisateurs pour la BU " + RequestContext.get().getIdBu());
        return new ShopUserListResponse(userService.getShopUserList(idBuForRequest, idShopForRequest));
    }

    /**
     * WS de récupération d'un user
     * @param idUser
     * @return
     */
    @Profile({USER_PROFILE.ADMIN_USER, USER_PROFILE.USER_MANAGER})
    @RequestMapping(value = "/{idUser}", method = RequestMethod.GET)
    public @ResponseBody UserResponse getUser(@PathVariable(value = "idUser") long idUser) throws MarketPayException {
        //On vérifie les droits d'accès au user
        checkAccessUser(idUser);

        LOGGER.info("Récupération du user " + idUser);
        return new UserResponse(userService.getUser(idUser));
    }

    /**
     * WS de création d'un user
     * @param userResource
     * @return
     */
    @Profile({USER_PROFILE.ADMIN_USER, USER_PROFILE.USER_MANAGER})
    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody IdResponse createUser(@RequestBody @Valid UserResource userResource) throws MarketPayException {
        long newIdUser = userService.createUser(RequestContext.get().getUserProfile(), userResource, RequestContext.get().getLanguage());
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
    public @ResponseBody UserResponse editUser(@RequestBody @Valid EditUserRequest editUserRequest, @PathVariable(value = "idUser") long idUser) throws MarketPayException {
        //On vérifie les droits d'accès au user
        checkAccessUser(idUser);

        LOGGER.info("Modification du user " + idUser);
        return new UserResponse(userService.editUser(idUser, editUserRequest));
    }

    /**
     * WS de suppression d'un user
     * @param idUser
     */
    @Profile({USER_PROFILE.ADMIN_USER, USER_PROFILE.USER_MANAGER})
    @RequestMapping(value = "/{idUser}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable(value = "idUser") long idUser) throws MarketPayException {
        //On vérifie les droits d'accès au user
        checkAccessUser(idUser);

        LOGGER.info("Suppression du user " + idUser);
        userService.deleteUser(idUser, RequestContext.get().getUser().getId());
    }

    /**
     * WS de récupération du user connecté
     * @return
     */
    @Profile({})
    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public @ResponseBody UserResponse getMyUser() throws MarketPayException {
        LOGGER.info("Récupération du user connecté " + RequestContext.get().getUser().getId());
        return new UserResponse(userService.getUser(RequestContext.get().getUser().getId()));
    }

    /**
     * WS d'edition du user connecté par lui même
     * @return
     */
    @Profile({})
    @RequestMapping(value = "/my", method = RequestMethod.POST)
    public @ResponseBody EditMyResponse editMyUser(@RequestBody @Valid EditUserRequest editUserRequest) throws MarketPayException {
        LOGGER.info("Modification du user connecté " + RequestContext.get().getUser().getId());
        return userService.editMyUser(RequestContext.get().getUser().getId(), editUserRequest);
    }

    /**
     * WS d'edition du user connecté par lui même
     * @return
     */
    @Profile({})
    @RequestMapping(value = "/passwd", method = RequestMethod.POST)
    public void editMyPassword(@RequestBody @Valid EditMyPasswordRequest editMyPasswordRequest) throws MarketPayException {
        LOGGER.info("Modification du password du user connecté " + RequestContext.get().getUser().getId());
        userService.editMyPassword(RequestContext.get().getUser().getId(), editMyPasswordRequest);
    }

}
