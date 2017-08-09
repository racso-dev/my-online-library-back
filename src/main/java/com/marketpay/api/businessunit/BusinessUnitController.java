package com.marketpay.api.businessunit;

import com.marketpay.annotation.Profile;
import com.marketpay.api.MarketPayController;
import com.marketpay.api.RequestContext;
import com.marketpay.api.businessunit.response.BusinessUnitUserListResponse;
import com.marketpay.references.USER_PROFILE;
import com.marketpay.services.businessunit.BusinessUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/businessunit")
public class BusinessUnitController extends MarketPayController {

    private final Logger LOGGER = LoggerFactory.getLogger(BusinessUnitController.class);

    @Autowired
    private BusinessUnitService businessUnitService;

    /**
     * WS de récupération de toutes les BU pour l'administrateur
     * @return
     */
    @Profile({USER_PROFILE.ADMIN_USER})
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody BusinessUnitUserListResponse getBusinessUnitList() {

        LOGGER.info("Récupération de la liste des BU pour l'administrateur " + RequestContext.get().getUser().getId());
        return new BusinessUnitUserListResponse(businessUnitService.getBusinessUnitList());
    }

}
