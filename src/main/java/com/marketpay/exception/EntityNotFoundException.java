package com.marketpay.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by etienne on 25/07/17.
 */
public class EntityNotFoundException extends MarketPayException {

    private long id;
    private String type;

    public EntityNotFoundException(long id, String type) {
        super(HttpStatus.NOT_FOUND, "Entity " + type + " non trouvé pour l'id " + id);
        this.id = id;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
