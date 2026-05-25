package com.example.portfolio_simple_spring_mvc.infrastructure.exception;

public class SupabaseStorageException extends InfrastructureException {
    private static final long serialVersionUID = 1L;

    public SupabaseStorageException(String message) {
        super(message);
    }
}
