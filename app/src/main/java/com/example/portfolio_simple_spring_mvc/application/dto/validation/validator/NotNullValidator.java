package com.example.portfolio_simple_spring_mvc.application.dto.validation.validator;

import org.springframework.stereotype.Component;

import com.example.portfolio_simple_spring_mvc.application.dto.validation.ValidationErrorMessage;
import com.example.portfolio_simple_spring_mvc.application.dto.validation.annotation.NotNull;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class NotNullValidator implements ConstraintValidator<NotNull, Object> {
    private final MessageUtil messageUtil;

    public NotNullValidator(
        MessageUtil messageUtil
    ) {
        this.messageUtil = messageUtil;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                messageUtil.getMessage(ValidationErrorMessage.NOT_NULL.getKey())
            ).addConstraintViolation();
            return false;
        }

        return true;
    }

    
}
