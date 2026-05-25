package com.example.portfolio_simple_spring_mvc.application.dto.validation.validator;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import com.example.portfolio_simple_spring_mvc.application.dto.validation.ValidationErrorMessage;
import com.example.portfolio_simple_spring_mvc.application.dto.validation.annotation.Adult;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {
    private final MessageUtil messageUtil;

    public AdultValidator(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        // null なら not null であることを検証するアノテーションで検証されるため、true を返す
        if(value == null) return true;

        if(Period.between(value, LocalDate.now()).getYears() < 20) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                messageUtil.getMessage(ValidationErrorMessage.ADULT.getKey())
            )
            .addConstraintViolation();
            return false;
        }

        return true;
    }
}
