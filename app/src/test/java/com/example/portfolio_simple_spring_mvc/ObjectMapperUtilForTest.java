package com.example.portfolio_simple_spring_mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class ObjectMapperUtilForTest {
    private static final ObjectMapper objectMapper =
        new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    private ObjectMapperUtilForTest() {}

    public static String of(Object value) throws JsonProcessingException {
        String json = objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(value);

        return json;
    }
}
