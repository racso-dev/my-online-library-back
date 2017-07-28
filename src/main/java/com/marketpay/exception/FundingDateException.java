package com.marketpay.exception;

public class FundingDateException extends Exception {
    private String errorLine;

    public FundingDateException(String message, Throwable cause, String errorLine) {
        super("Erreur lors du parsing de la funding date pour la ligne : " + errorLine + ", message : " + message, cause);
        this.errorLine = errorLine;

    }

    public String getErrorLine() {
        return errorLine;
    }

    public void setErrorLine(String errorLine) {
        this.errorLine = errorLine;
    }
}
