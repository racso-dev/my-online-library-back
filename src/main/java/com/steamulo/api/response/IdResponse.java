package com.steamulo.api.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Created by etienne on 08/08/17.
 */
public class IdResponse {

    @JsonSerialize(using = ToStringSerializer.class)
    private long id;

    public IdResponse(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
