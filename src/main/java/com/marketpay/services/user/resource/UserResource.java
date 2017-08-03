package com.marketpay.services.user.resource;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.marketpay.persistence.entity.User;

public class UserResource {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private int profile;
    private String email;
    private String lastName;
    private String firstName;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long idBu;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long idShop;

    public UserResource(User user) {
        this.id = user.getId();
        this.profile = user.getProfile();
        this.email = user.getEmail();
        this.idBu = user.getIdBu();
        this.idShop = user.getIdShop();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getIdBu() {
        return idBu;
    }

    public void setIdBu(Long idBu) {
        this.idBu = idBu;
    }

    public Long getIdShop() {
        return idShop;
    }

    public void setIdShop(Long idShop) {
        this.idShop = idShop;
    }

}
