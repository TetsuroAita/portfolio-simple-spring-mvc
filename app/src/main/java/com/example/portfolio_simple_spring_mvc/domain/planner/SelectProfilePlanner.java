package com.example.portfolio_simple_spring_mvc.domain.planner;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.domain.plan.SelectProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;

public class SelectProfilePlanner implements Planner<SelectProfilePlan, ProfileState> {
    
    @Override
    public SelectProfilePlan createPlan(ProfileState state) {
        return switch (state) {
            case ProfileState.NotExist s -> {
                throw new ProfileNotFoundException(DomainErrorMessage.PROFILE_NOT_FOUND);
            }

            case ProfileState.NotActive s -> {
                yield new SelectProfilePlan(s.profile());
            }

            case ProfileState.Active s -> {
                yield new SelectProfilePlan(s.profile());
            }
        };
    }
}
