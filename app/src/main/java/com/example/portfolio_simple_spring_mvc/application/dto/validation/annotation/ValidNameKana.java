package com.example.portfolio_simple_spring_mvc.application.dto.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.portfolio_simple_spring_mvc.application.dto.validation.validator.NameKanaValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = NameKanaValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNameKana {
    
    String message() default "入力が不正です";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {}; 
}
