package com.devdmin.mudis.core.services.exceptions;

public class AccountExistsException extends RuntimeException {

    public AccountExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountExistsException(String message) {
        super(message);
    }

    public AccountExistsException() {
        super();
    }
}
