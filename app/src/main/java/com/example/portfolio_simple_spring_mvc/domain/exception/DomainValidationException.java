package com.example.portfolio_simple_spring_mvc.domain.exception;

public class DomainValidationException extends DomainException {
    private static final long serialVersionUID = 1L;

    public DomainValidationException(DomainErrorMessage domainErrorMessage) {
        super(domainErrorMessage.getKey());
    }
}
