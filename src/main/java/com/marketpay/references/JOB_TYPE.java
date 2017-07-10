package com.marketpay.references;

public enum JOB_TYPE {
    CODA("Coda"),
    N43("N43");

    private String code;

    JOB_TYPE(String code) {
        this.code = code;
    }

    public static JOB_TYPE getByValue(String code) {
        for (JOB_TYPE type : values() ) {
            if(type.code == code) {
                return type;
            }
        }
        return null;
    }

    public String getCode() {
        return this.code;
    }
}
