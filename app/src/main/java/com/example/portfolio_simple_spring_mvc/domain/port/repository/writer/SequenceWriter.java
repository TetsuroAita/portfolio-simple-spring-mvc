package com.example.portfolio_simple_spring_mvc.domain.port.repository.writer;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Sequence;

public interface SequenceWriter {
    void updateSequence(Sequence sequence);
}
