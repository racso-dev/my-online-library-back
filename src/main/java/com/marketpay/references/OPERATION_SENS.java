package com.marketpay.references;

public enum OPERATION_SENS {
    CREDIT(0, "pdfOperationService.credit"),
    DEBIT(1, "pdfOperationService.debit");

    private int code;
    private String value;

    OPERATION_SENS(int code, String value) {
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

    public String getValue() {
        return value;
    }

    public int getCode() {
        return this.code;
    }
}
