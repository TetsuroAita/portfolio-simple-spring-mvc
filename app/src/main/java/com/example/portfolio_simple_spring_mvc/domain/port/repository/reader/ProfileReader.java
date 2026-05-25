package com.example.portfolio_simple_spring_mvc.domain.port.repository.reader;

import java.util.Optional;
import java.util.UUID;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.model.pagination.DomainPageRequest;
import com.example.portfolio_simple_spring_mvc.domain.model.pagination.DomainPagedResult;

public interface ProfileReader {
    Optional<Profile> selectProfile(UUID profileId);
    DomainPagedResult<Profile> selectProfiles(boolean activity, DomainPageRequest domainPageRequest);
}