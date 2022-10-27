package io.github.arhor.dotenv;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.StringJoiner;

public final class DotenvConfigurer {

    private static final class LazyHolder {
        private static final DotenvConfigurer DEFAULT = new DotenvConfigurer(
            /* location =               */ ".",
            /* filename =               */ ".env",
            /* strictMode =             */ false,
            /* includeSystemVariables = */ true,
            /* replaceSystemVariables = */ true
        );
    }

    private final String location;
    private final String filename;
    private final boolean strictMode;
    private final boolean includeSystemVariables;
    private final boolean replaceSystemVariables;

    private DotenvConfigurer(
        final String location,
        final String filename,
        final boolean strictMode,
        final boolean includeSystemVariables,
        final boolean replaceSystemVariables
    ) {
        this.location = Objects.requireNonNull(location, "location must not be null");
        this.filename = Objects.requireNonNull(filename, "filename must not be null");
        this.strictMode = strictMode;
        this.includeSystemVariables = includeSystemVariables;
        this.replaceSystemVariables = replaceSystemVariables;
    }

    static DotenvConfigurer getInstance() {
        return LazyHolder.DEFAULT;
    }

    @Nonnull
    public Dotenv load() {
        try {
            final var properties = DotenvFileLoader.readDotenvFileAsProperties(location, filename);
            return new DotenvImpl(this, properties);
        } catch (final Exception e) {
            throw new LoadingException(e);
        }
    }

    @Nonnull
    public DotenvConfigurer location(@Nonnull final String location) {
        return new DotenvConfigurer(
            location,
            this.filename,
            this.strictMode,
            this.includeSystemVariables,
            this.replaceSystemVariables
        );
    }

    @Nonnull
    public DotenvConfigurer filename(@Nonnull final String filename) {
        return new DotenvConfigurer(
            this.location,
            filename,
            this.strictMode,
            this.includeSystemVariables,
            this.replaceSystemVariables
        );
    }

    @Nonnull
    public DotenvConfigurer strictMode(final boolean strictMode) {
        return new DotenvConfigurer(
            this.location,
            this.filename,
            strictMode,
            this.includeSystemVariables,
            this.replaceSystemVariables
        );
    }

    @Nonnull
    public DotenvConfigurer includeSystemVariables(final boolean includeSystemVariables) {
        return new DotenvConfigurer(
            this.location,
            this.filename,
            this.strictMode,
            includeSystemVariables,
            this.replaceSystemVariables
        );
    }

    @Nonnull
    public DotenvConfigurer replaceSystemVariables(final boolean replaceSystemVariables) {
        return new DotenvConfigurer(
            this.location,
            this.filename,
            this.strictMode,
            this.includeSystemVariables,
            replaceSystemVariables
        );
    }

    @Nonnull
    public String getLocation() {
        return location;
    }

    @Nonnull
    public String getFilename() {
        return filename;
    }

    public boolean isStrictMode() {
        return strictMode;
    }

    public boolean isIncludeSystemVariables() {
        return includeSystemVariables;
    }

    public boolean isReplaceSystemVariables() {
        return replaceSystemVariables;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DotenvConfigurer that = (DotenvConfigurer) o;

        return strictMode == that.strictMode
            && includeSystemVariables == that.includeSystemVariables
            && replaceSystemVariables == that.replaceSystemVariables
            && location.equals(that.location)
            && filename.equals(that.filename);
    }

    @Override
    public int hashCode() {
        int result = location.hashCode();
        result = 31 * result + filename.hashCode();
        result = 31 * result + (strictMode ? 1 : 0);
        result = 31 * result + (includeSystemVariables ? 1 : 0);
        result = 31 * result + (replaceSystemVariables ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DotenvConfigurer.class.getSimpleName() + "(", ")")
            .add("location='" + location + "'")
            .add("filename='" + filename + "'")
            .add("strictMode=" + strictMode)
            .add("includeSystemVariables=" + includeSystemVariables)
            .add("replaceSystemVariables=" + replaceSystemVariables)
            .toString();
    }
}
