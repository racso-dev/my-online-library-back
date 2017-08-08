package com.marketpay.api.response;

/**
 * Created by etienne on 08/08/17.
 */
public class MarketPayErrorResponse {

    private String errorCode;

    public MarketPayErrorResponse(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
