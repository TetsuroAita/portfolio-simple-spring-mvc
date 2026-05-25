package com.example.portfolio_simple_spring_mvc.application.handler;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileCommand;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.domain.event.ProfileDeletedEvent;
import com.example.portfolio_simple_spring_mvc.domain.executor.DeleteProfileExecutor;
import com.example.portfolio_simple_spring_mvc.domain.plan.DeleteProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;

@Service
public class DeleteProfileHandler implements CommandHandler<Void, ProfileCommand.Delete> {
    private final ApplicationEventPublisher publisher;
    private final StateResolver<ProfileState> stateResolver;
    private final Planner<DeleteProfilePlan, ProfileState> planner;
    private final DeleteProfileExecutor executor;
    private final HandledResultFactory handledResultFactory;

    public DeleteProfileHandler(
        ApplicationEventPublisher publisher,
        StateResolver<ProfileState> stateResolver,
        Planner<DeleteProfilePlan, ProfileState> planner,
        DeleteProfileExecutor executor,
        HandledResultFactory handledResultFactory
    ) {
        this.publisher = publisher;
        this.stateResolver = stateResolver;
        this.planner = planner;
        this.executor = executor;
        this.handledResultFactory = handledResultFactory;
    }

    @Transactional
    @Override
    public HandledResult<Void> handle(ProfileCommand.Delete command) {
        ProfileState state = stateResolver.resolve(command.profileId());

        DeleteProfilePlan plan = planner.createPlan(state);

        ProfileDeletedEvent event = executor.execute(plan);

        if (event != null) {
            publisher.publishEvent(event);
        }

        return handledResultFactory.of(HandledResultMessage.PROFILE_DELETED);
    }

    @Override
    public Class<? extends CommandHandler<?, ?>> getHandlerType() {
        return DeleteProfileHandler.class;
    }  
}
