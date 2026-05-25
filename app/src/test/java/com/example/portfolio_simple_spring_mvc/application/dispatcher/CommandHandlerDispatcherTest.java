package com.example.portfolio_simple_spring_mvc.application.dispatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileCommand;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.ResponseDto;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;
import com.example.portfolio_simple_spring_mvc.application.handler.SelectProfileHandler;

@ExtendWith(MockitoExtension.class)
public class CommandHandlerDispatcherTest {
    private CommandHandlerDispatcher dispatcher;

    @Mock
    private ProfileCommand.Select command;
    
    @Mock
    private SelectProfileHandler handler;

    @Mock
    HandledResult<ResponseDto> expected;

    @BeforeEach
    void setUp() {
        doReturn(SelectProfileHandler.class).when(command).getHandlerType();
    }    
    
    @Test
    void testDispatcher_chosenCollectHandler() {
        doReturn(SelectProfileHandler.class).when(handler).getHandlerType();
        dispatcher = new CommandHandlerDispatcher(List.of(handler));
        when(handler.handle(command)).thenReturn(expected);

        HandledResult<ResponseDto> result = dispatcher.dispatch(command);

        assertThat(result).isEqualTo(expected);
        verify(handler).handle(command);
    }

    @Test
    void testDispatcher_cantFindHandler() {
        dispatcher = new CommandHandlerDispatcher(List.of());

        assertThatThrownBy(() -> dispatcher.dispatch(command))
            .hasMessageContaining("Handler not found: ");
    }
}
