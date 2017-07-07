package com.marketpay.references;

public enum JOB_TYPE {
    CODA("Coda"),
    N43("N43");

    private String value;

    JOB_TYPE(String value) {
        this.value = value;
    }

    public static JOB_TYPE getByValue(String value) {
        for (JOB_TYPE type : values() ) {
            if(type.value == value) {
                return type;
            }
        }
        return null;
    }

    public String getValue() {
        return this.value;
    }
}
