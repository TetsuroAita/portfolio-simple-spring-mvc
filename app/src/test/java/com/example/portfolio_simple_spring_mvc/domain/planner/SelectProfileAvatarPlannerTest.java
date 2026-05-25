package com.example.portfolio_simple_spring_mvc.domain.planner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.SelectProfileAvatarPlan;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState.ProfileActive_AvatarExist;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState.ProfileNotActive_AvatarExist;

public class SelectProfileAvatarPlannerTest {
    private SelectProfileAvatarPlanner planner =
        new SelectProfileAvatarPlanner();

    @Test
    void test_stateIsProfileNotActiveHasNoAvatar_throwDomainIllegalStateException() {
        assertThatThrownBy(() -> planner.createPlan(
            mock(ProfileAvatarState.ProfileNotActive_AvatarNotExist.class)
        ))
        .isInstanceOf(DomainIllegalStateException.class);
    }

    @Test
    void test_stateIsProfileNotActiveHasAvatar_returnProfileAvatar_SelectPlan() {
        Profile mockProfile = mock(Profile.class);
        ProfileAvatarState.ProfileNotActive_AvatarExist mockNotActive_AvatarExist =
            new ProfileNotActive_AvatarExist(mockProfile);
        SelectProfileAvatarPlan expect = new SelectProfileAvatarPlan(mockProfile);

        SelectProfileAvatarPlan result = planner.createPlan(mockNotActive_AvatarExist);

        assertThat(result).isEqualTo(expect);
    }

    @Test
    void test_stateIsProfileActiveHasNoAvatar_throwDomainIllegalStateException() {
        assertThatThrownBy(() -> planner.createPlan(
            mock(ProfileAvatarState.ProfileActive_AvatarNotExist.class)
        ))
        .isInstanceOf(DomainIllegalStateException.class);
    }

    @Test
    void test_stateIsProfileActiveHasAvatar_returnProfileAvatar_SelectPlan() {
        Profile mockProfile = mock(Profile.class);
        ProfileAvatarState.ProfileActive_AvatarExist mockActive_AvatarExist =
            new ProfileActive_AvatarExist(mockProfile);
        SelectProfileAvatarPlan expect = new SelectProfileAvatarPlan(mockProfile);

        SelectProfileAvatarPlan result = planner.createPlan(mockActive_AvatarExist);

        assertThat(result).isEqualTo(expect);
    }
}
