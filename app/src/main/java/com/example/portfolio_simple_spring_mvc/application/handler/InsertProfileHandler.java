package com.example.portfolio_simple_spring_mvc.application.handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileCommand;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.application.util.ProfileMapper;

import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.ProfileWriter;
import com.example.portfolio_simple_spring_mvc.domain.service.GeneratePersonalNumberService;

@Service
public class InsertProfileHandler implements CommandHandler<Void, ProfileCommand.Insert> {
    private final ProfileWriter profileWriter;
    private final GeneratePersonalNumberService generatePersonalNumberService;
    private final HandledResultFactory handledResultFactory;

    public InsertProfileHandler(
        @Qualifier("profileWriterOfJpaRepo") ProfileWriter profileWriter,
        GeneratePersonalNumberService generatePersonalNumberService,
        HandledResultFactory handledResultFactory
    ) {
        this.profileWriter = profileWriter;
        this.generatePersonalNumberService = generatePersonalNumberService;
        this.handledResultFactory = handledResultFactory;
    }

    @Transactional
    @Override
    public HandledResult<Void> handle(ProfileCommand.Insert command) {
        profileWriter.insertProfile(
            ProfileMapper.toNewProfile(
                command.requestDto(),
                generatePersonalNumberService.nextPersonalNumber()
            )
        );
        
        return handledResultFactory.of(HandledResultMessage.PROFILE_INSERTED);
    }

    @Override
    public Class<? extends CommandHandler<?, ?>> getHandlerType() {
        return InsertProfileHandler.class;
    }
}
