package io.github.arhor.dotenv;

public final class CyclicReferenceException extends DotenvException {

    public CyclicReferenceException(final String searchPath) {
        super("Cyclic references found, path: " + searchPath);
    }
}
