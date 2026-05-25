package com.example.portfolio_simple_spring_mvc.domain.planner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.ChangeProfileAvatarPlan;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState.ProfileActive_AvatarExist;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState.ProfileActive_AvatarNotExist;

public class ChangeProfileAvatarPlannerTest {
    private ChangeProfileAvatarPlanner planner =
        new ChangeProfileAvatarPlanner();

    @Test
    void test_stateIsProfileNotActiveHasNoAvatar_throwDomainIllegalStateException() {
        assertThatThrownBy(() -> planner.createPlan(
            mock(ProfileAvatarState.ProfileNotActive_AvatarNotExist.class)
        ))
        .isInstanceOf(DomainIllegalStateException.class);
    }

    @Test
    void test_stateIsProfileNotActiveHasAvatar_throwDomainIllegalStateException() {
        assertThatThrownBy(() -> planner.createPlan(
            mock(ProfileAvatarState.ProfileNotActive_AvatarExist.class)
        ))
        .isInstanceOf(DomainIllegalStateException.class);
    }

    @Test
    void test_stateIsProfileActiveHasNoAvatar_returnProfileAvatar_ChangePlan() {
        Profile mockProfile = mock(Profile.class);
        ProfileAvatarState.ProfileActive_AvatarNotExist profileActive_AvatarNotExist =
            new ProfileActive_AvatarNotExist(mockProfile);
        ChangeProfileAvatarPlan expect = new ChangeProfileAvatarPlan(mockProfile);

        ChangeProfileAvatarPlan result = planner.createPlan(profileActive_AvatarNotExist);

        assertThat(result).isEqualTo(expect);
    }

    @Test
    void test_stateIsProfileActiveHasAvatar_returnProfileAvatar_ChangePlan() {
        Profile mockProfile = mock(Profile.class);
        ProfileAvatarState.ProfileActive_AvatarExist profileActive_AvatartExist =
            new ProfileActive_AvatarExist(mockProfile);
        ChangeProfileAvatarPlan expect = new ChangeProfileAvatarPlan(mockProfile);

        ChangeProfileAvatarPlan result = planner.createPlan(profileActive_AvatartExist);

        assertThat(result).isEqualTo(expect);
    }
}