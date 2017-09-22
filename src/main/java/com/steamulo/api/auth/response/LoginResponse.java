package com.steamulo.api.auth.response;

/**
 * Created by etienne on 01/08/17.
 */
public class LoginResponse {

    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
