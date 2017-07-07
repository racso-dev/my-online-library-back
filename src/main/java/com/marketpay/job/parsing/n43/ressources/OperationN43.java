package com.marketpay.job.parsing.n43.ressources;

import com.marketpay.persistence.entity.Operation;

public class OperationN43 extends Operation {
    private int operationType;

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }
}
