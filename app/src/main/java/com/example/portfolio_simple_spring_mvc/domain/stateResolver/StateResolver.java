package com.example.portfolio_simple_spring_mvc.domain.stateResolver;

import java.util.UUID;

import com.example.portfolio_simple_spring_mvc.domain.state.State;

public interface StateResolver<T extends State> {
    T resolve(UUID id);
}
