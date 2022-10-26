package io.github.arhor.dotenv;

public final class MissingPropertyException extends DotenvException {

    public MissingPropertyException(final String name) {
        super("Cannot find property: '" + name + "'");
    }
}
