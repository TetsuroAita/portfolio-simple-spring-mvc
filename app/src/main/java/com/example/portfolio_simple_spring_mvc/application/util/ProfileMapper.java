package com.example.portfolio_simple_spring_mvc.application.util;

import com.example.portfolio_simple_spring_mvc.application.dto.profile.Birthplace;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.Gender;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.RequestDto;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.ResponseDto;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.example.portfolio_simple_spring_mvc.domain.model.value.FullName;

public final class ProfileMapper {

    private ProfileMapper() {}
    
    //Person -> ResponseDto
    public static ResponseDto toResponseDto(Profile profile) {
        return new ResponseDto(
            profile.getId(),
            DateTimeUtil.formatDateTime(profile.getCreatedAt()),
            DateTimeUtil.formatDateTime(profile.getUpdatedAt()),
            profile.activeStatus(),
            profile.getPersonalNumber(),
            profile.getFullName().getFullName(), 
            profile.getFullName().getFullNameKana(),
            Gender.convert(profile.getGender()).getDisplayName(),
            DateTimeUtil.formatDate(profile.getDateOfBirth()),
            AgeCalculator.getAge(profile.getDateOfBirth()),
            Birthplace.convert(profile.getBirthplace()).getDisplayName(),
            profile.getMemo() != "" ? profile.getMemo() : "特になし",
            profile.getAvatar() != null ? true : false
        );
    }

    //RequestDto -> Person(新規)
    public static Profile toNewProfile(RequestDto requestDto, String profilealNumber) {
        return new Profile(
            profilealNumber,
            new FullName(requestDto.getLastName(), requestDto.getFirstName(), requestDto.getLastNameKana(), requestDto.getFirstNameKana()),
            requestDto.getGender().name(),
            requestDto.getDateOfBirth(),
            requestDto.getBirthplace().name(),
            requestDto.getMemo()
        );
    }

    //RequestDto -> Person(更新)
    public static Profile toUpdatedProfile(Profile profile, RequestDto requestDto) {
        profile.setFullName(new FullName(requestDto.getLastName(), requestDto.getFirstName(), requestDto.getLastNameKana(), requestDto.getFirstNameKana()));
        profile.setGender(requestDto.getGender().name());
        profile.setDateOfBirth(requestDto.getDateOfBirth());
        profile.setBirthplace(requestDto.getBirthplace().name());
        profile.setMemo(requestDto.getMemo());
        return profile;
    }

    //Person -> RequestDto
    public static RequestDto toRequestDto(Profile profile) {
        return new RequestDto(
            profile.getFullName().getLastName(),
            profile.getFullName().getFirstName(),
            profile.getFullName().getLastNameKana(),
            profile.getFullName().getFirstNameKana(),
            Gender.convert(profile.getGender()),
            profile.getDateOfBirth(),
            Birthplace.convert(profile.getBirthplace()),
            profile.getMemo()
        );
    }
}
