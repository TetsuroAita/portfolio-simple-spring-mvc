package com.example.portfolio_simple_spring_mvc.application.handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileAvatarCommand;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;

import com.example.portfolio_simple_spring_mvc.domain.plan.SelectProfileAvatarPlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.port.storage.AvatarStorageClient;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;

@Service
public class SelectProfileAvatarHandler implements CommandHandler<String, ProfileAvatarCommand.Select> {
    private final StateResolver<ProfileAvatarState> stateResolver;
    private final Planner<SelectProfileAvatarPlan, ProfileAvatarState> planner;
    private final AvatarStorageClient avatarStorageClient;
    private final HandledResultFactory handledResultFactory;

    public SelectProfileAvatarHandler(
        StateResolver<ProfileAvatarState> stateResolver,
        Planner<SelectProfileAvatarPlan, ProfileAvatarState> planner,
        @Qualifier("avatarStorageClientOfSupabase") AvatarStorageClient avatarStorageClient,
        HandledResultFactory handledResultFactory
    ) {
        this.stateResolver = stateResolver;
        this.planner = planner;
        this.avatarStorageClient = avatarStorageClient;
        this.handledResultFactory = handledResultFactory;
    }

    @Transactional(readOnly = true)
    @Override
    public HandledResult<String> handle(ProfileAvatarCommand.Select command) {
        ProfileAvatarState state = stateResolver.resolve(command.base().profileId());

        SelectProfileAvatarPlan plan = planner.createPlan(state);

        String avatarUrl = avatarStorageClient.selectAvatar(
            plan.profile().getAvatar().generatePath()
        );
        
        return handledResultFactory.of(
            HandledResultMessage.AVATAR_SELECTED, 
            avatarUrl
        );
    }

    @Override
    public Class<? extends CommandHandler<?, ?>> getHandlerType() {
        return SelectProfileAvatarHandler.class;
    }
}