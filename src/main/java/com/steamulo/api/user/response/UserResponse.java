package com.steamulo.api.user.response;

import com.steamulo.persistence.entity.User;

/**
 * Response body de l'appel /user/create
 */
public class UserResponse {

    private String login;
    private String role;

    public UserResponse(String login, String role) {
        this.login = login;
        this.role = role;
    }

    public UserResponse(User user) {
        this.login = user.getLogin();
        this.role = user.getRole();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
