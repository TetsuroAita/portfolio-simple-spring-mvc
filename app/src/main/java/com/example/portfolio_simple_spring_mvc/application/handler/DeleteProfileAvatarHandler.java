package com.example.portfolio_simple_spring_mvc.application.handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileAvatarCommand;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.domain.event.ProfileAvatarChangedEvent;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.DeleteProfileAvatarPlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.AvatarWriter;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;

@Service
public class DeleteProfileAvatarHandler implements CommandHandler<Void, ProfileAvatarCommand.Delete> {
    private ApplicationEventPublisher publisher;
    private final StateResolver<ProfileAvatarState> stateResolver;
    private final Planner<DeleteProfileAvatarPlan, ProfileAvatarState> planner;
    private final AvatarWriter avatarWriter;
    private final HandledResultFactory handledResultFactory;

    public DeleteProfileAvatarHandler(
        ApplicationEventPublisher publisher,
        StateResolver<ProfileAvatarState> stateResolver,
        Planner<DeleteProfileAvatarPlan, ProfileAvatarState> planner,
        @Qualifier("avatarWriterOfJpaRepo") AvatarWriter avatarWriter,
        HandledResultFactory handledResultFactory
    ) {
        this.publisher = publisher;
        this.stateResolver = stateResolver;
        this.planner = planner;
        this.avatarWriter = avatarWriter;
        this.handledResultFactory = handledResultFactory;
    }

    @Transactional
    @Override
    public HandledResult<Void> handle(ProfileAvatarCommand.Delete command) {
        ProfileAvatarState state = stateResolver.resolve(command.base().profileId());

        DeleteProfileAvatarPlan plan = planner.createPlan(state);

        Profile profile = plan.profile();
        Avatar avatar = plan.profile().getAvatar();

        avatarWriter.updateAvatar(avatar.inactivate());

        publisher.publishEvent(new ProfileAvatarChangedEvent(profile, null));

        return handledResultFactory.of(HandledResultMessage.AVATAR_DELETED);
    }

    @Override
    public Class<? extends CommandHandler<?, ?>> getHandlerType() {
        return DeleteProfileAvatarHandler.class;
    }
}