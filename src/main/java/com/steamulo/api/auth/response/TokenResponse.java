package com.steamulo.api.auth.response;

/**
 * Created by etienne on 01/08/17.
 */
public class TokenResponse {

    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
