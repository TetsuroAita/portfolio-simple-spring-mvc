package com.example.portfolio_simple_spring_mvc.domain.event;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;

public record ProfileDeletedEvent(
    Profile profile
) implements Event {}
