package com.marketpay.api.shop;

import com.marketpay.annotation.Profile;
import com.marketpay.api.MarketPayController;
import com.marketpay.api.RequestContext;
import com.marketpay.api.shop.response.ShopListResponse;
import com.marketpay.exception.MarketPayException;
import com.marketpay.references.USER_PROFILE;
import com.marketpay.services.shop.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by etienne on 07/08/17.
 */
@RestController
@RequestMapping(value = "/shop")
public class ShopController extends MarketPayController {

    private final Logger LOGGER = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private ShopService shopService;

    @Profile({USER_PROFILE.ADMIN_USER,USER_PROFILE.SUPER_USER})
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody ShopListResponse getShopList(@RequestParam(value= "idBu", required = false) Long idBu) throws MarketPayException {
        //On récupère le bon idBu
        Long idBuUsed;
        if(idBu != null && USER_PROFILE.ADMIN_USER.equals(RequestContext.get().getUserProfile())){
            idBuUsed = idBu;
        } else if(USER_PROFILE.SUPER_USER.equals(RequestContext.get().getUserProfile())) {
            idBuUsed = RequestContext.get().getIdBu();
        } else {
            throw new MarketPayException(HttpStatus.UNAUTHORIZED, "Pas idBu utilisable pour le user " + RequestContext.get().getUser().getId());
        }

        LOGGER.info("Récupération des shop pour la BU " + idBuUsed);
        return new ShopListResponse(shopService.getShopListByIdBu(idBuUsed));
    }

}
