package com.example.portfolio_simple_spring_mvc.application.handler;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileCommand;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.RequestDto;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.UpdateProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.ProfileWriter;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;

@ExtendWith(MockitoExtension.class)
public class UpdateProfileHandlerTest {
    @Mock private ProfileWriter profileWriter;
    @Mock private StateResolver<ProfileState> stateResolver;
    @Mock private Planner<UpdateProfilePlan, ProfileState> planner;
    @Mock private HandledResultFactory handledResultFactory;
    @InjectMocks private UpdateProfileHandler handler;

    @Test
    void test_givenProfileIdNull_throwException() {
        UUID id = null;
        RequestDto requestDto = RequestDto.createObjectForTest();
        var command = ProfileCommand.update(id, requestDto);
        
        given(stateResolver.resolve(id)).willThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verify(stateResolver).resolve(id);
        verifyNoInteractions(planner, profileWriter, handledResultFactory);
        verifyNoMoreInteractions(stateResolver, planner, profileWriter, handledResultFactory);
    }

    @Test
    void test_stateIsNoExsit_throwException() {
        UUID id = UUID.randomUUID();
        RequestDto requestDto = RequestDto.createObjectForTest();
        var state = new ProfileState.NotExist();
        var command = ProfileCommand.update(id, requestDto);
        
        given(stateResolver.resolve(id)).willReturn(state);
        given(planner.createPlan(state)).willThrow(ProfileNotFoundException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(ProfileNotFoundException.class);
        verify(stateResolver).resolve(id);
        verify(planner).createPlan(state);
        verifyNoInteractions(profileWriter, handledResultFactory);
        verifyNoMoreInteractions(stateResolver, planner, profileWriter, handledResultFactory);
    }

    @Test
    void test_stateIsNotActive_throwException() {
        UUID id = UUID.randomUUID();
        Profile profile = Profile.createObjectForTest();
        RequestDto requestDto = RequestDto.createObjectForTest();
        var state = new ProfileState.NotActive(profile);
        var command = ProfileCommand.update(id, requestDto);
        
        given(stateResolver.resolve(id)).willReturn(state);
        given(planner.createPlan(state)).willThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verify(stateResolver).resolve(id);
        verify(planner).createPlan(state);
        verifyNoInteractions(profileWriter, handledResultFactory);
        verifyNoMoreInteractions(stateResolver, planner, profileWriter, handledResultFactory);
    }

    @Test
    void test_stateIsActive_throwException() {
        UUID id = UUID.randomUUID();
        Profile profile = Profile.createObjectForTest();
        RequestDto requestDto = RequestDto.createObjectForTest();
        var state = new ProfileState.Active(profile);
        var plan = new UpdateProfilePlan(profile);
        var command = ProfileCommand.update(id, requestDto);
        
        given(stateResolver.resolve(id)).willReturn(state);
        given(planner.createPlan(state)).willReturn(plan);

        handler.handle(command);

        verify(stateResolver).resolve(id);
        verify(planner).createPlan(state);
        verify(profileWriter).updateProfile(profile);
        verify(handledResultFactory).of(HandledResultMessage.PROFILE_UPDATED);
        verifyNoMoreInteractions(stateResolver, planner, profileWriter, handledResultFactory);
    }
}
