package com.marketpay.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by etienne on 01/08/17.
 */
public class MarketPayException extends Exception {

    private HttpStatus httpStatus;
    private String errorCode;

    public MarketPayException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public MarketPayException(HttpStatus httpStatus, String message, String errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public MarketPayException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
