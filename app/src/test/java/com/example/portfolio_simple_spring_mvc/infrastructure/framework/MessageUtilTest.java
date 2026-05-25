package com.example.portfolio_simple_spring_mvc.infrastructure.framework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.example.portfolio_simple_spring_mvc.Utf8ResourceBundle;
import com.example.portfolio_simple_spring_mvc.domain.exception.DomainErrorMessage;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;

@ExtendWith(MockitoExtension.class)
public class MessageUtilTest {
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MessageUtil messageUtil;

    @Test
    void test_givenSomeKey_returnCollectMessage() {
        when(messageSource.getMessage(
            DomainErrorMessage.BAD_REQUEST.getKey(),
            new Object[0],
            LocaleContextHolder.getLocale()
        ))
        .thenReturn(
            Utf8ResourceBundle
                .getBundle("messages")
                .getString("bad_request")
        );

        String expected = "不正なリクエストです";

        String result = messageUtil.getMessage(DomainErrorMessage.BAD_REQUEST.getKey());
        
        assertThat(result).isEqualTo(expected);
    }
}
