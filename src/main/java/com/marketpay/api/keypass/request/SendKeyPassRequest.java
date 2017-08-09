package com.marketpay.api.keypass.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Created by etienne on 09/08/17.
 */
public class SendKeyPassRequest {

    @NotBlank(message = "Email mandatory")
    @Size(min = 5, max = 100, message = "invalid email")
    private String email;

    public SendKeyPassRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
