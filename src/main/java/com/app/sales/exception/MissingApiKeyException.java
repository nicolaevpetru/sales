package com.app.sales.exception;

import org.springframework.security.core.AuthenticationException;

public class MissingApiKeyException extends AuthenticationException {
    public MissingApiKeyException(String message) {
        super(message);
    }
}