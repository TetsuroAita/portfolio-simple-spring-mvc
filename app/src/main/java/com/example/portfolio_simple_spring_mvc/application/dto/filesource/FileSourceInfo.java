package com.example.portfolio_simple_spring_mvc.application.dto.filesource;

public record FileSourceInfo(
    String originalFilename,
    String contentType,
    long contentSize
) {}
