package com.marketpay.services.user.resource;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.marketpay.persistence.entity.User;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserResource {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotNull(message = "profile mandatory")
    @Max(value = 4, message = "unknow profile")
    @Min(value = 1, message = "unknow profile")
    private Integer profile;

    @NotBlank(message = "email mandatory")
    @Size(min = 5, max = 100, message = "invalid email")
    private String email;

    @NotBlank(message = "lastName mandatory")
    @Size(min = 3, max = 45, message = "invalid lastName")
    private String lastName;

    @NotBlank(message = "firstName mandatory")
    @Size(min = 3, max = 45, message = "invalid firstName")
    private String firstName;

    @NotBlank(message = "login mandatory")
    @Size(min = 3, max = 45, message = "invalid login")
    private String login;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long idBu;

    private String nameBu;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long idShop;

    private String nameShop;

    public UserResource(User user) {
        this.id = user.getId();
        this.profile = user.getProfile();
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
        this.idBu = user.getIdBu();
        this.idShop = user.getIdShop();
    }

    public UserResource() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProfile() {
        return profile;
    }

    public void setProfile(Integer profile) {
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

    public String getNameBu() {
        return nameBu;
    }

    public void setNameBu(String nameBu) {
        this.nameBu = nameBu;
    }

    public Long getIdShop() {
        return idShop;
    }

    public void setIdShop(Long idShop) {
        this.idShop = idShop;
    }

    public String getNameShop() {
        return nameShop;
    }

    public void setNameShop(String nameShop) {
        this.nameShop = nameShop;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}
