package com.example.portfolio_simple_spring_mvc.domain.event;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Avatar;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;

public record ProfileAvatarChangedEvent(
    Profile profile,
    Avatar avatar
) implements Event {}
