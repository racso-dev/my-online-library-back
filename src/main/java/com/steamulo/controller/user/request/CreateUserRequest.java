package com.steamulo.controller.user.request;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@JsonDeserialize(builder = CreateUserRequest.CreateUserRequestBuilder.class)
public class CreateUserRequest {
    @NotNull(message = "Login mandatory")
    @Size(min = 3, max = 45, message = "Invalid login")
    private final String login;

    @NotNull(message = "Password mandatory")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9]).*", message = "Invalid password")
    @Size(min = 8, max = 256, message = "Invalid password")
    private final String password;

    @NotNull(message = "Role mandatory")
    private final String role;

    private final String firstName;

    private final String lastName;

    @JsonPOJOBuilder(withPrefix = StringUtils.EMPTY)
    static class CreateUserRequestBuilder {}
}
