package com.example.portfolio_simple_spring_mvc.application.dto.validation;

public enum ValidationErrorMessage {
    NOT_NULL("not_null"),
    NOT_BLANK("not_blank"),
    ONLY_JAPANESE("only_japanese"),
    ONLY_KATAKANA("only_katakana"),
    SIZE10("size10"),
    SIZE200("size200"),
    ADULT("adult");
    
    private String key;

    ValidationErrorMessage(
        String key
    ) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}