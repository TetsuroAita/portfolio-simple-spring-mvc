package com.example.portfolio_simple_spring_mvc.infrastructure.persistence.service.writer;

import org.springframework.stereotype.Component;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Sequence;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.SequenceWriter;
import com.example.portfolio_simple_spring_mvc.infrastructure.persistence.jpaRepository.SequenceRepository;

@Component("sequenceWriterOfJpaRepo")
public class SequenceWriterOfJpaRepo implements SequenceWriter {
    private final SequenceRepository sequenceRepository;

    public SequenceWriterOfJpaRepo(
        SequenceRepository sequenceRepository
    ) {
        this.sequenceRepository = sequenceRepository;
    }

    @Override
    public void updateSequence(Sequence sequence) {
        sequenceRepository.save(sequence);
    }
}
