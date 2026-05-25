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
import com.example.portfolio_simple_spring_mvc.application.dto.profile.RequestDto;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.SelectProfileForEditPlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;

@ExtendWith(MockitoExtension.class)
public class SelectProfileForEditHandlerTest {
    @Mock private StateResolver<ProfileState> stateResolver;
    @Mock private Planner<SelectProfileForEditPlan, ProfileState> planner;
    @Mock private HandledResultFactory handledResultFactory;
    @InjectMocks private SelectProfileForEditHandler handler;

    @Test
    void test_givenNull_throwException() {
        UUID id = null;
        var command = new ProfileCommand.Select_ForEdit(id);
        when(stateResolver.resolve(id)).thenThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verifyNoInteractions(planner, handledResultFactory);
    }

    @Test
    void test_stateIsNoExist_throwException() {
        UUID id = UUID.randomUUID();
        ProfileState mockState = mock(ProfileState.NotExist.class);
        var command = new ProfileCommand.Select_ForEdit(id);
        when(stateResolver.resolve(id)).thenReturn(mockState);
        when(planner.createPlan(mockState)).thenThrow(ProfileNotFoundException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(ProfileNotFoundException.class);
        verifyNoInteractions(handledResultFactory);
    }

    @Test
    void test_stateIsNotActive_throwException() {
        UUID id = UUID.randomUUID();
        ProfileState mockState = mock(ProfileState.NotActive.class);
        var command = new ProfileCommand.Select_ForEdit(id);
        when(stateResolver.resolve(id)).thenReturn(mockState);
        when(planner.createPlan(mockState)).thenThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verifyNoInteractions(handledResultFactory);
    }

    @Test
    void test_stateIsActive() {
        UUID id = UUID.randomUUID();
        Profile profile = Profile.createObjectForTest();
        ProfileState state = new ProfileState.Active(profile);
        SelectProfileForEditPlan plan = new SelectProfileForEditPlan(profile);
        var command = new ProfileCommand.Select_ForEdit(id);
        when(stateResolver.resolve(id)).thenReturn(state);
        when(planner.createPlan(state)).thenReturn(plan);

        handler.handle(command);

        verify(stateResolver).resolve(id);
        verify(planner).createPlan(state);
        verify(handledResultFactory).of(
            eq(HandledResultMessage.PROFILE_SELECTED_FOR_EDIT),
            argThat((RequestDto requestDto) ->
                requestDto != null &&
                requestDto.getLastName().equals("山田")
            )
        );
    }
}