package com.marketpay.services.businessunit;

import com.marketpay.exception.EntityNotFoundException;
import com.marketpay.exception.MarketPayException;
import com.marketpay.persistence.entity.BusinessUnit;
import com.marketpay.persistence.repository.BusinessUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BusinessUnitService {

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    /**
     * Retourne une bu
     * @return
     */
    public BusinessUnit getBusinessUnit(long idBu) throws MarketPayException {
        return businessUnitRepository.findOne(idBu).orElseThrow(() ->
            new EntityNotFoundException(idBu, "business_unit")
        );
    }

    /**
     * Retourne la liste de toutes les BU
     * @return
     */
    public List<BusinessUnit> getBusinessUnitList() {
        return businessUnitRepository.findAll();
    }

}
