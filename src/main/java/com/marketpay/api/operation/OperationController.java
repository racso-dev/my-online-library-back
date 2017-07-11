package com.marketpay.api.operation;

import com.marketpay.api.MarketPayController;
import com.marketpay.api.operation.response.OperationListResponse;
import com.marketpay.services.operation.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Created by tchekroun on 10/07/2017.
 */
@RestController
@RequestMapping(value = "/operation")
public class OperationController extends MarketPayController {

    @Autowired
    private OperationService operationService;

    /**
     * Hello world d'exemple
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody
    OperationListResponse getOperationListByDate(@RequestParam(value = "localDate") LocalDate localDate) {
        //return operationService.getOperationFromStoreIdListAndLocalDate(localDate, )
    }
}
