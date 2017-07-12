package com.marketpay.services.operation;

import com.marketpay.persistence.entity.Operation;
import com.marketpay.persistence.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by tchekroun on 10/07/2017.
 */
@Component
public class OperationService {
    @Autowired
    private OperationRepository operationRepository;

    /**
     * Permet de récupérer la liste d'opération effectué dans x magasins à un instant T
     * @param localDate
     * @param storeIdList
     * @return List d'opération
     */
    public List<Operation> getOperationFromStoreIdListAndLocalDate(LocalDate localDate, List<Long> storeIdList) {
        return operationRepository.findOperationsByIdStoreInAndFundingDate(storeIdList, localDate);
    }

}
