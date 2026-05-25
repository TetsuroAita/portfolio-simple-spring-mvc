package com.example.portfolio_simple_spring_mvc.domain.stateResolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;

@ExtendWith(MockitoExtension.class)
public class ProfileAvatarStateResolverTest {
    @Mock
    private ProfileStateResolver profile_StateResolver;

    @InjectMocks
    private ProfileAvatarStateResolver profileAvatar_StateResolver;

    @Mock private ProfileState.NotExist notExist;
    @Mock private ProfileState.NotActive notActive;
    @Mock private ProfileState.Active active;

    @Mock private Profile mockProfile;
    @Mock private Avatar mockAvatar;

    @Test
    @DisplayName("profile が NotExist で例外")
    void test_ProfileNotExist_throwDomainIllegalArgumentException() {
        when(profile_StateResolver.resolve(any())).thenReturn(notExist);
        assertThatThrownBy(() -> profileAvatar_StateResolver.resolve(UUID.randomUUID()))
            .isInstanceOf(DomainIllegalStateException.class)
            .hasMessage(DomainErrorMessage.BAD_REQUEST.getKey());
        verify(profile_StateResolver).resolve(any());
    }

    @Test
    @DisplayName("profile が NotActive かつ avatar が null で ProfileNotActive_AvatarNotExist")
    void test_ProfileNotActive_andHasNoAvatar_returnProfileNotActive_AvatarNotExist() {
        when(profile_StateResolver.resolve(any())).thenReturn(notActive);
        when(notActive.profile()).thenReturn(mockProfile);
        when(notActive.profile().getAvatar()).thenReturn(null);
        ProfileAvatarState result = profileAvatar_StateResolver.resolve(UUID.randomUUID());
        assertThat(result).isInstanceOf(ProfileAvatarState.ProfileNotActive_AvatarNotExist.class);
        verify(profile_StateResolver).resolve(any());
    }

    @Test
    @DisplayName("profile が NotActive かつ avatar が any で ProfileNotActive_AvatarExist")
    void test_ProfileNotActive_andHasAvatar_returnProfileNotActive_AvatarExist() {
        when(profile_StateResolver.resolve(any())).thenReturn(notActive);
        when(notActive.profile()).thenReturn(mockProfile);
        when(notActive.profile().getAvatar()).thenReturn(mockAvatar);
        ProfileAvatarState result = profileAvatar_StateResolver.resolve(UUID.randomUUID());
        assertThat(result).isInstanceOf(ProfileAvatarState.ProfileNotActive_AvatarExist.class);
        verify(profile_StateResolver).resolve(any());
    }

    @Test
    @DisplayName("profile が Active かつ avatar が null で ProfileActive_AvatarNotExist")
    void test_ProfileActive_andHasNoAvatar_returnProfileActive_AvatarNotExist() {
        when(profile_StateResolver.resolve(any())).thenReturn(active);
        when(active.profile()).thenReturn(mockProfile);
        when(active.profile().getAvatar()).thenReturn(null);
        ProfileAvatarState result = profileAvatar_StateResolver.resolve(UUID.randomUUID());
        assertThat(result).isInstanceOf(ProfileAvatarState.ProfileActive_AvatarNotExist.class);
        verify(profile_StateResolver).resolve(any());
    }

    @Test
    @DisplayName("profile が Active かつ avatar が any で ProfileActive_AvatarExist")
    void test_ProfileActive_andHasAvatar_returnProfileActive_AvatarExist() {
        when(profile_StateResolver.resolve(any())).thenReturn(active);
        when(active.profile()).thenReturn(mockProfile);
        when(active.profile().getAvatar()).thenReturn(mockAvatar);
        ProfileAvatarState result = profileAvatar_StateResolver.resolve(UUID.randomUUID());
        assertThat(result).isInstanceOf(ProfileAvatarState.ProfileActive_AvatarExist.class);
        verify(profile_StateResolver).resolve(any());
    }
}