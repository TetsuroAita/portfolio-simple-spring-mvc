package com.example.portfolio_simple_spring_mvc.infrastructure.external_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public sealed interface ApiResponse
    permits ApiResponse.Supabase_Error,
            ApiResponse.Supabase_GeneratedPresignedUrl,
            ApiResponse.Supabase_DeletedObjectName
{
    record Supabase_Error(
        String statuscode,
        String error,
        String message
    ) implements ApiResponse {}   

    record Supabase_GeneratedPresignedUrl(
        @JsonProperty("signedURL") String signedURL
    ) implements ApiResponse {}

    record Supabase_DeletedObjectName(
        String name
    ) implements ApiResponse {}
}

