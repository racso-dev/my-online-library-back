package com.steamulo.services.user.resource;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.steamulo.persistence.entity.User;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserResource {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotNull(message = "profile mandatory")
    private String profile;

    @NotBlank(message = "login mandatory")
    @Size(min = 3, max = 45, message = "invalid login")
    private String login;

    public UserResource(User user) {
        this.id = user.getId();
        this.profile = user.getProfile();
        this.login = user.getLogin();
    }

    public UserResource() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}
