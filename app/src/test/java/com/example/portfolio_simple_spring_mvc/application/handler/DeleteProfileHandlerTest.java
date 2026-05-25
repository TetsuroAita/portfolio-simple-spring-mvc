package com.example.portfolio_simple_spring_mvc.application.handler;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import org.springframework.context.ApplicationEventPublisher;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileCommand;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.domain.event.ProfileDeletedEvent;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.domain.executor.DeleteProfileExecutor;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.DeleteProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;

@ExtendWith(MockitoExtension.class)
public class DeleteProfileHandlerTest {
    @Mock private ApplicationEventPublisher publisher;
    @Mock private StateResolver<ProfileState> stateResolver;
    @Mock private Planner<DeleteProfilePlan, ProfileState> planner;
    @Mock private DeleteProfileExecutor executor;
    @Mock private HandledResultFactory handledResultFactory;
    @InjectMocks private DeleteProfileHandler handler;

    @Test
    void test_ProfileIdIsNull_throwException() {
        UUID id = null;
        var command = new ProfileCommand.Delete(id);
        when(stateResolver.resolve(id)).thenThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verifyNoInteractions(planner, executor, publisher, handledResultFactory);
    }

    @Test
    void test_ProfileStateIsNotExist_throwException() {
        var id = UUID.randomUUID();
        var state = mock(ProfileState.NotExist.class);
        var command = new ProfileCommand.Delete(id);
        when(stateResolver.resolve(id)).thenReturn(state);
        when(planner.createPlan(state)).thenThrow(ProfileNotFoundException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(ProfileNotFoundException.class);
        verifyNoInteractions(executor, publisher, handledResultFactory);
    }

    @Test
    void test_ProfileStateIsNotActive() {
        var id = UUID.randomUUID();
        var state = mock(ProfileState.NotActive.class);
        var plan = new DeleteProfilePlan(
            mock(Profile.class), false, true);
        var event = new ProfileDeletedEvent(mock(Profile.class));
        var command = new ProfileCommand.Delete(id);
        when(stateResolver.resolve(id)).thenReturn(state);
        when(planner.createPlan(state)).thenReturn(plan);
        when(executor.execute(plan)).thenReturn(event);

        handler.handle(command);

        verify(stateResolver).resolve(id);
        verify(planner).createPlan(state);
        verify(executor).execute(plan);
        verify(publisher).publishEvent(event);
        verify(handledResultFactory).of(HandledResultMessage.PROFILE_DELETED);
    }

    @Test
    void test_ProfileStateIsActive() {
        var id = UUID.randomUUID();
        var state = mock(ProfileState.Active.class);
        var plan = new DeleteProfilePlan(
            mock(Profile.class), true, false);
        ProfileDeletedEvent event = null;
        var command = new ProfileCommand.Delete(id);
        when(stateResolver.resolve(id)).thenReturn(state);
        when(planner.createPlan(state)).thenReturn(plan);
        when(executor.execute(plan)).thenReturn(event);

        handler.handle(command);

        verify(stateResolver).resolve(id);
        verify(planner).createPlan(state);
        verify(executor).execute(plan);
        verifyNoInteractions(publisher);
        verify(handledResultFactory).of(HandledResultMessage.PROFILE_DELETED);
    }
}
