package com.marketpay.api.businessunit.response;

import com.marketpay.persistence.entity.BusinessUnit;

public class BusinessUnitResponse {

    private BusinessUnit businessUnit;

    public BusinessUnitResponse(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

}
