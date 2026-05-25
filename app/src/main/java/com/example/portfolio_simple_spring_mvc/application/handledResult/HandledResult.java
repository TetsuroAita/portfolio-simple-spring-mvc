package com.example.portfolio_simple_spring_mvc.application.handledResult;

import com.fasterxml.jackson.annotation.JsonInclude;


public record HandledResult<T> (
    String message,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    T data
) {}