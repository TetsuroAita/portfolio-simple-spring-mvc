package com.example.portfolio_simple_spring_mvc.application.domainModuleConfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.portfolio_simple_spring_mvc.domain.port.repository.reader.SequenceReader;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.AvatarWriter;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.SequenceWriter;
import com.example.portfolio_simple_spring_mvc.domain.port.storage.AvatarStorageClient;
import com.example.portfolio_simple_spring_mvc.domain.service.CreateNewAvatarService;
import com.example.portfolio_simple_spring_mvc.domain.service.GeneratePersonalNumberService;

@Configuration
public class ServiceConfig {
    
    @Bean
    public GeneratePersonalNumberService generatePersonalNumberService(
        @Qualifier("sequenceReaderOfJpaRepo") SequenceReader sequenceReader,
        @Qualifier("sequenceWriterOfJpaRepo") SequenceWriter sequenceWriter
    ) {
        return new GeneratePersonalNumberService(sequenceReader, sequenceWriter);
    }

    @Bean
    public CreateNewAvatarService createNewAvatarService(
        @Qualifier("avatarWriterOfJpaRepo") AvatarWriter avatarWriter,
        @Qualifier("avatarStorageClientOfSupabase") AvatarStorageClient avatarStorageClient
    ) {
        return new CreateNewAvatarService(avatarWriter, avatarStorageClient);
    }
}
