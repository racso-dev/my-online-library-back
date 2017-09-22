package com.steamulo.api.response;

/**
 * Response body d'une erreur
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
