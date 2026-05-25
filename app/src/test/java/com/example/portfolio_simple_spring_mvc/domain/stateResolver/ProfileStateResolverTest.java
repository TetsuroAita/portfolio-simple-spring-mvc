package com.example.portfolio_simple_spring_mvc.domain.stateResolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.reader.ProfileReader;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;

@ExtendWith(MockitoExtension.class)
public class ProfileStateResolverTest {
    @Mock
    private ProfileReader profileReader;

    @InjectMocks
    private ProfileStateResolver stateResolver;

    @Mock
    private Profile mockProfile;

    @Test
    @DisplayName("null を渡すと例外")
    void test_givenNull_throwDomainIllegalArgumentException() {
        assertThatThrownBy(() -> stateResolver.resolve(null))
            .isInstanceOf(DomainIllegalStateException.class)
            .hasMessage(DomainErrorMessage.BAD_REQUEST.getKey());
    }

    @Test
    @DisplayName("profile が見つからない")
    void test_givenUUID_returnNotExist() {
        when(profileReader.selectProfile(any())).thenReturn(Optional.empty());
        ProfileState result = stateResolver.resolve(UUID.randomUUID());
        assertThat(result).isInstanceOf(ProfileState.NotExist.class);
        verify(profileReader).selectProfile(any());
    }

    @Test
    @DisplayName("profile が NotActive")
    void test_givenUUID_returnNotActive() {
        when(profileReader.selectProfile(any())).thenReturn(Optional.of(mockProfile));
        when(mockProfile.activeStatus()).thenReturn(false);
        ProfileState result = stateResolver.resolve(UUID.randomUUID());
        assertThat(result).isInstanceOf(ProfileState.NotActive.class);
        verify(profileReader).selectProfile(any());
    }

    @Test
    @DisplayName("profile が Active")
    void test_givenUUID_returnActive() {
        when(profileReader.selectProfile(any())).thenReturn(Optional.of(mockProfile));
        when(mockProfile.activeStatus()).thenReturn(true);
        ProfileState result = stateResolver.resolve(UUID.randomUUID());
        assertThat(result).isInstanceOf(ProfileState.Active.class);
        verify(profileReader).selectProfile(any());
    }
}
