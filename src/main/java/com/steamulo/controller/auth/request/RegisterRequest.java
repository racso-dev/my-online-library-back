package com.steamulo.controller.auth.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@JsonDeserialize(builder = RegisterRequest.RegisterRequestBuilder.class)
public class RegisterRequest {
    private final String login;
    private final String password;
    private final String firstName;
    private final String lastName;

    @JsonPOJOBuilder(withPrefix = StringUtils.EMPTY)
    static class RegisterRequestBuilder {}
}
