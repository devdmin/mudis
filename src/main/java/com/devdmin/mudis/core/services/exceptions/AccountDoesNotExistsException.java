package com.devdmin.mudis.core.services.exceptions;

public class AccountDoesNotExistsException extends RuntimeException {

    public AccountDoesNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountDoesNotExistsException(String message) {
        super(message);
    }

    public AccountDoesNotExistsException() {
        super();
    }
}
