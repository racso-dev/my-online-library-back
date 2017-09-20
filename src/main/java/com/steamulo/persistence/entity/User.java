package com.steamulo.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by antony on 06/07/17.
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private int profile;
    private String login;
    private String password;

    public User(User user) {
        this.id = user.getId();
        this.profile = user.getProfile();
        this.login = user.getLogin();
        this.password = user.getPassword();
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
}
