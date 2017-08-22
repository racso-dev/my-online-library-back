package com.marketpay.api.keypass.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Created by etienne on 09/08/17.
 */
public class SendKeyPassRequest {

    @NotBlank(message = "login mandatory")
    @Size(min = 3, max = 45, message = "invalid login")
    private String login;

    public SendKeyPassRequest() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
