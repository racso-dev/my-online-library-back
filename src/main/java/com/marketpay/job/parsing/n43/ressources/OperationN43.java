package com.marketpay.job.parsing.n43.ressources;

import com.marketpay.persistence.entity.Operation;

public class OperationN43 extends Operation {
    private int operationType;


    public Operation toOperation() {
        Operation operation = new Operation();
        operation.setFundingDate(this.getFundingDate());
        operation.setTradeDate(this.getTradeDate());
        operation.setCardType(this.getCardType());
        operation.setSens(this.getSens());
        operation.setGrossAmount(this.getGrossAmount());
        operation.setNetAmount(this.getNetAmount());
        operation.setContractNumber(this.getContractNumber());
        operation.setNameShop(this.getNameShop());
        operation.setIdShop(this.getIdShop());
        return operation;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }
}
