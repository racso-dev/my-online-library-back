package com.marketpay.api.operation;

import com.marketpay.api.MarketPayController;
import com.marketpay.api.operation.response.OperationListResponse;
import com.marketpay.services.operation.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tchekroun on 10/07/2017.
 */
@RestController
@RequestMapping(value = "/operation")
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
    OperationListResponse getOperationListByDate(@RequestParam(value = "localDate") LocalDate localDate) {
        // TODO: Récupérrer la liste de store associé à l'utilisateur connecté via la session
        OperationListResponse response = new OperationListResponse();

        List<Long> storeIdList = new ArrayList<>();
        response.setOperationList(operationService.getOperationFromStoreIdListAndLocalDate(localDate, storeIdList));

        return response;
    }
}
