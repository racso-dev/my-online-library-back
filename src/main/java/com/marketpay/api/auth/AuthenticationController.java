package com.marketpay.api.auth;

import com.marketpay.api.MarketPayController;
import com.marketpay.api.auth.request.LoginRequest;
import com.marketpay.api.auth.response.LoginResponse;
import com.marketpay.persistence.entity.User;
import com.marketpay.services.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

/**
 * Created by etienne on 03/07/17.
 */
@RequestMapping(value = "/api/auth")
public class AuthenticationController extends MarketPayController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * WS de login avec login et password
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody LoginResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {

        Optional<User> userOpt = authenticationService.login(request.getLogin(), request.getPassword());

        LoginResponse loginResponse = new LoginResponse();

        if (userOpt.isPresent()) {
            //OK
            loginResponse.setUser(userOpt.get());
            response.setStatus(HttpStatus.OK.value());
        } else {
            //KO
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        return loginResponse;
    }
}
