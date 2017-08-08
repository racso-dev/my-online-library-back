package com.marketpay.api;

import com.marketpay.api.response.MarketPayErrorResponse;
import com.marketpay.exception.EntityNotFoundException;
import com.marketpay.exception.MarketPayException;
import com.marketpay.persistence.entity.BusinessUnit;
import com.marketpay.persistence.entity.Shop;
import com.marketpay.persistence.repository.BusinessUnitRepository;
import com.marketpay.persistence.repository.ShopRepository;
import com.marketpay.references.USER_PROFILE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private ShopRepository shopRepository;

    /**
     * Permet d'attraper les erreurs du serveur lors des appels WS et de retourner la réponse adéquate
     * @param req
     * @param response
     * @param t
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MarketPayException.class)
    public MarketPayErrorResponse handleException(HttpServletRequest req, HttpServletResponse response, Throwable t) {
        response.setContentType("application/json");

        String errorCode = null;
        //On traite l'exception set on set le status de la response en conséquence
        if (t instanceof MarketPayException) {
            //Si cas d'erreur fonctionnel, mauvaise request, unauthorized, ...
            response.setStatus(((MarketPayException) t).getHttpStatus().value());
            errorCode = ((MarketPayException) t).getErrorCode();
            LOGGER.info(t.getMessage(), t.getCause());
        }

        MarketPayErrorResponse respError = null;
        if(errorCode != null){
            respError = new MarketPayErrorResponse(errorCode);
        }

        return respError;
    }

    /**
     * Méthod qui vérifie les droits d'accès à une BU pour le user connecté
     * @param idBu
     */
    protected void checkAccessBU(long idBu) throws MarketPayException {
        BusinessUnit businessUnit = businessUnitRepository.findOne(idBu).orElseThrow(() ->
            new EntityNotFoundException(idBu, "businessUnit")
        );

        //SuperUser on verifie qu'il est rattaché à la bu
        if(USER_PROFILE.SUPER_USER.equals(RequestContext.get().getUserProfile()) && !RequestContext.get().getIdBu().equals(idBu)){
            throw new MarketPayException(HttpStatus.UNAUTHORIZED, "Acces à la BU " + idBu + " UNAUTHORIZED pour le user " + RequestContext.get().getUser().getId());
        }

        //UserManager, User n'ont pas le droit
        if(USER_PROFILE.USER.equals(RequestContext.get().getUserProfile()) || USER_PROFILE.USER_MANAGER.equals(RequestContext.get().getUserProfile())){
            throw new MarketPayException(HttpStatus.UNAUTHORIZED, "Acces à la BU " + idBu + " UNAUTHORIZED pour le user " + RequestContext.get().getUser().getId());
        }
    }


    /**
     * Méthod qui vérifie les droits d'accès à un Shop pour le user connecté
     * @param idShop
     */
    protected void checkAccessShop(long idShop) throws MarketPayException {
        Shop shop = shopRepository.findOne(idShop).orElseThrow(() ->
                new EntityNotFoundException(idShop, "shop")
        );

        //SuperUser on verifie qu'il est rattaché à la bu du shop
        if(USER_PROFILE.SUPER_USER.equals(RequestContext.get().getUserProfile()) && !RequestContext.get().getIdBu().equals(shop.getIdBu())){
            throw new MarketPayException(HttpStatus.UNAUTHORIZED, "Acces au shop " + idShop + " UNAUTHORIZED pour le user " + RequestContext.get().getUser().getId());
        }

        //UserManager, User n'ont pas le droit
        if((USER_PROFILE.USER.equals(RequestContext.get().getUserProfile()) || USER_PROFILE.USER_MANAGER.equals(RequestContext.get().getUserProfile()))
            && !RequestContext.get().getIdShopList().contains(idShop)){
            throw new MarketPayException(HttpStatus.UNAUTHORIZED, "Acces au shop " + idShop + " UNAUTHORIZED pour le user " + RequestContext.get().getUser().getId());
        }
    }

}
