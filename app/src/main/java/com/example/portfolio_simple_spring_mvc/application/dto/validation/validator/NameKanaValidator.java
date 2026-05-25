package com.example.portfolio_simple_spring_mvc.application.dto.validation.validator;

import org.springframework.stereotype.Component;

import com.example.portfolio_simple_spring_mvc.application.dto.validation.ValidationErrorMessage;
import com.example.portfolio_simple_spring_mvc.application.dto.validation.annotation.ValidNameKana;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class NameKanaValidator implements ConstraintValidator<ValidNameKana, String> {
    private final MessageUtil messageUtil;

    public NameKanaValidator(MessageUtil messageUtil) {
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
            context.buildConstraintViolationWithTemplate(messageUtil.getMessage(ValidationErrorMessage.SIZE10.getKey()))
                .addConstraintViolation();
            return false;
        }

        // カタカナと長音符以外の文字が含まれている場合はエラー
        if (!value.matches("^[\\u30A0-\\u30FFー]+$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageUtil.getMessage(ValidationErrorMessage.ONLY_KATAKANA.getKey()))
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}
