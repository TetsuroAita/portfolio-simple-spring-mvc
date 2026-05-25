package com.example.portfolio_simple_spring_mvc.application.handler;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileAvatarCommand;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.domain.event.ProfileAvatarChangedEvent;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.DeleteProfileAvatarPlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.AvatarWriter;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;

@ExtendWith(MockitoExtension.class)
public class DeleteProfileAvatarHandlerTest {
    @Mock private ApplicationEventPublisher publisher;
    @Mock private StateResolver<ProfileAvatarState> stateResolver;
    @Mock private Planner<DeleteProfileAvatarPlan, ProfileAvatarState> planner;
    @Mock private AvatarWriter avatarWriter;
    @Mock private HandledResultFactory handledResultFactory;
    @InjectMocks private DeleteProfileAvatarHandler handler;

    private static UUID PROFILE_ID = UUID.randomUUID();

    @Test
    void test_givenProfileIdNull_throwException() {
        var command = ProfileAvatarCommand.delete(null);
        given(stateResolver.resolve(null)).willThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verifyNoInteractions(planner, avatarWriter, publisher, handledResultFactory);
    }

    @Test
    @DisplayName("プロフィールが有効かつアバターが存在する場合、削除に成功すること")
    void test_stateIsProfileActiveHasAvatar_returnHandledResult() {
        Profile profile = Profile.createObjectForTest();
        profile.setAvatar(Avatar.createObjectForTest());

        var state = new ProfileAvatarState.ProfileActive_AvatarExist(profile);
        var plan = new DeleteProfileAvatarPlan(profile);
        var command = ProfileAvatarCommand.delete(PROFILE_ID);

        given(stateResolver.resolve(PROFILE_ID)).willReturn(state);
        given(planner.createPlan(state)).willReturn(plan);

        handler.handle(command);

        verify(stateResolver).resolve(PROFILE_ID);
        verify(planner).createPlan(state);
        verify(avatarWriter).updateAvatar(
            argThat(avatar ->
                avatar != null &&
                avatar.activeStatus() == false
            )
        );
        verify(publisher).publishEvent(
            argThat((ProfileAvatarChangedEvent event) ->
                event.profile().equals(profile) &&
                event.avatar() == null
            )
        );
        verify(handledResultFactory).of(HandledResultMessage.AVATAR_DELETED);
    }

    @Test
    @DisplayName("DB更新時に例外が発生した場合、イベント発行や結果生成が行われないこと")
    void test_throwExceptionWhenDBUpdate() {
        Profile profile = Profile.createObjectForTest();
        profile.setAvatar(Avatar.createObjectForTest());

        var state = new ProfileAvatarState.ProfileActive_AvatarExist(profile);
        var plan = new DeleteProfileAvatarPlan(profile);
        var command = ProfileAvatarCommand.delete(PROFILE_ID);

        given(stateResolver.resolve(PROFILE_ID)).willReturn(state);
        given(planner.createPlan(state)).willReturn(plan);
        doThrow(RuntimeException.class).when(avatarWriter).updateAvatar(any());

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(RuntimeException.class);
        verify(stateResolver).resolve(PROFILE_ID);
        verify(planner).createPlan(state);
        verify(avatarWriter).updateAvatar(any());
        verifyNoInteractions(publisher, handledResultFactory);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidStates")
    void test_stateIsNotProfileActiveHasNoAvatar_throwException(ProfileAvatarState state) {
        var command = ProfileAvatarCommand.delete(PROFILE_ID);

        given(stateResolver.resolve(PROFILE_ID)).willReturn(state);
        given(planner.createPlan(state)).willThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verifyNoInteractions(avatarWriter, publisher, handledResultFactory);
    }

    private static Stream<Arguments> provideInvalidStates() {
        Profile profile = Profile.createObjectForTest();
        profile.setAvatar(Avatar.createObjectForTest());
        return Stream.of(
            Arguments.of(new ProfileAvatarState.ProfileNotActive_AvatarNotExist(profile)),
            Arguments.of(new ProfileAvatarState.ProfileNotActive_AvatarExist(profile)),
            Arguments.of(new ProfileAvatarState.ProfileActive_AvatarNotExist(profile))
        );
    }
}