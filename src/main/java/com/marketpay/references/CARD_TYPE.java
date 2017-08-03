package com.marketpay.references;

public enum CARD_TYPE {
    MAE("MAE", "Maestro"),
    MC( "MC ", "Master Card"),
    VIS("VIS", "Visa"),
    BCM("BCM", "Bancontact");


    private String code;
    private String value;

    CARD_TYPE(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static CARD_TYPE getByCode(String code) {
        for (CARD_TYPE type : values() ) {
            if(type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return this.code;
    }
}
