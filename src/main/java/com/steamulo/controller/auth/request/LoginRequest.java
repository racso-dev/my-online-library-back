package com.steamulo.controller.auth.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@JsonDeserialize(builder = LoginRequest.LoginRequestBuilder.class)
public class LoginRequest {
    private final String login;
    private final String password;

    @JsonPOJOBuilder(withPrefix = StringUtils.EMPTY)
    static class LoginRequestBuilder {}
}
