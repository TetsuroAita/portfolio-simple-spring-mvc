package com.example.portfolio_simple_spring_mvc.infrastructure.persistence.jpaRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import com.example.portfolio_simple_spring_mvc.application.dto.profile.ProfileColumn;
import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Profile;

@DataJpaTest
@ActiveProfiles("test")
public class ProfileRepositoryTest {

    @Autowired private ProfileRepository profileRepository;

    private Profile profile;

    @BeforeEach
    void setup() {
        profile = Profile.createObjectForTest();
    }

    @Test
    @DisplayName("@PrePersistで, createdAt, updatedAtが永続化時にセットされる")
    void testPrePersist_onCreated() {
        Profile created = profileRepository.save(profile);
        
        assertNotNull(created.getCreatedAt());
        assertNotNull(created.getUpdatedAt());
        assertEquals(created.getCreatedAt(), created.getUpdatedAt());
    }

    

    @Test
    @DisplayName("@EmbeddedでFullNameが埋め込まれている")
    void testCreatePerson_embedded_FullName() {
        Profile created = profileRepository.save(profile);

        assertThat(created.getFullName().getLastName()).isEqualTo("山田");
        assertThat(created.getFullName().getFirstName()).isEqualTo("太郎");
        assertThat(created.getFullName().getLastNameKana()).isEqualTo("ヤマダ");
        assertThat(created.getFullName().getFirstNameKana()).isEqualTo("タロウ");
        assertThat(created.getFullName().getFullName()).isEqualTo("山田 太郎");
        assertThat(created.getFullName().getFullNameKana()).isEqualTo("ヤマダ タロウ");
    }

    @Test
    @DisplayName("アクティブ状態のものを指定のカラム名で昇順ソート")
    void test_selectProfiles_Activate_ByColumnName_ASC() {
        // personalNumberで昇順ソート
        Sort sort = Sort.by(ProfileColumn.PERSONAL_NUMBER.getColumnName()).ascending();
        Pageable pageable = PageRequest.of(0, 10, sort);

        Page<Profile> profiles = profileRepository.findByActiveTrue(pageable);

        List<String> expected = List.of("002", "005");
        List<String> result = profiles.stream().map(profile -> profile.getPersonalNumber()).toList();

        assertThat(result).isEqualTo(expected);
        assertThat(profiles.getSize()).isEqualTo(10);
        assertThat(profiles.getNumber()).isEqualTo(0);
        assertThat(profiles.getNumberOfElements()).isEqualTo(2);
        assertThat(profiles.getTotalPages()).isEqualTo(1);
        assertThat(profiles.getTotalElements()).isEqualTo(2);
        assertThat(profiles.hasNext()).isFalse();
        assertThat(profiles.hasPrevious()).isFalse();
    }

    @Test
    @DisplayName("非アクティブ状態のものを指定のカラム名で降順ソート")
    void test_selectProfiles_Inactivate_ByColumnName_DESC() {
        // lastNameKanaで降順ソート
        Sort sort = Sort.by(ProfileColumn.LAST_NAME_KANA.getColumnName()).descending();
        Pageable pageable = PageRequest.of(0, 10, sort);

        Page<Profile> profiles = profileRepository.findByActiveFalse(pageable);

        List<String> expected = List.of("003", "004", "001");
        List<String> result = profiles.stream().map(profile -> profile.getPersonalNumber()).toList();

        assertThat(result).isEqualTo(expected);
        assertThat(profiles.getSize()).isEqualTo(10);
        assertThat(profiles.getNumber()).isEqualTo(0);
        assertThat(profiles.getNumberOfElements()).isEqualTo(3);
        assertThat(profiles.getTotalPages()).isEqualTo(1);
        assertThat(profiles.getTotalElements()).isEqualTo(3);
        assertThat(profiles.hasNext()).isFalse();
        assertThat(profiles.hasPrevious()).isFalse();
    }

    @Test
    @DisplayName("返り値が空")
    void test_selectProfiles_retutnEmpty() {
        profileRepository.deleteAll();
        
        Sort sort = Sort.by(ProfileColumn.LAST_NAME_KANA.getColumnName()).descending();
        Pageable pageable = PageRequest.of(0, 10, sort);

        Page<Profile> profiles = profileRepository.findByActiveFalse(pageable);

        List<String> expected = List.of();
        List<String> result = profiles.stream().map(profile -> profile.getPersonalNumber()).toList();

        assertThat(result).isEqualTo(expected);
        assertThat(profiles.getSize()).isEqualTo(10);
        assertThat(profiles.getNumber()).isEqualTo(0);
        assertThat(profiles.getNumberOfElements()).isEqualTo(0);
        assertThat(profiles.getTotalPages()).isEqualTo(0);
        assertThat(profiles.getTotalElements()).isEqualTo(0);
        assertThat(profiles.hasNext()).isFalse();
        assertThat(profiles.hasPrevious()).isFalse();
    }
}