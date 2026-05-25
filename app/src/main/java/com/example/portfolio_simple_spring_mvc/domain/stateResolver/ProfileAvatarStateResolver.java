package com.example.portfolio_simple_spring_mvc.domain.stateResolver;

import java.util.UUID;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileAvatarState;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;

public class ProfileAvatarStateResolver implements StateResolver<ProfileAvatarState> {
    private final StateResolver<ProfileState> stateResolver;

    public ProfileAvatarStateResolver(
        StateResolver<ProfileState> stateResolver
    ) {
        this.stateResolver = stateResolver;
    }

    @Override
    public ProfileAvatarState resolve(UUID profileId) {
        ProfileState state = stateResolver.resolve(profileId);

        return switch (state) {
            case ProfileState.NotExist s -> {
                throw new DomainIllegalStateException(DomainErrorMessage.BAD_REQUEST);
            }

            case ProfileState.NotActive s -> {
                if (s.profile().getAvatar() == null) {
                    yield new ProfileAvatarState.ProfileNotActive_AvatarNotExist(s.profile());
                }
                yield new ProfileAvatarState.ProfileNotActive_AvatarExist(s.profile());
            }

            case ProfileState.Active s -> {
                if (s.profile().getAvatar() == null) {
                    yield new ProfileAvatarState.ProfileActive_AvatarNotExist(s.profile());
                }
                yield new ProfileAvatarState.ProfileActive_AvatarExist(s.profile());
            }
        };
    }
}
