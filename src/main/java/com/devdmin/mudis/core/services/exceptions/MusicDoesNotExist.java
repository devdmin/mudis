package com.devdmin.mudis.core.services.exceptions;

public class MusicDoesNotExist extends RuntimeException {
    public MusicDoesNotExist(String message, Throwable cause) {
        super(message, cause);
    }

    public MusicDoesNotExist(String message) {
        super(message);
    }

    public MusicDoesNotExist() {
        super();
    }
}
