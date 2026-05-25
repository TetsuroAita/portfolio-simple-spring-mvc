package com.example.portfolio_simple_spring_mvc.domain.exception;

public class ProfileNotFoundException extends DomainException {
    private static final long serialVersionUID = 1L;

    public ProfileNotFoundException(DomainErrorMessage domainErrorMessage) {
        super(domainErrorMessage.getKey());
    }
}
