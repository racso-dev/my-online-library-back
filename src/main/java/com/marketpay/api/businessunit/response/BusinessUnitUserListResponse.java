package com.marketpay.api.businessunit.response;

import com.marketpay.persistence.entity.BusinessUnit;

import java.util.List;

public class BusinessUnitUserListResponse {

    private List<BusinessUnit> businessUnitList;

    public BusinessUnitUserListResponse() {
    }

    public BusinessUnitUserListResponse(List<BusinessUnit> businessUnitList) {
        this.businessUnitList = businessUnitList;
    }

    public List<BusinessUnit> getBusinessUnitList() {
        return businessUnitList;
    }

    public void setBusinessUnitList(List<BusinessUnit> businessUnitList) {
        this.businessUnitList = businessUnitList;
    }

}
