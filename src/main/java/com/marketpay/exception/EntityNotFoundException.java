package com.marketpay.exception;

/**
 * Created by etienne on 25/07/17.
 */
public class EntityNotFoundException extends Exception {

    private long id;
    private String type;

    public EntityNotFoundException(long id, String type) {
        super("Entity " + type + " non trouvé pour l'id " + id);
        this.id = id;
        this.type = type;
    }
}
