package com.marketpay.api.operation;

import com.marketpay.api.MarketPayController;
import com.marketpay.api.operation.response.OperationListResponse;
import com.marketpay.persistence.entity.User;
import com.marketpay.persistence.repository.ShopRepository;
import com.marketpay.persistence.repository.UserRepository;
import com.marketpay.services.operation.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by tchekroun on 10/07/2017.
 */
@RestController
@RequestMapping(value = "/api/operation")
public class OperationController extends MarketPayController {

    @Autowired
    private OperationService operationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopRepository shopRepository;

    /**
     * Renvoie la liste des opérations associé à un utilisateur à un instant T
     * @param localDate : Date des opérations
     * @return OperationListResponse
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody
    OperationListResponse getOperationListByDate(@RequestParam(value = "localDate") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate localDate, HttpServletResponse response) {
        OperationListResponse operationListResponse = new OperationListResponse();

        //On récupère le user connecté
        Optional<User> userOpt = userRepository.findByLogin((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if(!userOpt.isPresent()){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return operationListResponse;
        }

        List<Long> shopIdList = new ArrayList<>();
        if(userOpt.get().getIdShop() != null){
            shopIdList.add(userOpt.get().getIdShop());
        } else if(userOpt.get().getIdBu() != null) {
            //On récupère les shop de la BU
            shopRepository.findByIdBu(userOpt.get().getIdBu()).forEach(shop -> {
                shopIdList.add(shop.getId());
            });
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return operationListResponse;
        }

        operationListResponse.setOperationList(operationService.getOperationFromShopIdListAndLocalDate(localDate, shopIdList));

        return operationListResponse;
    }
}
