package com.example.portfolio_simple_spring_mvc.presentation.advice;

import java.net.URI;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainValidationException;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;
import com.example.portfolio_simple_spring_mvc.presentation.controller.ProfileAvatarController;

import jakarta.servlet.http.HttpServletRequest;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = ProfileAvatarController.class)
public class ProfileAvatarExceptionHandler {
    private final MessageUtil messageUtil;
    
    public ProfileAvatarExceptionHandler(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @ExceptionHandler(DomainValidationException.class)
    public ProblemDetail handleDomainValidationException(
        DomainValidationException e,
        HttpServletRequest request
    ) {
        ProblemDetail problem =
            ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        problem.setTitle("422: Unprocessable Entity");
        problem.setDetail(messageUtil.getMessage(e.getMessage()));
        problem.setInstance(URI.create(request.getRequestURI()));

        return problem;
    }
}
