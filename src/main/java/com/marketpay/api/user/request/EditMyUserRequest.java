package com.marketpay.api.user.request;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by etienne on 08/08/17.
 */
public class EditMyUserRequest extends EditUserRequest {

    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9]).*", message = "Invalid password")
    @Size(min = 8, max = 256, message = "Invalid password")
    private String password;

    public EditMyUserRequest() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
