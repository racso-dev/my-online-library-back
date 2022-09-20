package com.steamulo.controller.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private final String login;
    private final String role;
    private final String firstName;
    private final String lastName;
}
