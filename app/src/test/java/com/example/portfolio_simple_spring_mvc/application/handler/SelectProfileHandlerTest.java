package com.example.portfolio_simple_spring_mvc.application.handler;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileCommand;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.ResponseDto;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.SelectProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;

@ExtendWith(MockitoExtension.class)
public class SelectProfileHandlerTest {
    @Mock private StateResolver<ProfileState> stateResolver;
    @Mock private Planner<SelectProfilePlan, ProfileState> planner;
    @Mock private HandledResultFactory handledResultFactory;
    @InjectMocks private SelectProfileHandler handler;

    @Test
    void test_givenNull_throwException() {
        UUID id = null;
        var command = new ProfileCommand.Select(id);
        when(stateResolver.resolve(id)).thenThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verifyNoInteractions(planner, handledResultFactory);
    }

    @Test
    void test_stateIsNoExist_throwException() {
        UUID id = UUID.randomUUID();
        var command = new ProfileCommand.Select(id);
        var mockState = mock(ProfileState.NotExist.class);
        when(stateResolver.resolve(id)).thenReturn(mockState);
        when(planner.createPlan(mockState)).thenThrow(ProfileNotFoundException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(ProfileNotFoundException.class);
        verifyNoInteractions(handledResultFactory);
    }

    @Test
    void test_stateIsNotActive() {
        UUID id = UUID.randomUUID();
        Profile profile = Profile.createObjectForTest();
        var state = new ProfileState.NotActive(profile);
        var plan = new SelectProfilePlan(profile);
        var command = new ProfileCommand.Select(id);
        when(stateResolver.resolve(id)).thenReturn(state);
        when(planner.createPlan(state)).thenReturn(plan);

        handler.handle(command);

        verify(stateResolver).resolve(id);
        verify(planner).createPlan(state);
        verify(handledResultFactory).of(
            eq(HandledResultMessage.PROFILE_SELECTED),
            argThat((ResponseDto responseDto) ->
                responseDto != null &&
                responseDto.getFullName().equals("山田 太郎")
            )
        );
    }

    @Test
    void test_stateIsActive() {
        UUID id = UUID.randomUUID();
        Profile profile = Profile.createObjectForTest();
        var state = new ProfileState.Active(profile);
        var plan = new SelectProfilePlan(profile);
        var command = new ProfileCommand.Select(id);
        when(stateResolver.resolve(id)).thenReturn(state);
        when(planner.createPlan(state)).thenReturn(plan);

        handler.handle(command);

        verify(stateResolver).resolve(id);
        verify(planner).createPlan(state);
        verify(handledResultFactory).of(
            eq(HandledResultMessage.PROFILE_SELECTED),
            argThat((ResponseDto responseDto) ->
                responseDto != null &&
                responseDto.getFullName().equals("山田 太郎")
            )
        );
    }
}
