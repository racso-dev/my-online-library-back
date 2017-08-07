package com.marketpay.services.businessunit;

import com.marketpay.persistence.entity.BusinessUnit;
import com.marketpay.persistence.repository.BusinessUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BusinessUnitService {

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    /**
     * Retourne une BU
     * @param idBu
     * @return
     */
    public BusinessUnit getBusinessUnit(long idBu) {
        return businessUnitRepository.findById(idBu);
    }

    /**
     * Retourne la liste de toutes les BU
     * @return
     */
    public List<BusinessUnit> getBusinessUnitList() {
        return businessUnitRepository.findAll();
    }

}
