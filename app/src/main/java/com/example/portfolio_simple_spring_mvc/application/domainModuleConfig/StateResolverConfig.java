package com.example.portfolio_simple_spring_mvc.application.domainModuleConfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.portfolio_simple_spring_mvc.domain.port.repository.reader.ProfileReader;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.ProfileAvatarStateResolver;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.ProfileStateResolver;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;

@Configuration
public class StateResolverConfig {

    @Bean
    public StateResolver<ProfileState> profileStateResolver(
        @Qualifier("profileReaderOfJpaRepo") ProfileReader profileReader
    ) {
        return new ProfileStateResolver(profileReader);
    }

    @Bean
    public StateResolver<ProfileAvatarState> profileAvatarStateResolver(
        StateResolver<ProfileState> profileStateResolver
    ) {
        return new ProfileAvatarStateResolver(profileStateResolver);
    }
}
