package com.example.portfolio_simple_spring_mvc.infrastructure.persistence.service.reader;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.model.pagination.DomainPageRequest;
import com.example.portfolio_simple_spring_mvc.domain.model.pagination.DomainPagedResult;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.reader.ProfileReader;
import com.example.portfolio_simple_spring_mvc.infrastructure.persistence.jpaRepository.ProfileRepository;

@Component("profileReaderOfJpaRepo")
public class ProfileReaderOfJpaRepo implements ProfileReader {
    private final ProfileRepository profileRepository;

    public ProfileReaderOfJpaRepo(
        ProfileRepository profileRepository
    ) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Profile> selectProfile(UUID profileId) {
        return profileRepository.findById(profileId);
    }

    @Override
    public DomainPagedResult<Profile> selectProfiles(boolean activity, DomainPageRequest domainPageRequest) {
        Sort sort = domainPageRequest.asc() ?
            Sort.by(domainPageRequest.sortBy()).ascending() : Sort.by(domainPageRequest.sortBy()).descending();

        Pageable pageable = PageRequest.of(domainPageRequest.page(), domainPageRequest.size(), sort);

        Page<Profile> result = activity ?
            profileRepository.findByActiveTrue(pageable) : profileRepository.findByActiveFalse(pageable);

        return new DomainPagedResult<>(
            result.getContent(),
            result.getNumber(),
            result.getTotalPages(),
            result.getTotalElements(),
            result.hasNext(),
            result.hasPrevious()
        );
    }
}
