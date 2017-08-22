package com.marketpay.api.keypass;

import com.marketpay.annotation.NotAuthenticated;
import com.marketpay.api.MarketPayController;
import com.marketpay.api.RequestContext;
import com.marketpay.api.auth.response.TokenResponse;
import com.marketpay.api.keypass.request.ResetPasswordRequest;
import com.marketpay.api.keypass.request.SendKeyPassRequest;
import com.marketpay.exception.MarketPayException;
import com.marketpay.services.keypass.KeyPassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by etienne on 09/08/17.
 */
@RestController
@RequestMapping(value = "/login/keypass")
public class KeyPassController extends MarketPayController {

    private final Logger LOGGER = LoggerFactory.getLogger(KeyPassController.class);

    @Autowired
    private KeyPassService keyPassService;

    @NotAuthenticated
    @RequestMapping(value = "/{keyPass}", method = RequestMethod.POST)
    public @ResponseBody TokenResponse resetPasswordKeyPass(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest, @PathVariable(value = "keyPass") String keyPass) throws MarketPayException {
        LOGGER.info("Reset du password avec le keyPass " + keyPass);
        return new TokenResponse(keyPassService.resetPasswordKeyPass(keyPass, resetPasswordRequest));
    }


    @NotAuthenticated
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void sendKeyPass(@RequestBody @Valid SendKeyPassRequest sendKeyPassRequest) throws MarketPayException {
        LOGGER.info("Demande de reset de password par mail pour le login " + sendKeyPassRequest.getLogin());
        keyPassService.sendKeyPass(sendKeyPassRequest.getLogin(), false, RequestContext.get().getLanguage());
    }
}
