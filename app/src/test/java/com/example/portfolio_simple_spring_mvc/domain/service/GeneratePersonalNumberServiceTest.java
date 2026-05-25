package com.example.portfolio_simple_spring_mvc.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.domain.model.Entity.Sequence;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.reader.SequenceReader;
import com.example.portfolio_simple_spring_mvc.domain.port.repository.writer.SequenceWriter;

@ExtendWith(MockitoExtension.class)
public class GeneratePersonalNumberServiceTest {

    @Mock
    private SequenceReader sequenceReader;

    @Mock
    private SequenceWriter sequenceWriter;
    
    @InjectMocks
    private GeneratePersonalNumberService generatePersonalNumberService;

    @Test
    @DisplayName("正常系: nextPersonalNumber()実行時に正しく値がインクリメントされること")
    void testNextPersonalNumber_success() {
        Sequence sequence = mock(Sequence.class);
        long currentValue = 1L;
        when(sequenceReader.selectSequence("PERSONAL_NUMBER")).thenReturn(sequence);
        when(sequence.getCurrentValue()).thenReturn(currentValue);
        
        String expect = "002";
        String result = generatePersonalNumberService.nextPersonalNumber();

        assertThat(result).isEqualTo(expect);
    }
}