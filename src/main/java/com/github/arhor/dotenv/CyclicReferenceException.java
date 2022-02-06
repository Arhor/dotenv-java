package com.github.arhor.dotenv;

public final class CyclicReferenceException extends RuntimeException {

    public CyclicReferenceException(final String message) {
        super(message);
    }
}
