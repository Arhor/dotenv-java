package com.github.arhor.dotenv;

public final class CyclicReferenceException extends DotenvException {

    public CyclicReferenceException(final String searchPath, final String delimiter, final String reference) {
        super("Cyclic references found, path: " + searchPath + delimiter + reference);
    }
}
