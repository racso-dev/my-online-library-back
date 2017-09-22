package com.steamulo.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ApiException {

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
