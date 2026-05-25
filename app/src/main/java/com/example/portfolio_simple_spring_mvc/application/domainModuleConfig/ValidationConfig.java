package com.example.portfolio_simple_spring_mvc.application.domainModuleConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.portfolio_simple_spring_mvc.domain.validation.AvatarPolicy;
import com.example.portfolio_simple_spring_mvc.domain.validation.ProfileAvatarPolicy;

@Configuration
public class ValidationConfig {
    
    @Bean("profileAvatarPolicy")
    public AvatarPolicy profileAvatarPolicy() {
        return new ProfileAvatarPolicy();
    }
}
