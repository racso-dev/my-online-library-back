package com.steamulo.api.user.response;

import com.steamulo.persistence.entity.User;

/**
 * Created by etienne on 08/08/17.
 */
public class UserResponse {

    private String login;
    private String profile;

    public UserResponse(String login, String profile) {
        this.login = login;
        this.profile = profile;
    }

    public UserResponse(User user) {
        this.login = user.getLogin();
        this.profile = user.getProfile();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
