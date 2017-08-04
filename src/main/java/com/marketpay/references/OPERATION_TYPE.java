package com.marketpay.references;

public enum OPERATION_TYPE {
    VENTAS(125, "pdfOperationService.ventas"),
    RECLAM( 126, "pdfOperationService.reclamacion"),
    BOLETA(127, "pdfOperationService.cargo");

    private int code;
    private String value;

    OPERATION_TYPE(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static OPERATION_TYPE getByCode(int code) {
        for (OPERATION_TYPE type : values() ) {
            if(type.code == code) {
                return type;
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
