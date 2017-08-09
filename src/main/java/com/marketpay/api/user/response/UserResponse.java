package com.marketpay.api.user.response;

import com.marketpay.services.user.resource.UserResource;

/**
 * Created by etienne on 08/08/17.
 */
public class UserResponse {

    private UserResource user;

    public UserResponse(UserResource user) {
        this.user = user;
    }

    public UserResource getUser() {
        return user;
    }

    public void setUser(UserResource user) {
        this.user = user;
    }
}
