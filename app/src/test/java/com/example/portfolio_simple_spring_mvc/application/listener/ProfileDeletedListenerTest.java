package com.example.portfolio_simple_spring_mvc.application.listener;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.domain.event.ProfileDeletedEvent;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.AvatarWriter;

@ExtendWith(MockitoExtension.class)
public class ProfileDeletedListenerTest {
    @Mock private AvatarWriter avatarWriter;
    @InjectMocks private ProfileDeletedListener listener;

    @Test
    void test_ProfileIsNull() {
        var event = new ProfileDeletedEvent(null);

        listener.onProfileDeleted(event);

        verifyNoInteractions(avatarWriter);
    }

    @Test
    void test_AvatarIsNull() {
        Profile profile = Profile.createObjectForTest();
        var event = new ProfileDeletedEvent(profile);

        listener.onProfileDeleted(event);

        verifyNoInteractions(avatarWriter);
    }

    @Test
    void test_givenProfileHasAvatar() {
        Profile profile = Profile.createObjectForTest();
        profile.setAvatar(Avatar.createObjectForTest());
        var event = new ProfileDeletedEvent(profile);

        listener.onProfileDeleted(event);

        verify(avatarWriter).updateAvatar(
            event.profile().getAvatar().inactivate()
        );
    }
}
