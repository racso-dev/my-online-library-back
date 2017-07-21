package com.marketpay.api.auth;

import com.marketpay.api.MarketPayController;
import com.marketpay.api.operation.response.OperationListResponse;
import com.marketpay.persistence.entity.User;
import com.marketpay.services.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by etienne on 03/07/17.
 */
@RequestMapping(value = "/api/auth")
public class AuthenticationController extends MarketPayController {


    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody void connect(String login, String password) {
        Optional<User> userOptional = authenticationService.findUserFromConnectInformation(login, password);
        if (userOptional.isPresent()) {
           // TODO : Return success avec l'utilisateur
        }

        // TODO: Return failure
    }
}
