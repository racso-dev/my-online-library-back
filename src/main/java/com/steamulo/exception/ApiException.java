package com.steamulo.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends Exception {

    private HttpStatus httpStatus;
    private String errorCode;
    private String fileName;
    private Integer lineNumber;

    public ApiException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        if (Thread.currentThread().getStackTrace()[2] != null) {
            this.fileName = Thread.currentThread().getStackTrace()[2].getFileName();
            this.lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        }
    }

    public ApiException(HttpStatus httpStatus, String message, String errorCode) {
        this(httpStatus, message);
        this.errorCode = errorCode;
    }

    public ApiException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
