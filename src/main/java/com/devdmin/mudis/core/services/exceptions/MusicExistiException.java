package com.devdmin.mudis.core.services.exceptions;

public class MusicExistiException extends RuntimeException {
    public MusicExistiException(String message, Throwable cause) {
        super(message, cause);
    }

    public MusicExistiException(String message) {
        super(message);
    }

    public MusicExistiException() {
        super();
    }
}
