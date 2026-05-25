package com.example.portfolio_simple_spring_mvc.domain.plan;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;

public record SelectProfileAvatarPlan(
    Profile profile
) implements Plan {}
