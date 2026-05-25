package com.example.portfolio_simple_spring_mvc.infrastructure.persistence.jpaRepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class AvatarRepositoryTest {
    @Autowired private AvatarRepository avatarRepository;

    @Test
    @DisplayName("非アクティブ状態の avatar を一覧で取得し、件数だけ確認")
    void test_selecteAvatarsInactivate() {
        long size = avatarRepository.findByActiveFalse().size();
        assertThat(size).isEqualTo(3L);
    }

    @Test
    @DisplayName("非アクティブ状態の avatar を削除し、削除件数が返る")
    void test_deleteAvatarFromDB() {
        long size = avatarRepository.deleteByActiveFalse();
        assertThat(size).isEqualTo(3L);
    }
}
