package dev.arhor.dotenv;

import javax.annotation.Nonnull;
import java.util.Properties;

public final class DotenvConfigurer {

    private static final boolean DEFAULT_CASE_SENSITIVE = true;
    private static final boolean DEFAULT_INCLUDE_SYSTEM_VARIABLES = true;
    private static final boolean DEFAULT_ALLOW_OVERRIDE_SYSTEM_VARIABLES = true;
    private static final String DEFAULT_LOCATION = "./";
    private static final String DEFAULT_FILENAME = ".env";

    private final boolean caseSensitive;
    private final boolean includeSystemVariables;
    private final boolean allowOverrideSystemVariables;
    private final String location;
    private final String filename;

    DotenvConfigurer() {
        this(
            DEFAULT_CASE_SENSITIVE,
            DEFAULT_INCLUDE_SYSTEM_VARIABLES,
            DEFAULT_ALLOW_OVERRIDE_SYSTEM_VARIABLES,
            DEFAULT_LOCATION,
            DEFAULT_FILENAME
        );
    }

    private DotenvConfigurer(
        final boolean caseSensitive,
        final boolean includeSystemVariables,
        final boolean allowOverrideSystemVariables,
        final String location,
        final String filename
    ) {
        this.caseSensitive = caseSensitive;
        this.includeSystemVariables = includeSystemVariables;
        this.allowOverrideSystemVariables = allowOverrideSystemVariables;
        this.location = location;
        this.filename = filename;
    }

    @Nonnull
    public Dotenv load() {
        final Properties fileContent = DotenvFileLoader.readDotenvFileAsProperties(location, filename);
        return new DotenvImpl(this, fileContent);
    }

    @Nonnull
    public DotenvConfigurer caseSensitive(final boolean caseSensitive) {
        return new DotenvConfigurer(
            caseSensitive,
            includeSystemVariables,
            allowOverrideSystemVariables,
            location,
            filename
        );
    }


    @Nonnull
    public DotenvConfigurer includeSystemVariables(final boolean includeSystemVariables) {
        return new DotenvConfigurer(
            caseSensitive,
            includeSystemVariables,
            allowOverrideSystemVariables,
            location,
            filename
        );
    }


    @Nonnull
    public DotenvConfigurer allowOverrideSystemVariables(final boolean allowOverrideSystemVariables) {
        return new DotenvConfigurer(
            caseSensitive,
            includeSystemVariables,
            allowOverrideSystemVariables,
            location,
            filename
        );
    }


    @Nonnull
    public DotenvConfigurer location(@Nonnull final String location) {
        return new DotenvConfigurer(
            caseSensitive,
            includeSystemVariables,
            allowOverrideSystemVariables,
            location,
            filename
        );
    }


    @Nonnull
    public DotenvConfigurer filename(@Nonnull final String filename) {
        return new DotenvConfigurer(
            caseSensitive,
            includeSystemVariables,
            allowOverrideSystemVariables,
            location,
            filename
        );
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public boolean isIncludeSystemVariables() {
        return includeSystemVariables;
    }

    public boolean isAllowOverrideSystemVariables() {
        return allowOverrideSystemVariables;
    }

    @Nonnull
    public String getLocation() {
        return location;
    }

    @Nonnull
    public String getFilename() {
        return filename;
    }
}
