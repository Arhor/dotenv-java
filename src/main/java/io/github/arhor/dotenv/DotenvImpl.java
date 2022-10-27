package io.github.arhor.dotenv;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

final class DotenvImpl implements Dotenv {

    private static final String SEARCH_PATH_DELIMITER = " -> ";
    private static final String REF_PREFIX = "${";
    private static final String REF_SUFFIX = "}";

    private static final int REF_PREFIX_LENGTH = REF_PREFIX.length();

    private final DotenvConfigurer configurer;
    private final Properties properties;

    private final Map<String, String> resolvedRefs = new HashMap<>();
    private final Set<String> currentSearchHistory = new LinkedHashSet<>();

    DotenvImpl(final DotenvConfigurer configurer, final Properties properties) {
        this.configurer = Objects.requireNonNull(configurer, "configurer must not be null");
        this.properties = Objects.requireNonNull(properties, "properties must not be null");
    }

    @Nullable
    @Override
    public String get(@Nullable final String name) {
        return getPropertyThenClearSearchHistory(name, null, false);
    }

    @Nullable
    @Override
    public String get(@Nullable final String name, @Nullable final String defaultValue) {
        return getPropertyThenClearSearchHistory(name, defaultValue, true);
    }

    @Nonnull
    @Override
    public String getRequired(@Nonnull final String name) throws MissingPropertyException {
        final var property = getPropertyThenClearSearchHistory(name, null, false);
        if (property != null) {
            return property;
        }
        throw new MissingPropertyException(name);
    }

    private String getPropertyThenClearSearchHistory(
        final String name,
        final String defaultValue,
        final boolean useDefaultValue
    ) {
        try {
            final var property = getProperty(name);

            if (property != null || !useDefaultValue) {
                return property;
            } else if (defaultValue != null) {
                return resolveReferences(defaultValue);
            } else {
                return null;
            }
        } finally {
            currentSearchHistory.clear();
        }
    }

    private String getProperty(final String name) {
        if (!currentSearchHistory.add(name)) {
            throw new CyclicReferenceException(currentSearchPath() + SEARCH_PATH_DELIMITER + name);
        }
        final var property = findProperty(name);
        return (property != null)
            ? resolveReferences(property)
            : null;
    }

    private String findProperty(final String name) {
        final var property = configurer.isIncludeSystemVariables()
            ? System.getenv().get(name)
            : null;
        return ((property == null) || configurer.isReplaceSystemVariables())
            ? properties.getProperty(name)
            : property;
    }

    private String resolveReferences(final String input) {
        return resolveReferences(input, 0);
    }

    private String resolveReferences(final String input, final int startIndex) {
        final var refPrefixIndex = input.indexOf(REF_PREFIX, startIndex);
        if (refPrefixIndex == -1) {
            return input;
        }

        final var refSuffixIndex = input.indexOf(REF_SUFFIX, refPrefixIndex);
        if (refSuffixIndex == -1) {
            return input;
        }

        final var refName = input.substring(refPrefixIndex + REF_PREFIX_LENGTH, refSuffixIndex);
        final var refValue = findRefValue(refName);

        final var prefix = (refPrefixIndex > 0) ? input.substring(0, refPrefixIndex) : "";
        final var suffix = input.substring(refSuffixIndex + 1);

        return resolveReferences(prefix + refValue + suffix, prefix.length() + refValue.length());
    }

    private String findRefValue(final String refName) {
        var result = resolvedRefs.get(refName);

        if (result == null) {
            result = getProperty(refName);
            if (result == null) {
                if (configurer.isStrictMode()) {
                    throw new UnresolvedReferenceException(currentSearchPath(), refName);
                }
                result = REF_PREFIX + refName + REF_SUFFIX;
            }
            resolvedRefs.put(refName, result);
        }
        return result;
    }

    private String currentSearchPath() {
        return String.join(" -> ", currentSearchHistory);
    }
}
