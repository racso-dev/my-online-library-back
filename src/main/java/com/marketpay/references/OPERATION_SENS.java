package com.marketpay.references;

public enum OPERATION_SENS {
    CREDIT(0, "pdfOperationService.credit"),
    DEBIT(1, "pdfOperationService.debit");

    private int code;
    private String i18n;

    OPERATION_SENS(int code, String i18n) {
        this.code = code;
        this.i18n = i18n;
    }

    public static OPERATION_SENS getByCode(int code) {
        for (OPERATION_SENS sens : values() ) {
            if(sens.code == code) {
                return sens;
            }
        }
        return null;
    }

    public String getI18n() {
        return i18n;
    }

    public int getCode() {
        return this.code;
    }
}
