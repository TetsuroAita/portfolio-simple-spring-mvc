package com.example.portfolio_simple_spring_mvc.application.handler;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
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

import com.example.portfolio_simple_spring_mvc.application.command.ProfileAvatarCommand;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.SelectProfileAvatarPlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.port.storage.AvatarStorageClient;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;

@ExtendWith(MockitoExtension.class)
public class SelectProfileAvatarHandlerTest {
    @Mock private StateResolver<ProfileAvatarState> stateResolver;
    @Mock private Planner<SelectProfileAvatarPlan, ProfileAvatarState> planner;
    @Mock private AvatarStorageClient avatarStorageClient;
    @Mock private HandledResultFactory handledResultFactory;
    @InjectMocks private SelectProfileAvatarHandler handler;

    @Test
    void test_givenProfileIdNull_throwException() {
        UUID id = null;
        var command = ProfileAvatarCommand.select(id);

        given(stateResolver.resolve(id)).willThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verify(stateResolver).resolve(id);
        verifyNoInteractions(planner, avatarStorageClient, handledResultFactory);
        verifyNoMoreInteractions(stateResolver, planner, avatarStorageClient, handledResultFactory);
    }

    @Test
    void test_ProfileIdIsNotExist_throwException() {
        UUID id = UUID.randomUUID();
        var command = ProfileAvatarCommand.select(id);

        given(stateResolver.resolve(id)).willThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verify(stateResolver).resolve(id);
        verifyNoInteractions(planner, avatarStorageClient, handledResultFactory);
        verifyNoMoreInteractions(stateResolver, planner, avatarStorageClient, handledResultFactory);
    }

    @Test
    void test_stateIsProfileNotActiveHasNoAvatar_throwException() {
        UUID id = UUID.randomUUID();
        Profile profile = Profile.createObjectForTest();
        var state = new ProfileAvatarState.ProfileNotActive_AvatarNotExist(profile);
        var command = ProfileAvatarCommand.select(id);

        given(stateResolver.resolve(id)).willReturn(state);
        given(planner.createPlan(state)).willThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verify(stateResolver).resolve(id);
        verify(planner).createPlan(state);
        verifyNoInteractions(avatarStorageClient, handledResultFactory);
        verifyNoMoreInteractions(stateResolver, planner, avatarStorageClient, handledResultFactory);
    }

    @Test
    void test_stateIsProfileNotActiveHasAvatar_returnHandledResult() {
        UUID id = UUID.randomUUID();
        Profile profile = Profile.createObjectForTest();
        Avatar avatar = Avatar.createObjectForTest();
        profile.setAvatar(avatar);
        var state = new ProfileAvatarState.ProfileNotActive_AvatarExist(profile);
        var plan = new SelectProfileAvatarPlan(profile);
        var command = ProfileAvatarCommand.select(id);

        given(stateResolver.resolve(id)).willReturn(state);
        given(planner.createPlan(state)).willReturn(plan);
        given(avatarStorageClient.selectAvatar(avatar.generatePath())).willReturn("avatarURL");

        handler.handle(command);

        verify(stateResolver).resolve(id);
        verify(planner).createPlan(state);
        verify(avatarStorageClient).selectAvatar(avatar.generatePath());
        verify(handledResultFactory).of(
            eq(HandledResultMessage.AVATAR_SELECTED),
            argThat(avatarUrl ->
                avatarUrl != null &&
                avatarUrl.equals("avatarURL")
            )
        );
        verifyNoMoreInteractions(stateResolver, planner, avatarStorageClient, handledResultFactory);
    }

    @Test
    void test_stateIsProfileActiveHasNoAvatar_throwException() {
        UUID id = UUID.randomUUID();
        Profile profile = Profile.createObjectForTest();
        var state = new ProfileAvatarState.ProfileActive_AvatarNotExist(profile);
        var command = ProfileAvatarCommand.select(id);

        given(stateResolver.resolve(id)).willReturn(state);
        given(planner.createPlan(state)).willThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verify(stateResolver).resolve(id);
        verify(planner).createPlan(state);
        verifyNoInteractions(avatarStorageClient, handledResultFactory);
        verifyNoMoreInteractions(stateResolver, planner, avatarStorageClient, handledResultFactory);
    }

    @Test
    void test_stateIsProfileActiveHasAvatar_returnHandledResult() {
        UUID id = UUID.randomUUID();
        Profile profile = Profile.createObjectForTest();
        Avatar avatar = Avatar.createObjectForTest();
        profile.setAvatar(avatar);
        var state = new ProfileAvatarState.ProfileActive_AvatarExist(profile);
        var plan = new SelectProfileAvatarPlan(profile);
        var command = ProfileAvatarCommand.select(id);

        given(stateResolver.resolve(id)).willReturn(state);
        given(planner.createPlan(state)).willReturn(plan);
        given(avatarStorageClient.selectAvatar(avatar.generatePath())).willReturn("avatarURL");

        handler.handle(command);

        verify(stateResolver).resolve(id);
        verify(planner).createPlan(state);
        verify(avatarStorageClient).selectAvatar(avatar.generatePath());
        verify(handledResultFactory).of(
            eq(HandledResultMessage.AVATAR_SELECTED),
            argThat(avatarUrl ->
                avatarUrl != null &&
                avatarUrl.equals("avatarURL")
            )
        );
        verifyNoMoreInteractions(stateResolver, planner, avatarStorageClient, handledResultFactory);
    }
}
