package com.marketpay.api.operation.response;

import com.marketpay.persistence.entity.Operation;

import java.util.List;

/**
 * Created by tchekroun on 10/07/2017.
 */
public class OperationListResponse {
    private List<Operation> operationList;

    public List<Operation> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
    }
}
