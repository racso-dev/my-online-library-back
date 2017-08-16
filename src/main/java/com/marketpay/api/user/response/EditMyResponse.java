package com.marketpay.api.user.response;

import com.marketpay.services.user.resource.UserResource;

/**
 * Created by etienne on 16/08/17.
 */
public class EditMyResponse {

    private UserResource user;

    private String token;

    public EditMyResponse() {
    }

    public UserResource getUser() {
        return user;
    }

    public void setUser(UserResource user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
