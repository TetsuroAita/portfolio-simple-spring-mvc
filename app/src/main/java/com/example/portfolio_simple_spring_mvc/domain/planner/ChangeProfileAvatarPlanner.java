package com.example.portfolio_simple_spring_mvc.domain.planner;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.plan.ChangeProfileAvatarPlan;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;

public class ChangeProfileAvatarPlanner implements Planner<ChangeProfileAvatarPlan, ProfileAvatarState> {
    
    @Override
    public ChangeProfileAvatarPlan createPlan(ProfileAvatarState state) {
        return switch (state) {
            case ProfileAvatarState.ProfileNotActive_AvatarNotExist s -> {
                throw new DomainIllegalStateException(DomainErrorMessage.BAD_REQUEST);
            }

            case ProfileAvatarState.ProfileNotActive_AvatarExist s -> {
                throw new DomainIllegalStateException(DomainErrorMessage.BAD_REQUEST);
            }

            case ProfileAvatarState.ProfileActive_AvatarNotExist s -> {
                yield new ChangeProfileAvatarPlan(
                    s.profile()
                );
            }

            case ProfileAvatarState.ProfileActive_AvatarExist s -> {
                yield new ChangeProfileAvatarPlan(
                    s.profile()
                );
            }
        };
    }
}
