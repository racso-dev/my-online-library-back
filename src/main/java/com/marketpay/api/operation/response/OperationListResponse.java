package com.marketpay.api.operation.response;

import com.marketpay.persistence.entity.Operation;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by tchekroun on 10/07/2017.
 */
public class OperationListResponse {
    private List<Operation> operationList;
    private List<LocalDate> financementDateList;

    public List<LocalDate> getFinancementDateList() {
        return financementDateList;
    }

    public void setFinancementDateList(List<LocalDate> financementDateList) {
        this.financementDateList = financementDateList;
    }

    public List<Operation> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
    }
}
