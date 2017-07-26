package com.marketpay.api.user;

import com.marketpay.api.MarketPayController;
import com.marketpay.exception.EntityNotFoundException;
import com.marketpay.persistence.entity.User;
import com.marketpay.persistence.repository.UserRepository;
import com.marketpay.services.user.UserService;
import com.marketpay.services.user.resource.UserInformationResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Created by etienne on 26/07/17.
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserController extends MarketPayController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * WS de récupération d'un userInformation à du user connecté
     * @param response
     * @return
     */
    @RequestMapping(value = "/information", method = RequestMethod.GET)
    public @ResponseBody UserInformationResource getUserInformation(HttpServletResponse response) {
        //On récupère le user connecté
        Optional<User> userOpt = userRepository.findByLogin((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if(!userOpt.isPresent()){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }

        //On récupère le userInformation
        try {
            LOGGER.info("Récupération du userInformation pour le user " + userOpt.get().getId());
            return userService.getUserInformation(userOpt.get());
        } catch (EntityNotFoundException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return null;
    }

}
