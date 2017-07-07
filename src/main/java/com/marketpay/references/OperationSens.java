package com.marketpay.references;

public enum OperationSens {
    CREDIT(0, "Crédit"),
    DEBIT(1, "Débit");

    private Integer code;
    private String value;

    OperationSens(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static OperationSens getByCode(int code) {
        for (OperationSens sens : values() ) {
            if(sens.code == code) {
                return sens;
            }
        }
        return null;
    }
}
