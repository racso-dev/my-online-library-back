package com.marketpay.api.operation.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketpay.utils.serializer.LocalDateSerializer;

import java.time.LocalDate;

/**
 * Created by etienne on 07/08/17.
 */
public class LastFundingDateResponse {

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate lastFundingDate;

    public LastFundingDateResponse() {
    }

    public LastFundingDateResponse(LocalDate lastFundingDate) {
        this.lastFundingDate = lastFundingDate;
    }

    public LocalDate getLastFundingDate() {
        return lastFundingDate;
    }

    public void setLastFundingDate(LocalDate lastFundingDate) {
        this.lastFundingDate = lastFundingDate;
    }
}
