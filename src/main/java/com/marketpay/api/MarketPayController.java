package com.marketpay.api;

import com.marketpay.exception.MarketPayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by etienne on 03/07/17.
 */
@RestController
public class MarketPayController {

    private final Logger LOGGER = LoggerFactory.getLogger(MarketPayController.class);

    /**
     * Permet d'attraper les erreurs du serveur lors des appels WS et de retourner la réponse adéquate
     * @param req
     * @param response
     * @param t
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MarketPayException.class)
    public String handleException(HttpServletRequest req, HttpServletResponse response, Throwable t) {
        response.setContentType("application/json");

        // Statut http
        HttpStatus status;
        if (t instanceof MarketPayException) {
            //Si cas d'erreur fonctionnel, mauvaise request, unauthorized, ...
            response.setStatus(((MarketPayException) t).getHttpStatus().value());
            LOGGER.info(t.getMessage(), t.getCause());
        }

        return null;
    }

}
