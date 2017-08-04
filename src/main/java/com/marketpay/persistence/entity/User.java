package com.marketpay.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private int profile;
    private String email;
    private String login;
    private String password;
    private Long idBu;
    private Long idShop;
    private String lastName;
    private String firstName;

    public User(User user) {
        this.id = user.getId();
        this.profile = user.getProfile();
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.idBu = user.getIdBu();
        this.idShop = user.getIdShop();
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
    }

    public User() {
    }

    public Long getId() {
        return id;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
