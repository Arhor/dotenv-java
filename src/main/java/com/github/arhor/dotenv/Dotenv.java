package com.github.arhor.dotenv;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface representing loaded .env file.
 */
public interface Dotenv {

    /**
     * Factory method returning {@link DotenvConfigurer} instance.
     *
     * @return Dotenv configurer
     */
    static DotenvConfigurer configure() {
        return DotenvConfigurer.getInstance();
    }

    @Nullable
    String get(@Nullable String name);

    @Nullable
    String get(@Nullable String name, @Nullable String defaultValue);

    @Nonnull
    String getRequired(@Nonnull String name) throws MissingPropertyException;
}
