package com.example.portfolio_simple_spring_mvc.infrastructure.persistence.service.reader;

import org.springframework.stereotype.Component;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Sequence;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.reader.SequenceReader;
import com.example.portfolio_simple_spring_mvc.infrastructure.persistence.jpaRepository.SequenceRepository;

@Component("sequenceReaderOfJpaRepo")
public class SequenceReaderOfJpaRepo implements SequenceReader {
    private final SequenceRepository sequenceRepository;

    public SequenceReaderOfJpaRepo(
        SequenceRepository sequenceRepository
    ) {
        this.sequenceRepository = sequenceRepository;
    }

    @Override
    public Sequence selectSequence(String name) {
        return sequenceRepository.findByNameForUpdate(name);
    }
}