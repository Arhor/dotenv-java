package com.github.arhor.dotenv;

public final class UnresolvedReferenceException extends RuntimeException {

    public UnresolvedReferenceException(final String message) {
        super(message);
    }
}
