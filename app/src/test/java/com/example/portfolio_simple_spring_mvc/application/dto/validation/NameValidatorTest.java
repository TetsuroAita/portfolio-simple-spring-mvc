package com.example.portfolio_simple_spring_mvc.application.dto.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.portfolio_simple_spring_mvc.application.dto.validation.validator.NameValidator;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;

import jakarta.validation.ConstraintValidatorContext;

@ExtendWith(MockitoExtension.class)
public class NameValidatorTest {
    private MessageUtil messageUtil;
    private NameValidator nameValidator;
    private ConstraintValidatorContext context;
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @BeforeEach
    void setup() {
        messageUtil = mock(MessageUtil.class);
        nameValidator = new NameValidator(messageUtil);
        context = mock(ConstraintValidatorContext.class);
        builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
    }

    @Test
    void isValid_givenValidName_returnTrue() {
        boolean result = nameValidator.isValid("山田太郎", context);
        assertThat(result).isTrue();
    }

    @Test
    void isValid_givenBlank_returnFalse() {
        when(messageUtil.getMessage("not_blank")).thenReturn("error message");
        when(context.buildConstraintViolationWithTemplate("error message")).thenReturn(builder);
        boolean result = nameValidator.isValid(" ", context);
        assertThat(result).isFalse();
        verify(context).buildConstraintViolationWithTemplate(
            "error message"
        );
    }

    @Test
    void isValid_givenNull_returnFalse() {
        when(messageUtil.getMessage("not_blank")).thenReturn("error message");
        when(context.buildConstraintViolationWithTemplate("error message")).thenReturn(builder);

        boolean result = nameValidator.isValid(null, context);

        assertThat(result).isFalse();
        verify(context).buildConstraintViolationWithTemplate(
            "error message"
        );
    }

    @Test
    void isValid_givenTooLong_returnFalse() {
        when(messageUtil.getMessage("size10")).thenReturn("error message");
        when(context.buildConstraintViolationWithTemplate("error message")).thenReturn(builder);
        boolean result = nameValidator.isValid("太郎太郎太郎太郎太郎太郎太郎太郎太郎", context);
        assertThat(result).isFalse();
        verify(context).buildConstraintViolationWithTemplate(
            "error message"
        );
    }

    @Test
    void isValid_givenInvalidCharacters_returnFalse() {
        when(messageUtil.getMessage("only_japanese")).thenReturn("error message");
        when(context.buildConstraintViolationWithTemplate("error message")).thenReturn(builder);
        boolean result = nameValidator.isValid("John Doe", context);
        assertThat(result).isFalse();
        verify(context).buildConstraintViolationWithTemplate(
            "error message"
        );
    }
}
