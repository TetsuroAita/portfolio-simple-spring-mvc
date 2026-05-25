package com.example.portfolio_simple_spring_mvc.domain.planner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.DeleteProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState.Active;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState.NotActive;

public class DeleteProfilePlannerTest {
    private DeleteProfilePlanner planner = new DeleteProfilePlanner();

    @Test
    void test_stateIsNoExist_throwDomainIllegalStateException() {
        assertThatThrownBy(() -> planner.createPlan(mock(ProfileState.NotExist.class)))
            .isInstanceOf(ProfileNotFoundException.class);
    }

    @Test
    void test_stateIsNotActive_returnProfile_DeletePlan() {
        Profile mockProfile = mock(Profile.class);
        ProfileState.NotActive mockProfile_State = new NotActive(mockProfile);
        DeleteProfilePlan expect = new DeleteProfilePlan(
            mockProfile,
            false,
            true
        );

        DeleteProfilePlan result = planner.createPlan(mockProfile_State);

        assertThat(result).isEqualTo(expect);
    }

    @Test
    void test_stateIsActive_returnProfile_DeletePlan() {
        Profile mockProfile = mock(Profile.class);
        ProfileState.Active mockProfile_State = new Active(mockProfile);
        DeleteProfilePlan expect = new DeleteProfilePlan(
            mockProfile,
            true,
            false
        );

        DeleteProfilePlan result = planner.createPlan(mockProfile_State);

        assertThat(result).isEqualTo(expect);
    }
}