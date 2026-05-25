package com.example.portfolio_simple_spring_mvc.application.handler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.portfolio_simple_spring_mvc.application.command.ProfileCommand;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.RequestDto;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultFactory;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResultMessage;
import com.example.portfolio_simple_spring_mvc.application.util.ProfileMapper;

import com.example.portfolio_simple_spring_mvc.domain.plan.SelectProfileForEditPlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;
import com.example.portfolio_simple_spring_mvc.domain.stateResolver.StateResolver;

@Service
public class SelectProfileForEditHandler implements CommandHandler<RequestDto, ProfileCommand.Select_ForEdit> {
    private final StateResolver<ProfileState> stateResolver;
    private final Planner<SelectProfileForEditPlan, ProfileState> planner;
    private final HandledResultFactory handledResultFactory;

    public SelectProfileForEditHandler(
        StateResolver<ProfileState> stateResolver,
        Planner<SelectProfileForEditPlan, ProfileState> planner,
        HandledResultFactory handledResultFactory
    ) {
        this.stateResolver = stateResolver;
        this.planner = planner;
        this.handledResultFactory = handledResultFactory;
    }

    @Transactional(readOnly = true)
    @Override
    public HandledResult<RequestDto> handle(ProfileCommand.Select_ForEdit command) {
        ProfileState state = stateResolver.resolve(command.profileId());

        SelectProfileForEditPlan plan = planner.createPlan(state);

        RequestDto requestDto = ProfileMapper.toRequestDto(plan.profile());

        return handledResultFactory.of(
            HandledResultMessage.PROFILE_SELECTED_FOR_EDIT,
            requestDto
        );
    }

    @Override
    public Class<? extends CommandHandler<?, ?>> getHandlerType() {
        return SelectProfileForEditHandler.class;
    }
}
