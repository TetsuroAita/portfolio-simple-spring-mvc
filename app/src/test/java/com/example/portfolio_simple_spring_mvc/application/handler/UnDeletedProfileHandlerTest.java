package com.example.portfolio_simple_spring_mvc.application.handler;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileCommand;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.UnDeletedProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.ProfileWriter;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;

@ExtendWith(MockitoExtension.class)
public class UnDeletedProfileHandlerTest {
    @Mock private ProfileWriter profileWriter;
    @Mock private StateResolver<ProfileState> stateResolver;
    @Mock private Planner<UnDeletedProfilePlan, ProfileState> planner;
    @Mock private HandledResultFactory handledResultFactory;
    @InjectMocks private UnDeletedProfileHandler handler;

    @Test
    void test_givenNull_throwException() {
        UUID id = null;
        var command = new ProfileCommand.UnDeleted(id);
        when(stateResolver.resolve(id)).thenThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verifyNoInteractions(planner, profileWriter, handledResultFactory);
    }

    @Test
    void test_stateIsNotExist_throwException() {
        UUID id = UUID.randomUUID();
        var state = new ProfileState.NotExist();
        var command = new ProfileCommand.UnDeleted(id);
        when(stateResolver.resolve(id)).thenReturn(state);
        when(planner.createPlan(state)).thenThrow(ProfileNotFoundException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(ProfileNotFoundException.class);
        verifyNoInteractions(profileWriter, handledResultFactory);
    }

    @Test
    void test_stateIsNotActive() {
        UUID id = UUID.randomUUID();
        Profile profile = Profile.createObjectForTest();
        var state = new ProfileState.NotActive(profile);
        var plan = new UnDeletedProfilePlan(profile);
        var command = new ProfileCommand.UnDeleted(id);
        
        given(stateResolver.resolve(id)).willReturn(state);
        given(planner.createPlan(state)).willReturn(plan);

        handler.handle(command);

        verify(stateResolver).resolve(id);
        verify(planner).createPlan(state);
        verify(profileWriter).updateProfile(profile);
        verify(handledResultFactory).of(HandledResultMessage.PROFILE_UN_DELETED);
        verifyNoMoreInteractions(stateResolver, planner, profileWriter, handledResultFactory);
    }

    @Test
    void test_stateIsActive_throwException() {
        UUID id = UUID.randomUUID();
        Profile profile = Profile.createObjectForTest();
        var state = new ProfileState.Active(profile);
        var command = new ProfileCommand.UnDeleted(id);
        when(stateResolver.resolve(id)).thenReturn(state);
        when(planner.createPlan(state)).thenThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verifyNoInteractions(profileWriter, handledResultFactory);
    }
}
