package com.marketpay.job.parsing.n43.ressources;

import com.marketpay.references.Transaction;

public class TransactionN43 extends Transaction {
    private int operation_type;

    public int getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(int operation_type) {
        this.operation_type = operation_type;
    }
}
