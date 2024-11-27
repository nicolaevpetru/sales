package com.app.sales.exception;


import org.springframework.security.core.AuthenticationException;

public class InvalidApiKeyException extends AuthenticationException {
    public InvalidApiKeyException(String message) {
        super(message);
    }
}