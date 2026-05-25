package com.example.portfolio_simple_spring_mvc.domain.planner;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.domain.plan.UnDeletedProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;

public class UnDeletedProfilePlanner implements Planner<UnDeletedProfilePlan, ProfileState> {
    
    @Override
    public UnDeletedProfilePlan createPlan(ProfileState state) {
        return switch (state) {
            case ProfileState.NotExist s -> {
                throw new ProfileNotFoundException(DomainErrorMessage.PROFILE_NOT_FOUND);
            }

            case ProfileState.NotActive s -> {
                yield new UnDeletedProfilePlan(s.profile());
            }

            case ProfileState.Active s -> {
                throw new DomainIllegalStateException(DomainErrorMessage.BAD_REQUEST);
            }
        };
    }
}
