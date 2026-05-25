package com.example.portfolio_simple_spring_mvc.domain.state;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;

public sealed interface ProfileState extends State
    permits ProfileState.NotExist,
            ProfileState.NotActive,
            ProfileState.Active
{
    record NotExist() implements ProfileState {}
    record NotActive(Profile profile) implements ProfileState {}
    record Active(Profile profile) implements ProfileState {}
}