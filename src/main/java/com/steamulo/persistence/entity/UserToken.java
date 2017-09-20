package com.steamulo.persistence.entity;

import com.steamulo.persistence.converter.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by etienne on 07/08/17.
 */
@Entity
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String token;
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime expirationDateTime;

    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(LocalDateTime expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

}
