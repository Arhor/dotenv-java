package com.github.arhor.dotenv;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Dotenv {

    static DotenvConfigurer configure() {
        return DotenvConfigurer.getInstance();
    }

    @Nullable
    String get(@Nonnull String name);

    @Nullable
    String get(@Nonnull String name, @Nullable String defaultValue);

    @Nonnull
    String getRequired(@Nonnull String name) throws MissingPropertyException;
}
