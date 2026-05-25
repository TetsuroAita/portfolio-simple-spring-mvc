package com.example.portfolio_simple_spring_mvc.application.domainModuleConfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.portfolio_simple_spring_mvc.domain.executor.DeleteAvatarsExecutor;
import com.example.portfolio_simple_spring_mvc.domain.executor.DeleteProfileExecutor;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.reader.AvatarReader;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.AvatarWriter;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.ProfileWriter;
import com.example.portfolio_simple_spring_mvc.domain.port.storage.AvatarStorageClient;

@Configuration
public class ExecutorConfig {

    @Bean
    public DeleteProfileExecutor deleteProfileExecutor(
        @Qualifier("profileWriterOfJpaRepo") ProfileWriter profileWriter
    ) {
        return new DeleteProfileExecutor(profileWriter);
    }

    @Bean
    public DeleteAvatarsExecutor deleteAvatarsExecutor(
        @Qualifier("avatarReaderOfJpaRepo") AvatarReader avatarReader,
        @Qualifier("avatarWriterOfJpaRepo") AvatarWriter avatarWriter,
        @Qualifier("avatarStorageClientOfSupabase") AvatarStorageClient avatarStorageClient
    ) {
        return new DeleteAvatarsExecutor(
            avatarReader,
            avatarWriter,
            avatarStorageClient
        );
    }
}
