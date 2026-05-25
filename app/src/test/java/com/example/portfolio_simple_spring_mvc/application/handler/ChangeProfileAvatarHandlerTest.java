package com.example.portfolio_simple_spring_mvc.application.handler;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
import com.example.portfolio_simple_spring_mvc.application.dto.filesource.FileSource;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.domain.event.ProfileAvatarChangedEvent;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainValidationException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.ChangeProfileAvatarPlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.AvatarWriter;
import com.example.portfolio_simple_spring_mvc.domain.service.CreateNewAvatarService;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;
import com.example.portfolio_simple_spring_mvc.domain.validation.AvatarPolicy;

@ExtendWith(MockitoExtension.class)
public class ChangeProfileAvatarHandlerTest {
    @Mock private ApplicationEventPublisher publisher;
    @Mock private StateResolver<ProfileAvatarState> stateResolver;
    @Mock private Planner<ChangeProfileAvatarPlan, ProfileAvatarState> planner;
    @Mock private AvatarPolicy avatarPolicy;
    @Mock private CreateNewAvatarService createNewAvatarService;
    @Mock private AvatarWriter avatarWriter;
    @Mock private HandledResultFactory handledResultFactory;
    @InjectMocks private ChangeProfileAvatarHandler handler;

    private static UUID PROFILE_ID = UUID.randomUUID();
    @Mock FileSource mockFileSource;

