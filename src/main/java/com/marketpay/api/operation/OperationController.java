package com.marketpay.api.operation;

import com.marketpay.api.MarketPayController;
import com.marketpay.api.operation.response.OperationListResponse;
import com.marketpay.services.operation.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tchekroun on 10/07/2017.
 */
@RestController
@RequestMapping(value = "/api/operation")
public class OperationController extends MarketPayController {

    @Autowired
    private OperationService operationService;

    /**
     * Renvoie la liste des opérations associé à un utilisateur à un instant T
     * @param localDate : Date des opérations
     * @return OperationListResponse
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody
    OperationListResponse getOperationListByDate(@RequestParam(value = "localDate") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate localDate) {
        OperationListResponse response = new OperationListResponse();

        // TODO: Récupérrer la liste de shop associé à l'utilisateur connecté via la session
        List<Long> shopIdList = new ArrayList<>();
        shopIdList.add(2l);
        shopIdList.add(1l);
        response.setOperationList(operationService.getOperationFromShopIdListAndLocalDate(localDate, shopIdList));

        return response;
    }
}
