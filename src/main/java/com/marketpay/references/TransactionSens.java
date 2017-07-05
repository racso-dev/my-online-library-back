package com.marketpay.references;

public enum TransactionSens {
    CREDIT("Crédit"),
    DEBIT("Débit");

    private String value;

    TransactionSens(String value) {
        this.value = value;
    }
}
