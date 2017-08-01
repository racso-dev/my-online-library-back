package com.marketpay.api.user;

import com.marketpay.annotation.Profile;
import com.marketpay.api.MarketPayController;
import com.marketpay.api.RequestContext;
import com.marketpay.exception.EntityNotFoundException;
import com.marketpay.exception.MarketPayException;
import com.marketpay.services.user.UserService;
import com.marketpay.services.user.resource.UserInformationResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by etienne on 26/07/17.
 */
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

}
