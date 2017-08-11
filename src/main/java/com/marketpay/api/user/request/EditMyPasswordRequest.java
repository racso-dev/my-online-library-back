package com.marketpay.api.user.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by etienne on 08/08/17.
 */
public class EditMyPasswordRequest {

    @NotNull(message = "Password mandatory")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9]).*", message = "Invalid password")
    @Size(min = 8, max = 256, message = "Invalid password")
    private String newPassword;

    @NotNull(message = "Password mandatory")
    private String oldPassword;

    public EditMyPasswordRequest() {
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
