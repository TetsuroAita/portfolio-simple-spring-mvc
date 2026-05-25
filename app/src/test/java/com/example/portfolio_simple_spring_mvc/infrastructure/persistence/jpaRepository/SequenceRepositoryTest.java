package com.example.portfolio_simple_spring_mvc.infrastructure.persistence.jpaRepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Sequence;

@DataJpaTest
@ActiveProfiles("test")
public class SequenceRepositoryTest {

    @Autowired
    private SequenceRepository sequenceRepository;

    @Test
    @DisplayName("DBから指定したプライマリーキーを取得")
    void test_FindByNameForUpdate() {
        Sequence sequence = sequenceRepository.findByNameForUpdate("PERSONAL_NUMBER");
        assertThat(sequence.getName()).isEqualTo("PERSONAL_NUMBER");
    }
}