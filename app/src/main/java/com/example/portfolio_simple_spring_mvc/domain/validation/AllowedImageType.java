package com.example.portfolio_simple_spring_mvc.domain.validation;

import java.util.Arrays;

public enum AllowedImageType {

    PNG("image/png"),
    JPEG("image/jpeg");

    private final String contentType;

    AllowedImageType(String contentType) {
        this.contentType = contentType;
    }

    public static boolean isAllowed(String contentType) {
        return Arrays.stream(values())
                .anyMatch(type ->
                    type.contentType.equalsIgnoreCase(contentType));
    }
}
