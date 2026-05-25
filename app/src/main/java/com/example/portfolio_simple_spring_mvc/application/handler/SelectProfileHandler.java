package com.example.portfolio_simple_spring_mvc.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileCommand;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.ResponseDto;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.application.util.ProfileMapper;

import com.example.portfolio_simple_spring_mvc.domain.plan.SelectProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;

@Service
public class SelectProfileHandler implements CommandHandler<ResponseDto, ProfileCommand.Select> {
    private final StateResolver<ProfileState> stateResolver;
    private final Planner<SelectProfilePlan, ProfileState> planner;
    private final HandledResultFactory handledResultFactory;

    public SelectProfileHandler(
        StateResolver<ProfileState> stateResolver,
        Planner<SelectProfilePlan, ProfileState> planner,
        HandledResultFactory handledResultFactory
    ) {
        this.stateResolver = stateResolver;
        this.planner = planner;
        this.handledResultFactory = handledResultFactory;
    }

    @Transactional(readOnly = true)
    @Override
    public HandledResult<ResponseDto> handle(ProfileCommand.Select command) {
        ProfileState state = stateResolver.resolve(command.profileId());

        SelectProfilePlan plan = planner.createPlan(state);

        ResponseDto responseDto = ProfileMapper.toResponseDto(plan.profile());

        return handledResultFactory.of(
            HandledResultMessage.PROFILE_SELECTED,
            responseDto
        );
    }

    @Override
    public Class<? extends CommandHandler<?, ?>> getHandlerType() {
        return SelectProfileHandler.class;
    }
}
