package com.example.portfolio_simple_spring_mvc.domain.planner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.DeleteProfileAvatarPlan;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState.ProfileActive_AvatarExist;

public class DeleteProfileAvatarPlannerTest {
    private DeleteProfileAvatarPlanner planner =
        new DeleteProfileAvatarPlanner();

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
    void test_stateIsProfileActiveHasNoAvatar_throwDomainIllegalStateException() {
        assertThatThrownBy(() -> planner.createPlan(
            mock(ProfileAvatarState.ProfileActive_AvatarNotExist.class)
        ))
        .isInstanceOf(DomainIllegalStateException.class);
    }

    @Test
    void test_stateIsProfileActiveHasAvatar_returnProfileAvatar_DeletePlan() {
        Profile mockProfile = mock(Profile.class);
        ProfileAvatarState.ProfileActive_AvatarExist profileActive_AvatarExist =
            new ProfileActive_AvatarExist(mockProfile);
        DeleteProfileAvatarPlan expect = new DeleteProfileAvatarPlan(mockProfile);

        DeleteProfileAvatarPlan result = planner.createPlan(profileActive_AvatarExist);

        assertThat(result).isEqualTo(expect);
    }
}
