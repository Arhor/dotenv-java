package com.github.arhor.dotenv;

public final class CyclicReferenceException extends DotenvException {

    public CyclicReferenceException(final String message) {
        super(message);
    }
}
