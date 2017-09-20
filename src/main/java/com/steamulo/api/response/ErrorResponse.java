package com.steamulo.api.response;

/**
 * Created by etienne on 08/08/17.
 */
public class ErrorResponse {

    private String errorCode;

    public ErrorResponse(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
