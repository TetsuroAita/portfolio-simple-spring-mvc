package com.example.portfolio_simple_spring_mvc.application.dto.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.application.dto.validation.validator.NameKanaValidator;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;

import jakarta.validation.ConstraintValidatorContext;

@ExtendWith(MockitoExtension.class)
public class NameKanaValidatorTest {
    private MessageUtil messageUtil;
    private NameKanaValidator validator;
    private ConstraintValidatorContext context;
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @BeforeEach
    void setUp() {
        messageUtil = mock(MessageUtil.class);
        validator = new NameKanaValidator(messageUtil);
        context = mock(ConstraintValidatorContext.class);
        builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
    }
    
    @Test
    void isValid_givenValidValue_shouldReturnTrue() {
        boolean result = validator.isValid("カタカナ", context);
        assertThat(result).isTrue();
    }

    @Test
    void isValid_givenNullValue_shouldReturnFalse() {
        when(messageUtil.getMessage("not_blank")).thenReturn("ErrorMessage");
        when(context.buildConstraintViolationWithTemplate("ErrorMessage")).thenReturn(builder);
        boolean result = validator.isValid(null, context);
        assertThat(result).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("ErrorMessage");
    }

    @Test
    void isValid_givenBlankValue_shouldReturnFalse() {
        when(messageUtil.getMessage("not_blank")).thenReturn("ErrorMessage");
        when(context.buildConstraintViolationWithTemplate("ErrorMessage")).thenReturn(builder);
        boolean result = validator.isValid("   ", context);
        assertThat(result).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("ErrorMessage");
    }

    @Test
    void isValid_givenTooLongValue_shouldReturnFalse() {
        when(messageUtil.getMessage("size10")).thenReturn("SizeErrorMessage");
        when(context.buildConstraintViolationWithTemplate("SizeErrorMessage")).thenReturn(builder);
        boolean result = validator.isValid("カタカナカタカナカタカナ", context);
        assertThat(result).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("SizeErrorMessage");
    }

    @Test
    void isValid_givenInvalidCharacters_shouldReturnFalse() {
        when(messageUtil.getMessage("only_katakana")).thenReturn("OnlyKatakanaErrorMessage");
        when(context.buildConstraintViolationWithTemplate("OnlyKatakanaErrorMessage")).thenReturn(builder);
        boolean result = validator.isValid("カタカナabc", context);
        assertThat(result).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("OnlyKatakanaErrorMessage");
    }
}
