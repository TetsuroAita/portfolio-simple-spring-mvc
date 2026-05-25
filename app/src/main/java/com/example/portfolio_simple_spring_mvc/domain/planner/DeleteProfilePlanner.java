package com.example.portfolio_simple_spring_mvc.domain.planner;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.domain.plan.DeleteProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;

public class DeleteProfilePlanner implements Planner<DeleteProfilePlan, ProfileState> {
    
    @Override
    public DeleteProfilePlan createPlan(ProfileState state) {
        return switch (state) {
            case ProfileState.NotExist s -> {
                throw new ProfileNotFoundException(DomainErrorMessage.PROFILE_NOT_FOUND);
            }

            case ProfileState.NotActive s -> {
                yield new DeleteProfilePlan(
                    s.profile(),
                    false,
                    true
                );
            }

            case ProfileState.Active s -> {
                yield new DeleteProfilePlan(
                    s.profile(),
                    true,
                    false
                );
            }
        };
    }
}
