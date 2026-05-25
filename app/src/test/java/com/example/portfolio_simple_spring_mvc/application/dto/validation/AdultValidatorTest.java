package com.example.portfolio_simple_spring_mvc.application.dto.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.application.dto.validation.validator.AdultValidator;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;

import jakarta.validation.ConstraintValidatorContext;

@ExtendWith(MockitoExtension.class)
public class AdultValidatorTest {
    private MessageUtil messageUtil;
    private AdultValidator adultValidator;
    private ConstraintValidatorContext context;
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @BeforeEach
    void setUp() {
        messageUtil = mock(MessageUtil.class);
        adultValidator = new AdultValidator(messageUtil);
        context = mock(ConstraintValidatorContext.class);
        builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
    }

    @Test
    void isValid_givenAdultAge_shouldReturnTrue() {
        boolean result = adultValidator.isValid(LocalDate.now().minusYears(20), context);
        assertThat(result).isTrue();
    }

    @Test
    void isValid_givenUnderage_shouldReturnFalse() {
        when(messageUtil.getMessage(ValidationErrorMessage.ADULT.getKey())).thenReturn("AdultErrorMessage");
        when(context.buildConstraintViolationWithTemplate("AdultErrorMessage")).thenReturn(builder);
        boolean result = adultValidator.isValid(LocalDate.now().minusYears(20).plusDays(1), context);
        assertThat(result).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("AdultErrorMessage");
    }
}
