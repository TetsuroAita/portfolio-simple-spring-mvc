package com.example.portfolio_simple_spring_mvc.application.listener;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.portfolio_simple_spring_mvc.domain.event.ProfileDeletedEvent;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.AvatarWriter;

@Component
public class ProfileDeletedListener implements Listener {
    private final AvatarWriter avatarWriter;

    public ProfileDeletedListener(
        @Qualifier("avatarWriterOfJpaRepo") AvatarWriter avatarWriter
    ) {
        this.avatarWriter = avatarWriter;
    }

    @EventListener
    public void onProfileDeleted(ProfileDeletedEvent event) {
        if (event.profile() == null) {
            return;
        }

        if (event.profile().getAvatar() != null) {
            var avatar = event.profile().getAvatar();
            avatarWriter.updateAvatar(avatar.inactivate());
        }
    }
}