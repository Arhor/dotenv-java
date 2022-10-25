package com.github.arhor.dotenv;

public final class MissingPropertyException extends DotenvException {

    public MissingPropertyException(final String message) {
        super(message);
    }
}
