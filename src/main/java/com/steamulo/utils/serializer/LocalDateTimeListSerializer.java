package com.steamulo.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by dle on 20/09/17
 */
public class LocalDateTimeListSerializer extends JsonSerializer<List<LocalDateTime>> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public void serialize(List<LocalDateTime> localDateTimes, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();
        if(localDateTimes != null) {
            for (LocalDateTime localDateTime : localDateTimes) {
                if(localDateTime != null){
                    jsonGenerator.writeString(localDateTime.format(formatter));
                }
            }
        }
        jsonGenerator.writeEndArray();
    }
}
