package com.marketpay.references;

public enum OPERATION_SENS {
    CREDIT(0, "Crédit"),
    DEBIT(1, "Débit");

    private Integer code;
    private String value;

    OPERATION_SENS(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static OPERATION_SENS getByCode(int code) {
        for (OPERATION_SENS sens : values() ) {
            if(sens.code == code) {
                return sens;
            }
        }
        return null;
    }

    public Integer getCode() {
        return this.code;
    }
}
