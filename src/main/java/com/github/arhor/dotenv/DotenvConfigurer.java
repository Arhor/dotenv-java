package com.github.arhor.dotenv;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class DotenvConfigurer {

    private static final class LazyHolder {
        private static final DotenvConfigurer DEFAULT = new DotenvConfigurer(
            /* strictMode = */                   false,
            /* includeSystemVariables = */       true,
            /* allowOverrideSystemVariables = */ true,
            /* location = */                     ".",
            /* filename = */                     ".env"
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
        Objects.requireNonNull(location, "location must not be null");
        Objects.requireNonNull(filename, "filename must not be null");

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
        try {
            final var systemEnvironment = System.getenv();
            final var fileContent = DotenvFileLoader.readDotenvFileAsProperties(location, filename);

            return new DotenvImpl(this, systemEnvironment, fileContent);
        } catch (final Exception e) {
            throw new LoadingException(e);
        }
    }

    @Nonnull
    public DotenvConfigurer strictMode(final boolean strictMode) {
        return new DotenvConfigurer(
            strictMode,
            this.includeSystemVariables,
            this.allowOverrideSystemVariables,
            this.location,
            this.filename
        );
    }

    @Nonnull
    public DotenvConfigurer includeSystemVariables(final boolean includeSystemVariables) {
        return new DotenvConfigurer(
            this.strictMode,
            includeSystemVariables,
            this.allowOverrideSystemVariables,
            this.location,
            this.filename
        );
    }


    @Nonnull
    public DotenvConfigurer allowOverrideSystemVariables(final boolean allowOverrideSystemVariables) {
        return new DotenvConfigurer(
            this.strictMode,
            this.includeSystemVariables,
            allowOverrideSystemVariables,
            this.location,
            this.filename
        );
    }


    @Nonnull
    public DotenvConfigurer location(@Nonnull final String location) {
        return new DotenvConfigurer(
            this.strictMode,
            this.includeSystemVariables,
            this.allowOverrideSystemVariables,
            location,
            this.filename
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
