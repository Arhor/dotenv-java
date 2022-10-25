package com.github.arhor.dotenv;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

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

    @NonNull
    String getRequired(@NonNull String name) throws MissingPropertyException;
}
