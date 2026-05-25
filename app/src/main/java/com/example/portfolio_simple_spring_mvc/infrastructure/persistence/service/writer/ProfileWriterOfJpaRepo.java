package com.example.portfolio_simple_spring_mvc.infrastructure.persistence.service.writer;

import org.springframework.stereotype.Component;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.ProfileWriter;
import com.example.portfolio_simple_spring_mvc.infrastructure.persistence.jpaRepository.ProfileRepository;

@Component("profileWriterOfJpaRepo")
public class ProfileWriterOfJpaRepo implements ProfileWriter {
    private final ProfileRepository profileRepository;

    public ProfileWriterOfJpaRepo(
        ProfileRepository profileRepository
    ) {
        this.profileRepository = profileRepository;
    }

    @Override
    public void insertProfile(Profile profile) {
        profileRepository.save(profile);
    }

    @Override
    public void updateProfile(Profile profile) {
        profileRepository.save(profile);
    }

    @Override
    public void deleteProfile(Profile profile) {
        profileRepository.delete(profile);
    }
}
