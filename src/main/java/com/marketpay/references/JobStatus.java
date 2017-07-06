package com.marketpay.references;

public enum JobStatus {
    IN_PROGRESS(0, "en cours"),
    SUCESS(1, "OK"),
    BLOCK_FAIL(2, "block coda erreur"),
    FAIL(3, "erreur");


    private int code;
    private String value;

    JobStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }
}
