package com.example.portfolio_simple_spring_mvc.application.handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileCommand;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.application.util.ProfileMapper;

import com.example.portfolio_simple_spring_mvc.domain.plan.UpdateProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.ProfileWriter;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;

@Service
public class UpdateProfileHandler implements CommandHandler<Void, ProfileCommand.Update> {
    private final ProfileWriter profileWriter;
    private final StateResolver<ProfileState> stateResolver;
    private final Planner<UpdateProfilePlan, ProfileState> planner;
    private final HandledResultFactory handledResultFactory;

    public UpdateProfileHandler(
        @Qualifier("profileWriterOfJpaRepo") ProfileWriter profileWriter,
        StateResolver<ProfileState> stateResolver,
        Planner<UpdateProfilePlan, ProfileState> planner,
        HandledResultFactory handledResultFactory
    ) {
        this.profileWriter = profileWriter;
        this.stateResolver = stateResolver;
        this.planner = planner;
        this.handledResultFactory = handledResultFactory;
    }

    @Transactional
    @Override
    public HandledResult<Void> handle(ProfileCommand.Update command) {
        ProfileState state = stateResolver.resolve(command.profileId());

        UpdateProfilePlan plan = planner.createPlan(state);

        profileWriter.updateProfile(
            ProfileMapper.toUpdatedProfile(
                plan.profile(),
                command.requestDto()
            )
        );

        return handledResultFactory.of(HandledResultMessage.PROFILE_UPDATED);
    }

    @Override
    public Class<? extends CommandHandler<?, ?>> getHandlerType() {
        return UpdateProfileHandler.class;
    }
}
