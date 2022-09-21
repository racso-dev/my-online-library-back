package com.steamulo.controller.auth.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private final String token;
}
