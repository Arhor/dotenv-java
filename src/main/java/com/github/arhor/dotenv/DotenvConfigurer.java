package com.github.arhor.dotenv;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Properties;

public final class DotenvConfigurer {

    private static final class LazyHolder {
        private static final DotenvConfigurer DEFAULT = new DotenvConfigurer(
            false,
            true,
            true,
            ".",
            ".env"
        );
    }

    private final boolean strictMode;
    private final boolean includeSystemVariables;
    private final boolean allowOverrideSystemVariables;
    private final String location;
    private final String filename;

    private DotenvConfigurer(
        final boolean strictMode,
        final boolean includeSystemVariables,
        final boolean allowOverrideSystemVariables,
        final String location,
        final String filename
    ) {
        this.strictMode = strictMode;
        this.includeSystemVariables = includeSystemVariables;
        this.allowOverrideSystemVariables = allowOverrideSystemVariables;
        this.location = location;
        this.filename = filename;
    }

    static DotenvConfigurer getInstance() {
        return LazyHolder.DEFAULT;
    }

    @Nonnull
    public Dotenv load() {
        final Map<String, String> systemEnvironment = System.getenv();
        final Properties fileContent = DotenvFileLoader.readDotenvFileAsProperties(location, filename);
        return new DotenvImpl(this, systemEnvironment, fileContent);
    }

    @Nonnull
    public DotenvConfigurer strictMode(final boolean strictMode) {
        return new DotenvConfigurer(
            strictMode,
            includeSystemVariables,
            allowOverrideSystemVariables,
            location,
            filename
        );
    }

    @Nonnull
    public DotenvConfigurer includeSystemVariables(final boolean includeSystemVariables) {
        return new DotenvConfigurer(
            strictMode,
            includeSystemVariables,
            allowOverrideSystemVariables,
            location,
            filename
        );
    }


    @Nonnull
    public DotenvConfigurer allowOverrideSystemVariables(final boolean allowOverrideSystemVariables) {
        return new DotenvConfigurer(
            strictMode,
            includeSystemVariables,
            allowOverrideSystemVariables,
            location,
            filename
        );
    }


    @Nonnull
    public DotenvConfigurer location(@Nonnull final String location) {
        return new DotenvConfigurer(
            strictMode,
            includeSystemVariables,
            allowOverrideSystemVariables,
            location,
            filename
        );
    }


    @Nonnull
    public DotenvConfigurer filename(@Nonnull final String filename) {
        return new DotenvConfigurer(
            strictMode,
            includeSystemVariables,
            allowOverrideSystemVariables,
            location,
            filename
        );
    }

    public boolean isStrictMode() {
        return strictMode;
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
