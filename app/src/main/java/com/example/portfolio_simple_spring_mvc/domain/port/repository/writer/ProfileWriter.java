package com.example.portfolio_simple_spring_mvc.domain.port.repository.writer;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;

public interface ProfileWriter {
    void insertProfile(Profile profile);
    void updateProfile(Profile profile);
    void deleteProfile(Profile profile);
}
