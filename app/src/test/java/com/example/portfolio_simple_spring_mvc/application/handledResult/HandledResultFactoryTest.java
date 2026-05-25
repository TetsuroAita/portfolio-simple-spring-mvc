package com.example.portfolio_simple_spring_mvc.application.handledResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.application.dto.profile.ResponseDto;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;

@ExtendWith(MockitoExtension.class)
public class HandledResultFactoryTest {
    private HandledResultFactory handledResultFactory;

    @Mock
    private MessageUtil messageUtil;

    @BeforeEach
    void setup() {
        handledResultFactory = new HandledResultFactory(messageUtil);
    }

    @Test
    @DisplayName("null を渡すと例外発生")
    void test_givenNull_returnException() {
        assertThatThrownBy(() -> handledResultFactory.of(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("HandledResultMessage is null.");
    }

    @Test
    @DisplayName("メッセージキーのみ渡した時は『message』のみ生成")
    void test_givenKey_returnCollectMessage() {
        when(messageUtil.getMessage(HandledResultMessage.PROFILE_SELECTED.getKey())).thenReturn("selected");

        HandledResult<Void> result = handledResultFactory.of(HandledResultMessage.PROFILE_SELECTED);

        assertThat(result.message()).isEqualTo("selected");
        assertThat(result.data()).isEqualTo(null);
        verify(messageUtil).getMessage(HandledResultMessage.PROFILE_SELECTED.getKey());
    }

    @Test
    @DisplayName("メッセージキーとオブジェクトを渡した時は『message』とオブジェクトのキーを生成")
    void test_giveKeyAndSomeObject_returnCollectMessage() {
        when(messageUtil.getMessage(HandledResultMessage.PROFILE_SELECTED.getKey())).thenReturn("selected");
        ResponseDto responseDto = ResponseDto.createObjectForTest();

        HandledResult<ResponseDto> result = handledResultFactory.of(HandledResultMessage.PROFILE_SELECTED, responseDto);

        assertThat(result.message()).isEqualTo("selected");
        assertThat(result.data()).isEqualTo(responseDto);
        verify(messageUtil).getMessage(HandledResultMessage.PROFILE_SELECTED.getKey());
    }
}
