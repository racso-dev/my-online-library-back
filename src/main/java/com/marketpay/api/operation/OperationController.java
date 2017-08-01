package com.marketpay.api.operation;

import com.marketpay.annotation.Profile;
import com.marketpay.api.MarketPayController;
import com.marketpay.api.RequestContext;
import com.marketpay.api.operation.response.OperationListResponse;
import com.marketpay.references.USER_PROFILE;
import com.marketpay.services.operation.OperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by tchekroun on 10/07/2017.
 */
@RestController
@RequestMapping(value = "/operation")
public class OperationController extends MarketPayController {

    private final Logger LOGGER = LoggerFactory.getLogger(OperationController.class);

    @Autowired
    private OperationService operationService;

    /**
     * Renvoie la liste des opérations associé à un utilisateur à un instant T
     * @param localDate : Date des opérations
     * @param idShop
     * @return OperationListResponse
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @Profile({USER_PROFILE.SUPER_USER, USER_PROFILE.USER, USER_PROFILE.USER_MANAGER})
    public @ResponseBody
    OperationListResponse getOperationListByDate(@RequestParam(value = "localDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate localDate, @RequestParam(value="idShop", required = false) Long idShop, HttpServletResponse response) {
        OperationListResponse operationListResponse = new OperationListResponse();

        //On récupère la liste des shop associé au user
        List<Long> shopIdList = RequestContext.get().getIdShopList();

        //Si on passe un idShop, on vérifie que le user à le droit d'accès à ce shop
        if( idShop != null) {
            if( !shopIdList.contains(idShop) ) {
                //TODO ETI MarketPayException
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return operationListResponse;
            } else {
                shopIdList.clear();
                shopIdList.add(idShop);
            }
        }

        //Appel au service
        operationListResponse.setOperationList(operationService.getOperationFromShopIdListAndLocalDate(localDate, shopIdList));
        LOGGER.info("Récupération de " + operationListResponse.getOperationList().size() + " pour la fundingDate " + localDate.toString() + " et le user " + RequestContext.get().getUser().getId());

        return operationListResponse;
    }
}
