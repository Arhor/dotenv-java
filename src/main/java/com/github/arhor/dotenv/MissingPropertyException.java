package com.github.arhor.dotenv;

public final class MissingPropertyException extends RuntimeException {

    public MissingPropertyException(final String message) {
        super(message);
    }
}
