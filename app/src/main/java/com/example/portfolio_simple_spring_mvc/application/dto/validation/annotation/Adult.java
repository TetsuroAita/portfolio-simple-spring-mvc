package com.example.portfolio_simple_spring_mvc.application.dto.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.portfolio_simple_spring_mvc.application.dto.validation.validator.AdultValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = AdultValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Adult {
    
    String message() default "20歳未満は登録できません";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
