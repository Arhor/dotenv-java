package dev.arhor.dotenv;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Dotenv {

    static DotenvConfigurer configure() {
        return new DotenvConfigurer();
    }

    @Nonnull
    String get(@Nonnull String name) throws MissingPropertyException;

    @Nullable
    String get(@Nonnull String name, @Nullable String defaultValue);

    boolean contains(@Nonnull String name);
}
