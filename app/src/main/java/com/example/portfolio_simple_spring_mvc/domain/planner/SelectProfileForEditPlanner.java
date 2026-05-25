package com.example.portfolio_simple_spring_mvc.domain.planner;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.domain.plan.SelectProfileForEditPlan;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;

public class SelectProfileForEditPlanner implements Planner<SelectProfileForEditPlan, ProfileState> {
 
    @Override
    public SelectProfileForEditPlan createPlan(ProfileState state) {
        return switch (state) {
            case ProfileState.NotExist s -> {
                throw new ProfileNotFoundException(DomainErrorMessage.PROFILE_NOT_FOUND);
            }

            case ProfileState.NotActive s -> {
                throw new DomainIllegalStateException(DomainErrorMessage.BAD_REQUEST);
            }

            case ProfileState.Active s -> {
                yield new SelectProfileForEditPlan(s.profile());
            }
        };
    }
}