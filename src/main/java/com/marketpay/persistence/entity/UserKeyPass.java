package com.marketpay.persistence.entity;

import com.marketpay.persistence.converter.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by etienne on 09/08/17.
 */
@Entity
public class UserKeyPass {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String keyPass;
    private Long idUser;
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime expirationDateTime;

    public UserKeyPass() {
    }

    public Long getId() {
        return id;
    }

    public String getKeyPass() {
        return keyPass;
    }

    public void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public LocalDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(LocalDateTime expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }
}
