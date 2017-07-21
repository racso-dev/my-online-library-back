package com.marketpay.api.auth;

import com.marketpay.api.MarketPayController;
import com.marketpay.services.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by etienne on 03/07/17.
 */
@RequestMapping(value = "/api/auth")
public class AuthenticationController extends MarketPayController {


    @Autowired
    private AuthenticationService authenticationService;
}
