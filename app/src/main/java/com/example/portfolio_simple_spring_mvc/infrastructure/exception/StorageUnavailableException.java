package com.example.portfolio_simple_spring_mvc.infrastructure.exception;

public class StorageUnavailableException extends InfrastructureException {
    private static final long serialVersionUID = 1L;

    public StorageUnavailableException() {
    }

    public StorageUnavailableException(String message) {
        super(message);
    }

    public StorageUnavailableException(Throwable cause) {
        super(cause);
    }

    public StorageUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageUnavailableException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
