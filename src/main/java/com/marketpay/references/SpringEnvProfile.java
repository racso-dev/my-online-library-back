package com.marketpay.references;

/**
 * Created by antony on 07/07/17.
 */
public enum SpringEnvProfile {
    DEV("DEV"),
    ANTO("ANTO"),
    ETI("ETI"),
    THOMAS("THOMAS");

    private final String key;

    SpringEnvProfile(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
