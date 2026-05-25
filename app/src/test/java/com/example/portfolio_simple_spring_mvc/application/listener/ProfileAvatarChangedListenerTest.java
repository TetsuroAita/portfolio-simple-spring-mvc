package com.example.portfolio_simple_spring_mvc.application.listener;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.domain.event.ProfileAvatarChangedEvent;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.ProfileWriter;

@ExtendWith(MockitoExtension.class)
public class ProfileAvatarChangedListenerTest {
    @Mock private ProfileWriter profileWriter;
    @InjectMocks private ProfileAvatarChangedListener listener;

    @Test
    void test_givenProfileIsNull_doNothing() {
        var event = new ProfileAvatarChangedEvent(null, null);

        listener.onProfileAvatar_Changed(event);

        verifyNoInteractions(profileWriter);
    }

    @Test
    void test_givenProfile_doProfileWriterUpdate() {
        var profile = Profile.createObjectForTest();
        var event = new ProfileAvatarChangedEvent(profile, null);

        listener.onProfileAvatar_Changed(event);

        verify(profileWriter).updateProfile(profile);
    }
}