    @Test
    @DisplayName("profileId が null なら例外を期待")
    void test_givenProfileIdNull_throwException() {
        var command = ProfileAvatarCommand.change(null, mockFileSource);
        given(stateResolver.resolve(null))
            .willThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verifyNoInteractions(
            planner, avatarPolicy, createNewAvatarService,
            avatarWriter, publisher, handledResultFactory
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidStates")
    @DisplayName("プロフィールが無効なら例外を期待")
    void test_stateIsInvalidState_throwException(ProfileAvatarState state) {
        var command = ProfileAvatarCommand.change(PROFILE_ID, mockFileSource);

        given(stateResolver.resolve(PROFILE_ID)).willReturn(state);
        given(planner.createPlan(state))
            .willThrow(DomainIllegalStateException.class);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainIllegalStateException.class);
        verifyNoInteractions(
            avatarPolicy, createNewAvatarService,
            avatarWriter, publisher, handledResultFactory
        );
    }

    private static Stream<Arguments> provideInvalidStates() {
        Profile profile = Profile.createObjectForTest();
        return Stream.of(
            Arguments.of(new ProfileAvatarState.ProfileNotActive_AvatarNotExist(profile)),
            Arguments.of(new ProfileAvatarState.ProfileNotActive_AvatarExist(profile))
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidStates")
    @DisplayName("FileSource がポリシー違反で、イベント発行や結果生成が行われないことを期待")
    void test_AvatarPolicyIsInvalid_throwException(ProfileAvatarState state) {
        var plan = new ChangeProfileAvatarPlan(state.profile());
        var command = ProfileAvatarCommand.change(PROFILE_ID, mockFileSource);

        given(stateResolver.resolve(PROFILE_ID)).willReturn(state);
        given(planner.createPlan(state)).willReturn(plan);
        doThrow(DomainValidationException.class)
            .when(avatarPolicy).validate(mockFileSource, plan.profile().getAvatar());

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(DomainValidationException.class);
        verify(stateResolver).resolve(PROFILE_ID);
        verify(planner).createPlan(state);
        verify(avatarPolicy).validate(mockFileSource, plan.profile().getAvatar());
        verifyNoInteractions(createNewAvatarService, avatarWriter, publisher, handledResultFactory);
    }

    @ParameterizedTest
    @MethodSource("provideValidStates")
    @DisplayName("createNewAvatarServiceで例外発生で、イベント発行や結果生成が行われないことを期待")
    void test_throwExceptionAtCreateNewAvatarService(ProfileAvatarState state) {
        var plan = new ChangeProfileAvatarPlan(state.profile());
        var command = ProfileAvatarCommand.change(PROFILE_ID, mockFileSource);

        given(stateResolver.resolve(PROFILE_ID)).willReturn(state);
        given(planner.createPlan(state)).willReturn(plan);
        doThrow(RuntimeException.class)
            .when(createNewAvatarService).createOf(mockFileSource);

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(RuntimeException.class);
        verify(stateResolver).resolve(PROFILE_ID);
        verify(planner).createPlan(state);
        verify(avatarPolicy).validate(mockFileSource, plan.profile().getAvatar());
        verify(createNewAvatarService).createOf(mockFileSource);
        verifyNoInteractions(avatarWriter, publisher, handledResultFactory);
    }

    private static Stream<Arguments> provideValidStates() {
        Profile profile = Profile.createObjectForTest();
        return Stream.of(
            Arguments.of(new ProfileAvatarState.ProfileActive_AvatarNotExist(profile)),
            Arguments.of(new ProfileAvatarState.ProfileActive_AvatarExist(profile))
        );
    }

    @Test
    @DisplayName("""
            avatarWriter.updateAvatar(plan.profile().getAvatar().inactivate()) で例外発生しても createNewAvatarService はロールバックされない。
            イベント発行や結果生成が行われないことを期待
            """)
    void test_throwExceptionAtAvatarWriter_updateOldAvatar() {
        Profile profile = Profile.createObjectForTest();
        Avatar oldAvatar = new Avatar("old_avatar.png", "image/png", 1L).activate();
        profile.setAvatar(oldAvatar);
        Avatar newAvatar = new Avatar("new_avatar.png", "image/png", 1L);

        var state = new ProfileAvatarState.ProfileActive_AvatarExist(profile);
        var plan = new ChangeProfileAvatarPlan(state.profile());
        var command = ProfileAvatarCommand.change(PROFILE_ID, mockFileSource);

        given(stateResolver.resolve(PROFILE_ID)).willReturn(state);
        given(planner.createPlan(state)).willReturn(plan);
        given(createNewAvatarService.createOf(mockFileSource)).willReturn(newAvatar);
        doThrow(RuntimeException.class)
            .when(avatarWriter).updateAvatar(plan.profile().getAvatar().inactivate());

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(RuntimeException.class);
        verify(stateResolver).resolve(PROFILE_ID);
        verify(planner).createPlan(state);
        verify(avatarPolicy).validate(mockFileSource, plan.profile().getAvatar());
        verify(createNewAvatarService).createOf(mockFileSource);
        verify(avatarWriter).updateAvatar(
            argThat((Avatar a) ->
                a.getOriginalFilename().equals("old_avatar.png") &&
                a.activeStatus() == false
            )
        );
        verifyNoMoreInteractions(avatarWriter);
        verifyNoInteractions(publisher, handledResultFactory);
    }

    @Test
    @DisplayName("""
            avatarWriter.updateAvatar(newAvatar.activate()) で例外発生しても createNewAvatarService はロールバックされない。
            イベント発行や結果生成が行われないことを期待
            """)
    void test_throwExceptionAtAvatarWriter_updateNewAvatar() {
        Profile profile = Profile.createObjectForTest();
        Avatar newAvatar = new Avatar("new_avatar.png", "image/png", 1L);

        var state = new ProfileAvatarState.ProfileActive_AvatarExist(profile);
        var plan = new ChangeProfileAvatarPlan(state.profile());
        var command = ProfileAvatarCommand.change(PROFILE_ID, mockFileSource);

        given(stateResolver.resolve(PROFILE_ID)).willReturn(state);
        given(planner.createPlan(state)).willReturn(plan);
        given(createNewAvatarService.createOf(mockFileSource)).willReturn(newAvatar);
        doThrow(RuntimeException.class)
            .when(avatarWriter).updateAvatar(newAvatar.activate());

        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(RuntimeException.class);
        verify(stateResolver).resolve(PROFILE_ID);
        verify(planner).createPlan(state);
        verify(avatarPolicy).validate(mockFileSource, plan.profile().getAvatar());
        verify(createNewAvatarService).createOf(mockFileSource);
        verify(avatarWriter).updateAvatar(
            argThat((Avatar a) ->
                a.getOriginalFilename().equals("new_avatar.png") &&
                a.activeStatus() == true
            )
        );
        verifyNoInteractions(publisher, handledResultFactory);
    }

    @Test
    @DisplayName("プロフィールが有効かつアバターを保持していなければ AVATAR_INSERTED を期待")
    void test_whenProfileActiveHasNoAvatar_returnHandledResultIsAVATAR_INSERTED() {
        Profile profile = Profile.createObjectForTest();
        Avatar newAvatar = new Avatar("new_avatar.png", "image/png", 1L);

        var state = new ProfileAvatarState.ProfileActive_AvatarNotExist(profile);
        var plan = new ChangeProfileAvatarPlan(state.profile());
        var command = ProfileAvatarCommand.change(PROFILE_ID, mockFileSource);

        given(stateResolver.resolve(PROFILE_ID)).willReturn(state);
        given(planner.createPlan(state)).willReturn(plan);
        given(createNewAvatarService.createOf(mockFileSource)).willReturn(newAvatar);

        handler.handle(command);

        verify(stateResolver).resolve(PROFILE_ID);
        verify(planner).createPlan(
            argThat((ProfileAvatarState.ProfileActive_AvatarNotExist s) ->
                s.profile().getAvatar() == null
            )
        );
        verify(avatarPolicy).validate(mockFileSource, plan.profile().getAvatar());
        verify(createNewAvatarService).createOf(mockFileSource);
        verify(avatarWriter).updateAvatar(
            argThat(a ->
                a.getOriginalFilename().equals("new_avatar.png") &&
                a.activeStatus() == true
            )
        );
        verify(publisher).publishEvent(
            argThat((ProfileAvatarChangedEvent e) ->
                e.profile().equals(plan.profile()) &&
                e.avatar().getOriginalFilename().equals("new_avatar.png") &&
                e.avatar().activeStatus() == true
            )
        );
        verify(handledResultFactory).of(HandledResultMessage.AVATAR_INSERTED);
    }

    @Test
    @DisplayName("プロフィールが有効かつアバターを保持していれば AVATAR_UPDATED を期待")
    void test_whenProfileActiveHasAvatar_returnHandledResultIsAVATAR_UPDATED() {
        Profile profile = Profile.createObjectForTest();
        Avatar oldAvatar = new Avatar("old_avatar.png", "image/png", 1L).activate();
        profile.setAvatar(oldAvatar);
        Avatar newAvatar = new Avatar("new_avatar.png", "image/png", 1L);

        var state = new ProfileAvatarState.ProfileActive_AvatarExist(profile);
        var plan = new ChangeProfileAvatarPlan(state.profile());
        var command = ProfileAvatarCommand.change(PROFILE_ID, mockFileSource);

        given(stateResolver.resolve(PROFILE_ID)).willReturn(state);
        given(planner.createPlan(state)).willReturn(plan);
        given(createNewAvatarService.createOf(mockFileSource)).willReturn(newAvatar);

        handler.handle(command);

        verify(stateResolver).resolve(PROFILE_ID);
        verify(planner).createPlan(
            argThat((ProfileAvatarState.ProfileActive_AvatarExist s) ->
                s.profile().getAvatar() == oldAvatar
            )
        );
        verify(avatarPolicy).validate(mockFileSource, plan.profile().getAvatar());
        verify(createNewAvatarService).createOf(mockFileSource);
        verify(avatarWriter).updateAvatar(
            argThat((Avatar a) ->
                a.getOriginalFilename().equals("old_avatar.png") &&
                a.activeStatus() == false
            )
        );
        verify(avatarWriter).updateAvatar(
            argThat((Avatar a) ->
                a.getOriginalFilename().equals("new_avatar.png") &&
                a.activeStatus() == true
            )
        );
        verify(publisher).publishEvent(
            argThat((ProfileAvatarChangedEvent e) ->
                e.profile().equals(plan.profile()) &&
                e.avatar().getOriginalFilename().equals("new_avatar.png") &&
                e.avatar().activeStatus() == true
            )
        );
        verify(avatarWriter).updateAvatar(
            argThat(a ->
                a.activeStatus() == true
            )
        );
        verify(handledResultFactory).of(HandledResultMessage.AVATAR_UPDATED);
    }
}