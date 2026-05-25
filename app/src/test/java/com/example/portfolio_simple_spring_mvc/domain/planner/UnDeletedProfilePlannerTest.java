package com.example.portfolio_simple_spring_mvc.domain.planner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.UnDeletedProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState.NotActive;

public class UnDeletedProfilePlannerTest {
    private UnDeletedProfilePlanner planner =
        new UnDeletedProfilePlanner();

    @Test
    void test_stateIsNotExist_throwDomainIllegalStateException() {
        assertThatThrownBy(() -> planner.createPlan(mock(ProfileState.NotExist.class)))
            .isInstanceOf(ProfileNotFoundException.class);
    }

    @Test
    void test_stateIsNotActive_returnProfile_SelectPlan() {
        Profile mockProfile = mock(Profile.class);
        ProfileState.NotActive mockNotActive = new NotActive(mockProfile);
        UnDeletedProfilePlan expect = new UnDeletedProfilePlan(mockProfile);

        UnDeletedProfilePlan result = planner.createPlan(mockNotActive);

        assertThat(result).isEqualTo(expect);
    }

    @Test
    void test_stateIsActive_returnProfile_SelectPlan() {
        assertThatThrownBy(() -> planner.createPlan(mock(ProfileState.Active.class)))
            .isInstanceOf(DomainIllegalStateException.class);
    }
}
