package com.example.portfolio_simple_spring_mvc.presentation.controller;

public enum ControllerMessage {

    // GlobalErrorController
    INTERNAL_SERVER_ERROR_500("internal_server_error_500"),

    // ProfileController
    AGREE("agree");

    private final String key;

    ControllerMessage(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
