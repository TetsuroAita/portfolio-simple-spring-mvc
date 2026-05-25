package com.example.portfolio_simple_spring_mvc.domain.planner;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.plan.SelectProfileAvatarPlan;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;

public class SelectProfileAvatarPlanner implements Planner<SelectProfileAvatarPlan, ProfileAvatarState> {
    
    @Override
    public SelectProfileAvatarPlan createPlan(ProfileAvatarState state) {
        return switch (state) {
            case ProfileAvatarState.ProfileNotActive_AvatarNotExist s -> {
                throw new DomainIllegalStateException(DomainErrorMessage.BAD_REQUEST);
            }

            case ProfileAvatarState.ProfileNotActive_AvatarExist s -> {
                yield new SelectProfileAvatarPlan(s.profile());
            }

            case ProfileAvatarState.ProfileActive_AvatarNotExist s -> {
                throw new DomainIllegalStateException(DomainErrorMessage.BAD_REQUEST);
            }

            case ProfileAvatarState.ProfileActive_AvatarExist s -> {
                yield new SelectProfileAvatarPlan(s.profile());
            }
        };
    }
}
