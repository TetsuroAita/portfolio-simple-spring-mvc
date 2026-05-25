package com.example.portfolio_simple_spring_mvc.application.dto.validation.validator;

import org.springframework.stereotype.Component;

import com.example.portfolio_simple_spring_mvc.application.dto.validation.ValidationErrorMessage;
import com.example.portfolio_simple_spring_mvc.application.dto.validation.annotation.ValidName;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class NameValidator implements ConstraintValidator<ValidName, String> {
    private final MessageUtil messageUtil;

    public NameValidator(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                messageUtil.getMessage(ValidationErrorMessage.NOT_BLANK.getKey())
            )
            .addConstraintViolation();

            return false;
        }

        if (value.length() > 10) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                messageUtil.getMessage(ValidationErrorMessage.SIZE10.getKey())
            )
            .addConstraintViolation();

            return false;
        }

        if (!value.matches("^[\\p{IsHan}\\p{IsHiragana}\\p{IsKatakana}ー]+$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                messageUtil.getMessage(ValidationErrorMessage.ONLY_JAPANESE.getKey())
            )
            .addConstraintViolation();

            return false;
        }

        return true;
    }
}
