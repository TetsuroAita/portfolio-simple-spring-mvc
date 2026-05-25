package com.example.portfolio_simple_spring_mvc.domain.exception;

public class DomainIllegalStateException extends DomainException {
    private static final long serialVersionUID = 1L;

    public DomainIllegalStateException(DomainErrorMessage domainErrorMessage) {
        super(domainErrorMessage.getKey());
    }
}
