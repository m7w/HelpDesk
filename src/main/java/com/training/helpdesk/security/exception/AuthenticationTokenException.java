package com.training.helpdesk.security.exception;

public class AuthenticationTokenException extends RuntimeException {

    public AuthenticationTokenException(String message) {
        super(message);
    }
}
