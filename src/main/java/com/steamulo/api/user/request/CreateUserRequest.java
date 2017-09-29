package com.steamulo.api.user.request;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Request body de l'appel /user/create
 */
public class CreateUserRequest {

    @NotNull(message = "Login mandatory")
    @Size(min = 3, max = 45, message = "Invalid login")
    private String login;

    @NotNull(message = "Password mandatory")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9]).*", message = "Invalid password")
    @Size(min = 8, max = 256, message = "Invalid password")
    private String password;

    @NotNull(message = "Role mandatory")
    private String role;

    public CreateUserRequest() {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
