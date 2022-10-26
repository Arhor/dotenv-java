package io.github.arhor.dotenv;

public final class UnresolvedReferenceException extends DotenvException {

    public UnresolvedReferenceException(final String searchPath, final String reference) {
        super("Cannot resolve reference with name '" + reference + "', path: " + searchPath);
    }
}
