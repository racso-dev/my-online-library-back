package com.marketpay.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LocalDateListSerializer extends JsonSerializer<List<LocalDate>> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public void serialize(List<LocalDate> localDates, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        jsonGenerator.writeStartArray();
        if(localDates != null) {
            for (LocalDate localDate : localDates) {
                if(localDate != null){
                    jsonGenerator.writeString(localDate.format(formatter));
                }
            }
        }
        jsonGenerator.writeEndArray();

    }
}
