package com.marketpay.references;

public enum TransactionSens {
    CREDIT(0, "Crédit"),
    DEBIT(1, "Débit");

    private Integer code;
    private String value;

    TransactionSens(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
