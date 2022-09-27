package com.steamulo.controller.text.response;

import com.steamulo.enums.Page;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TextResponse {
    private final Page page;
    private final String content;
}
