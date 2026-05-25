package com.example.portfolio_simple_spring_mvc.domain.model.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.portfolio_simple_spring_mvc.ObjectMapperUtilForTest;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ProfileTest {
    
    @Test
    @DisplayName("正常系: 正しい値がフィールドに設定される")
    void testCreateProfile_success() throws JsonProcessingException {
        Profile profile = Profile.createObjectForTest();

        System.out.println(ObjectMapperUtilForTest.of(profile));

        assertThat(profile.getPersonalNumber()).isEqualTo("001");
        assertThat(profile.getFullName().getFullName()).isEqualTo("山田 太郎");
        assertThat(profile.getGender()).isEqualTo("MALE");
        assertThat(profile.getDateOfBirth()).isEqualTo(LocalDate.now().minusYears(20));
        assertThat(profile.getBirthplace()).isEqualTo("TOKYO");
        assertThat(profile.getMemo()).isEqualTo("趣味は読書です。");
    }

    @Test
    @DisplayName("異常系: コンストラクタの引数にnullを設定でNPE発生")
    void testCreateProfile_null_throwException() {
        assertThatThrownBy(
            () -> new Profile(
                null, 
                null, 
                null, 
                null, 
                null, 
                null)
            )
            .isInstanceOf(NullPointerException.class);
    }
}
