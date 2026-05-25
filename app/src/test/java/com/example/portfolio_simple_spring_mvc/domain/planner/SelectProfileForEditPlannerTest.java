package com.example.portfolio_simple_spring_mvc.domain.planner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.plan.SelectProfileForEditPlan;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState.Active;

public class SelectProfileForEditPlannerTest {
    private SelectProfileForEditPlanner planner =
        new SelectProfileForEditPlanner();

    @Test
    void test_stateIsNotExist_throwDomainIllegalStateException() {
        assertThatThrownBy(() -> planner.createPlan(mock(ProfileState.NotExist.class)))
            .isInstanceOf(ProfileNotFoundException.class);
    }

    @Test
    void test_stateIsNotActive_throwDomainIllegalStateException() {
        assertThatThrownBy(() -> planner.createPlan(mock(ProfileState.NotActive.class)))
            .isInstanceOf(DomainIllegalStateException.class);
    }

    @Test
    void test_stateIsActive_returnProfile_SelectForEditPlan() {
        Profile mockProfile = mock(Profile.class);
        ProfileState.Active mockProfile_State = new Active(mockProfile);
        SelectProfileForEditPlan expect = new SelectProfileForEditPlan(mockProfile);

        SelectProfileForEditPlan result = planner.createPlan(mockProfile_State);

        assertThat(result).isEqualTo(expect);
    }
}
