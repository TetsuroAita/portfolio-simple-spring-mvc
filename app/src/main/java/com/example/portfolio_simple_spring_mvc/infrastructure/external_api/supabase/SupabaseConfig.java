package com.example.portfolio_simple_spring_mvc.infrastructure.external_api.supabase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupabaseConfig {

    @Bean
    public SupabaseUtil supabaseUtils(
        @Value("${storage.project-url}") String projectUrl,
        @Value("${storage.api-key}") String apiKey,
        @Value("${storage.bucket-name-avatar}") String bucketName,
        @Value("${storage.expires-in}") long expiresIn
    ) {
        return new SupabaseUtil(projectUrl, apiKey, bucketName, expiresIn);
    }
}
