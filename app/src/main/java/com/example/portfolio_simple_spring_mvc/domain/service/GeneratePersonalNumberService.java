package com.example.portfolio_simple_spring_mvc.domain.service;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Sequence;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.reader.SequenceReader;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.SequenceWriter;

public class GeneratePersonalNumberService {
    private final SequenceReader sequenceReader;
    private final SequenceWriter sequenceWriter;

    private final String SEQUENCE_NAME = "PERSONAL_NUMBER";

    public GeneratePersonalNumberService(
        SequenceReader sequenceReader,
        SequenceWriter sequenceWriter
    ) {
        this.sequenceReader = sequenceReader;
        this.sequenceWriter = sequenceWriter;
    }

    public String nextPersonalNumber() {
        Sequence sequence = sequenceReader.selectSequence(SEQUENCE_NAME);
        long next = sequence.getCurrentValue() + 1;
        sequence.setCurrentValue(next);
        sequenceWriter.updateSequence(sequence);
        
        return String.format("%03d", next);
    }
}