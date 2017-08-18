package com.marketpay.references;

public enum CARD_TYPE {
    MAE("MAE", "pdfOperationService.mae"),
    MC( "MC", "pdfOperationService.mc"),
    VIS("VIS", "pdfOperationService.vis"),
    BCM("BCM", "pdfOperationService.bcm");


    private String code;
    private String i18n;

    CARD_TYPE(String code, String i18n) {
        this.code = code;
        this.i18n = i18n;
    }

    public static CARD_TYPE getByCode(String code) {
        for (CARD_TYPE type : values() ) {
            if(type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

    public String getI18n() {
        return i18n;
    }

    public String getCode() {
        return this.code;
    }
}
