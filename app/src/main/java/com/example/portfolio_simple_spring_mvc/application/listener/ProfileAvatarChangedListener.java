package com.example.portfolio_simple_spring_mvc.application.listener;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.portfolio_simple_spring_mvc.domain.event.ProfileAvatarChangedEvent;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.ProfileWriter;

@Component
public class ProfileAvatarChangedListener implements Listener {
    private final ProfileWriter profileWriter;

    public ProfileAvatarChangedListener(
        @Qualifier("profileWriterOfJpaRepo") ProfileWriter profileWriter
    ) {
        this.profileWriter = profileWriter;
    }

    @EventListener
    public void onProfileAvatar_Changed(ProfileAvatarChangedEvent event) {
        if (event.profile() == null) {
            return;
        }

        var profile = event.profile();
        profile.setAvatar(event.avatar());

        profileWriter.updateProfile(profile);
    }
}