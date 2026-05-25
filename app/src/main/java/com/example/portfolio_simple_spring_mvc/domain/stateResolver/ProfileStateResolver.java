package com.example.portfolio_simple_spring_mvc.domain.stateResolver;

import java.util.Optional;
import java.util.UUID;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.reader.ProfileReader;
import com.example.portfolio_simple_spring_mvc.domain.state.ProfileState;


public class ProfileStateResolver implements StateResolver<ProfileState> {
    private final ProfileReader profileReader;

    public ProfileStateResolver(
        ProfileReader profileReader
    ) {
        this.profileReader = profileReader;
    }
    
    @Override
    public ProfileState resolve(UUID profileId) {
        if (profileId == null) {
            throw new DomainIllegalStateException(DomainErrorMessage.BAD_REQUEST);
        }

        Optional<Profile> optionalProfile = profileReader.selectProfile(profileId);

        if (optionalProfile.isEmpty()) {
            return new ProfileState.NotExist();
        }

        Profile profile = optionalProfile.get();

        if (profile.activeStatus() == false) {
            return new ProfileState.NotActive(profile);
        }

        return new ProfileState.Active(profile);
    }
}
