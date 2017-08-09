package com.marketpay.api.keypass.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Created by etienne on 09/08/17.
 */
public class ResetPasswordRequest {

    @NotBlank(message = "Login mandatory")
    private String login;

    @NotBlank(message = "Password mandatory")
    //TODO ETI regex password
    @Size(min = 8, max = 256, message = "Invalid password")
    private String password;

    public ResetPasswordRequest() {
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
