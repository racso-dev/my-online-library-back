package com.marketpay.api.auth.response;

import com.marketpay.persistence.entity.User;

/**
 * Created by etienne on 24/07/17.
 */
public class LoginResponse {

    private User user;

    public LoginResponse(User user) {
        this.user = user;
    }

    public LoginResponse() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
