package com.example.portfolio_simple_spring_mvc.application.domainModuleConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.portfolio_simple_spring_mvc.domain.plan.ChangeProfileAvatarPlan;
import com.example.portfolio_simple_spring_mvc.domain.plan.DeleteProfileAvatarPlan;
import com.example.portfolio_simple_spring_mvc.domain.plan.SelectProfileAvatarPlan;
import com.example.portfolio_simple_spring_mvc.domain.plan.DeleteProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.plan.SelectProfileForEditPlan;
import com.example.portfolio_simple_spring_mvc.domain.plan.SelectProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.plan.UnDeletedProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.plan.UpdateProfilePlan;
import com.example.portfolio_simple_spring_mvc.domain.planner.Planner;
import com.example.portfolio_simple_spring_mvc.domain.planner.ChangeProfileAvatarPlanner;
import com.example.portfolio_simple_spring_mvc.domain.planner.DeleteProfileAvatarPlanner;
import com.example.portfolio_simple_spring_mvc.domain.planner.SelectProfileAvatarPlanner;
import com.example.portfolio_simple_spring_mvc.domain.planner.DeleteProfilePlanner;
import com.example.portfolio_simple_spring_mvc.domain.planner.SelectProfileForEditPlanner;
import com.example.portfolio_simple_spring_mvc.domain.planner.SelectProfilePlanner;
import com.example.portfolio_simple_spring_mvc.domain.planner.UnDeletedProfilePlanner;
import com.example.portfolio_simple_spring_mvc.domain.planner.UpdateProfilePlanner;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;

@Configuration
public class PlannerConfig {
    
    // ===== Profile =====
    @Bean
    public Planner<SelectProfilePlan, ProfileState> selectProfilePlanner() {
        return new SelectProfilePlanner();
    }
    
    @Bean
    public Planner<SelectProfileForEditPlan, ProfileState> selectProfileForEditPlanner() {
        return new SelectProfileForEditPlanner();
    }
    
    @Bean
    public Planner<UpdateProfilePlan, ProfileState> updateProfilePlanner() {
        return new UpdateProfilePlanner();
    }
    
    @Bean
    public Planner<DeleteProfilePlan, ProfileState> deleteProfilePlanner() {
        return new DeleteProfilePlanner();
    }
    
    @Bean
    public Planner<UnDeletedProfilePlan, ProfileState> unDeletedProfilePlanner() {
        return new UnDeletedProfilePlanner();
    }

    // ===== ProfileAvatar =====
    @Bean
    public Planner<SelectProfileAvatarPlan, ProfileAvatarState> selectProfileAvatarPlanner() {
        return new SelectProfileAvatarPlanner();
    }

    @Bean
    public Planner<ChangeProfileAvatarPlan, ProfileAvatarState> changeProfileAvatarPlanner() {
        return new ChangeProfileAvatarPlanner();
    }

    @Bean
    public Planner<DeleteProfileAvatarPlan, ProfileAvatarState> deleteProfileAvatarPlanner() {
        return new DeleteProfileAvatarPlanner();
    }
}
