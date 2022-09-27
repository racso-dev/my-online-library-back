package com.steamulo.controller.text.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.steamulo.enums.Page;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@JsonDeserialize(builder = UpdateTextRequest.UpdateTextRequestBuilder.class)
public class UpdateTextRequest {
    private final Page page;

    private final String content;

    @JsonPOJOBuilder(withPrefix = StringUtils.EMPTY)
    static class UpdateTextRequestBuilder {
    }
}
