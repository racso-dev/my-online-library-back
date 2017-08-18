package com.marketpay.references;

public enum OPERATION_TYPE {
    VENTAS(125, "pdfOperationService.ventas"),
    RECLAM( 126, "pdfOperationService.reclamacion"),
    BOLETA(127, "pdfOperationService.cargo");

    private int code;
    private String i18n;

    OPERATION_TYPE(int code, String i18n) {
        this.code = code;
        this.i18n = i18n;
    }

    public static OPERATION_TYPE getByCode(int code) {
        for (OPERATION_TYPE type : values() ) {
            if(type.code == code) {
                return type;
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
