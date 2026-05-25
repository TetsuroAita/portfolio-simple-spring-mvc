package com.example.portfolio_simple_spring_mvc.application.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileCommand;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.RequestDto;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.ProfileWriter;
import com.example.portfolio_simple_spring_mvc.domain.service.GeneratePersonalNumberService;

@ExtendWith(MockitoExtension.class)
public class InsertProfileHandlerTest {
    @Mock private ProfileWriter profileWriter;
    @Mock private GeneratePersonalNumberService generatePersonalNumberService;
    @Mock private HandledResultFactory handledResultFactory;
    @InjectMocks private InsertProfileHandler handler;

    @Test
    void test_() {
        var command = new ProfileCommand.Insert(RequestDto.createObjectForTest());
        when(generatePersonalNumberService.nextPersonalNumber()).thenReturn("001");

        handler.handle(command);

        verify(profileWriter).insertProfile(any());
        verify(generatePersonalNumberService).nextPersonalNumber();
        verify(handledResultFactory).of(HandledResultMessage.PROFILE_INSERTED);
    }
}
