package com.example.portfolio_simple_spring_mvc.domain.plan;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;

public record DeleteProfilePlan(
    Profile profile,
    boolean profile_Inactivate,
    boolean profile_Delete
) implements Plan {}
