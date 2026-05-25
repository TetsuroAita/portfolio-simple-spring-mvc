package com.example.portfolio_simple_spring_mvc.presentation.advice;

public enum AdviceMessage {
    INTERNAL_SERVER_ERROR_500("internal_server_error_500"),
    NOT_FOUND_404("not_found_404");

    private final String key;

    AdviceMessage(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
