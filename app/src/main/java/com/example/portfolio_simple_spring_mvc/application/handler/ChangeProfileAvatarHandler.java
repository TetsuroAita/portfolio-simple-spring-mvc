package com.example.portfolio_simple_spring_mvc.application.handler;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileAvatarCommand;
import com.example.portfolio_simple_spring_mvc.application.dto.filesource.FileSource;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;

import com.example.portfolio_simple_spring_mvc.domain.event.ProfileAvatarChangedEvent;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.ChangeProfileAvatarPlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.AvatarWriter;
import com.example.portfolio_simple_spring_mvc.domain.service.CreateNewAvatarService;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;
import com.example.portfolio_simple_spring_mvc.domain.validation.AvatarPolicy;

@Service
public class ChangeProfileAvatarHandler implements CommandHandler<Void, ProfileAvatarCommand.Change> {
    private final ApplicationEventPublisher publisher;
    private final StateResolver<ProfileAvatarState> stateResolver;
    private final Planner<ChangeProfileAvatarPlan, ProfileAvatarState> planner;
    private final AvatarPolicy avatarPolicy;
    private final CreateNewAvatarService createNewAvatarService;
    private final AvatarWriter avatarWriter;
    private final HandledResultFactory handledResultFactory;

    public ChangeProfileAvatarHandler(
        ApplicationEventPublisher publisher,
        StateResolver<ProfileAvatarState> stateResolver,
        Planner<ChangeProfileAvatarPlan, ProfileAvatarState> planner,
        @Qualifier("profileAvatarPolicy") AvatarPolicy avatarPolicy,
        CreateNewAvatarService createNewAvatarService,
        @Qualifier("avatarWriterOfJpaRepo") AvatarWriter avatarWriter,
        HandledResultFactory handledResultFactory
    ) {
        this.publisher = publisher;
        this.stateResolver = stateResolver;
        this.planner = planner;
        this.avatarPolicy = avatarPolicy;
        this.createNewAvatarService = createNewAvatarService;
        this.avatarWriter = avatarWriter;
        this.handledResultFactory = handledResultFactory;
    }

    @Transactional
    @Override
    public HandledResult<Void> handle(ProfileAvatarCommand.Change command) {
        
        UUID profileId = command.base().profileId();
        FileSource fileSource = command.fileSource();
        
        ProfileAvatarState state = stateResolver.resolve(profileId);
        
        ChangeProfileAvatarPlan plan = planner.createPlan(state);
        
        avatarPolicy.validate(fileSource, plan.profile().getAvatar());
        
        // 別トランザクション
        Avatar newAvatar = insertNewAvatar(fileSource);
        
        HandledResultMessage resultCode = HandledResultMessage.AVATAR_INSERTED;
        
        Profile profile = plan.profile();
        Avatar avatarForDelete = profile.getAvatar();

        if (avatarForDelete != null) {
            avatarWriter.updateAvatar(avatarForDelete.inactivate());
            resultCode = HandledResultMessage.AVATAR_UPDATED;
        }

        avatarWriter.updateAvatar(newAvatar.activate());

        publisher.publishEvent(new ProfileAvatarChangedEvent(profile, newAvatar));

        return handledResultFactory.of(resultCode);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private Avatar insertNewAvatar(FileSource fileSource) {
        return createNewAvatarService.createOf(fileSource);
    }

    @Override
    public Class<? extends CommandHandler<?, ?>> getHandlerType() {
        return ChangeProfileAvatarHandler.class;
    }
}