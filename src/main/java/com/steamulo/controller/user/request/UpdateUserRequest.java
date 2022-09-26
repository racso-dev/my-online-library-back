package com.steamulo.controller.user.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@JsonDeserialize(builder = UpdateUserRequest.UpdateUserRequestBuilder.class)
public class UpdateUserRequest {
    @Size(min = 3, max = 45, message = "Invalid login")
    private final String login;

    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9]).*", message = "Invalid password")
    @Size(min = 8, max = 256, message = "Invalid password")
    private final String password;

    private final String firstName;

    private final String lastName;

    @JsonPOJOBuilder(withPrefix = StringUtils.EMPTY)
    static class UpdateUserRequestBuilder {
    }
}
