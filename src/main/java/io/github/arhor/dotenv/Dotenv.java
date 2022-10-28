package io.github.arhor.dotenv;


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

    /**
     * Returns property value for the given name.
     *
     * @param name property name
     * @return property value if present, otherwise null
     */
    @Nullable
    String get(@Nullable String name);

    /**
     * Returns property value for the given name. If value is missing provided default value is used.
     *
     * @param name         property name
     * @param defaultValue default value to use
     * @return property value if present, otherwise default value
     */
    @Nullable
    String get(@Nullable String name, @Nullable String defaultValue);

    /**
     * Returns property value for the given name. If value is missing {@link MissingPropertyException} is thrown.
     *
     * @param name property name
     * @return property value if present, otherwise throws an exception
     * @throws MissingPropertyException exception thrown in case value by the given name is missing
     */
    @Nonnull
    String getRequired(@Nonnull String name) throws MissingPropertyException;
}
