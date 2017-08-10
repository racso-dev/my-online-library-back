package com.marketpay.api.user.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Created by etienne on 08/08/17.
 */
public class EditUserRequest {

    @NotBlank(message = "email mandatory")
    @Size(min = 5, max = 100, message = "invalid email")
    private String email;

    @NotBlank(message = "lastName mandatory")
    @Size(min = 1, max = 45, message = "invalid lastName")
    private String lastName;

    @NotBlank(message = "firstName mandatory")
    @Size(min = 1, max = 45, message = "invalid firstName")
    private String firstName;

    @NotBlank(message = "login mandatory")
    @Size(min = 3, max = 45, message = "invalid login")
    private String login;

    public EditUserRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
