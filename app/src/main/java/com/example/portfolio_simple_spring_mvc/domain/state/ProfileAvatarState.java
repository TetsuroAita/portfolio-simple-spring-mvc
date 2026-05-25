package com.example.portfolio_simple_spring_mvc.domain.state;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;

public sealed interface ProfileAvatarState extends State
    permits ProfileAvatarState.ProfileNotActive_AvatarNotExist,
            ProfileAvatarState.ProfileNotActive_AvatarExist,
            ProfileAvatarState.ProfileActive_AvatarNotExist,
            ProfileAvatarState.ProfileActive_AvatarExist
{
    Profile profile();
    record ProfileNotActive_AvatarNotExist(Profile profile) implements ProfileAvatarState {}
    record ProfileNotActive_AvatarExist(Profile profile) implements ProfileAvatarState {}
    record ProfileActive_AvatarNotExist(Profile profile) implements ProfileAvatarState {}
    record ProfileActive_AvatarExist(Profile profile) implements ProfileAvatarState {}
}
