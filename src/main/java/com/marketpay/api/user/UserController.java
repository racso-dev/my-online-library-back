package com.marketpay.api.user;

import com.marketpay.annotation.Profile;
import com.marketpay.api.MarketPayController;
import com.marketpay.api.RequestContext;
import com.marketpay.api.user.response.ShopUserListResponse;
import com.marketpay.exception.MarketPayException;
import com.marketpay.references.USER_PROFILE;
import com.marketpay.services.user.UserService;
import com.marketpay.services.user.resource.UserInformationResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

}
