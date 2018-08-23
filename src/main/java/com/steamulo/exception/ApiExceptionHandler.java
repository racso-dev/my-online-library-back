package com.steamulo.exception;

import com.steamulo.api.response.ErrorResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ApiExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     * Permet d'attraper les erreurs du serveur lors des appels WS et de retourner la réponse adéquate
     * @param req
     * @param response
     * @param t
     * @return
     */
    @ResponseBody
    @ExceptionHandler(com.steamulo.exception.ApiException.class)
    public ErrorResponse handleException(HttpServletRequest req, HttpServletResponse response, Throwable t) {
        response.setContentType("application/json");

        String errorCode = null;
        //On traite l'exception set on set le status de la response en conséquence
        if (t instanceof com.steamulo.exception.ApiException) {
            //Si cas d'erreur fonctionnel, mauvaise request, unauthorized, ...
            response.setStatus(((com.steamulo.exception.ApiException) t).getHttpStatus().value());
            errorCode = ((com.steamulo.exception.ApiException) t).getErrorCode();
            String message = t.getMessage() + "(";

            if (StringUtils.isNotBlank(((com.steamulo.exception.ApiException) t).getFileName())) {
                message += ((com.steamulo.exception.ApiException) t).getFileName();
            }
            if (((com.steamulo.exception.ApiException) t).getLineNumber() != null) {
                message += ":" + ((com.steamulo.exception.ApiException) t).getLineNumber();
            }
            message += ")";
            LOGGER.info(message, t.getCause());
        }

        ErrorResponse respError = null;
        if(errorCode != null){
            respError = new ErrorResponse(errorCode);
        }

        return respError;
    }

}
