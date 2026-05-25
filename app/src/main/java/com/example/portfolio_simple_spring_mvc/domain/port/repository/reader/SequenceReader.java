package com.example.portfolio_simple_spring_mvc.domain.port.repository.reader;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Sequence;

public interface SequenceReader {
    Sequence selectSequence(String name);
}
