package com.marketpay.services.businessunit;

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
     * Retourne la liste de toutes les BU
     * @return
     */
    public List<BusinessUnit> getBusinessUnitList() {
        return businessUnitRepository.findAll();
    }

}
