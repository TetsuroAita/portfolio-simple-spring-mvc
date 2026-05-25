package com.example.portfolio_simple_spring_mvc.infrastructure.external_api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public sealed interface ApiRequest
    permits ApiRequest.Supabase_DeleteMultipleObjects,
            ApiRequest.Supabase_ExpiresObjectURL
{
    static Supabase_DeleteMultipleObjects prefixes(List<String> prefixes) {
        return new Supabase_DeleteMultipleObjects(
            prefixes
        );
    }

    record Supabase_DeleteMultipleObjects(
        List<String> prefixes
    ) implements ApiRequest {}

    record Supabase_ExpiresObjectURL(
        @JsonProperty("expiresIn") long expiresIn
    ) implements ApiRequest {}
}
