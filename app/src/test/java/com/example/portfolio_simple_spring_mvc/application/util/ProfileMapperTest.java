package com.example.portfolio_simple_spring_mvc.application.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.portfolio_simple_spring_mvc.application.dto.profile.Birthplace;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.Gender;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.RequestDto;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.ResponseDto;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.model.value.FullName;

public class ProfileMapperTest {
    private Profile profile;
    private RequestDto requestDto;
    private ResponseDto responseDto;

    @BeforeEach
    void setup() {
        profile = Profile.createObjectForTest();
        requestDto = RequestDto.createObjectForTest();
        responseDto = ResponseDto.createObjectForTest();
    }
    
    @Test
    void toResponseDto_test() {
        ResponseDto correct = responseDto;
        ResponseDto result = ProfileMapper.toResponseDto(profile);
        
        assertThat(result).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt").isEqualTo(correct);
    }

    @Test
    void toNewProfile_test() {
        Profile correct = profile;
        Profile result = ProfileMapper.toNewProfile(requestDto, "001");
        
        assertThat(result).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt").isEqualTo(correct);
    }

    @Test
    void toUpdatedProfile_test() {
        RequestDto requestDto_forUpdate = new RequestDto("安田", "太郎", "ヤスダ", "タロウ", Gender.MALE, LocalDate.of(2000, 05, 10), Birthplace.TOKYO, "");
        Profile correct = new Profile("001", new FullName("安田", "太郎", "ヤスダ", "タロウ"), Gender.MALE.name(), LocalDate.of(2000, 05, 10), Birthplace.TOKYO.name(), "");
        Profile result = ProfileMapper.toUpdatedProfile(profile, requestDto_forUpdate);

        assertThat(result).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt").isEqualTo(correct);
    }

    @Test
    void toRequestDto_test() {
        RequestDto correct = requestDto;
        RequestDto result = ProfileMapper.toRequestDto(profile);

        assertThat(result).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt").isEqualTo(correct);
    }
}
