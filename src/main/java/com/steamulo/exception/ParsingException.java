package com.steamulo.exception;

/**
 * Created by etienne on 25/07/17.
 */
public class ParsingException extends Exception {

    private String filePath;
    private String type;

    public ParsingException(String message, String filePath, String type) {
        super(message + " lors du parsing " + type + " du fichier " + filePath);
        this.filePath = filePath;
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getType() {
        return type;
    }
}
