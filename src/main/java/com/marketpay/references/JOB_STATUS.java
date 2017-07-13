package com.marketpay.references;

public enum JOB_STATUS {
    IN_PROGRESS(0, "en cours"),
    SUCESS(1, "OK"),
    BLOCK_FAIL(2, "block coda erreur"),
    FAIL(3, "erreur"),
    MISSING_MATCHING_BU(4, "Le code client ne correspond pas à une BU en base"),
    MISSING_MATCHING_SHOP(5, "Impossible d'associer le contract number à un shop");


    private int code;
    private String value;

    JOB_STATUS(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return this.code;
    }
}
