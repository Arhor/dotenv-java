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

    private final boolean strictMode;
    private final boolean includeSystemVariables;
    private final boolean allowOverrideSystemVariables;

    private final Map<String, String> systemEnvironment;
    private final Properties fileContent;

    private final Map<String, String> resolvedRefs = new HashMap<>();
    private final Set<String> currentSearchHistory = new LinkedHashSet<>();

    DotenvImpl(
        final DotenvConfigurer config,
        final Map<String, String> systemEnvironment,
        final Properties fileContent
    ) {
        Objects.requireNonNull(config, "config must not be null");
        Objects.requireNonNull(systemEnvironment, "systemEnvironment must not be null");
        Objects.requireNonNull(fileContent, "fileContent must not be null");

        this.strictMode = config.isStrictMode();
        this.includeSystemVariables = config.isIncludeSystemVariables();
        this.allowOverrideSystemVariables = config.isAllowOverrideSystemVariables();
        this.systemEnvironment = systemEnvironment;
        this.fileContent = fileContent;
    }

    @Nullable
    @Override
    public String get(@Nullable final String name) {
        return getPropertyThenClearSearchHistory(name);
    }

    @Nullable
    @Override
    public String get(@Nullable final String name, @Nullable final String defaultValue) {
        final var property = getPropertyThenClearSearchHistory(name);
        return (property != null)
            ? property
            : defaultValue;
    }

    @Nonnull
    @Override
    public String getRequired(@Nonnull final String name) throws MissingPropertyException {
        final var property = getPropertyThenClearSearchHistory(name);
        if (property != null) {
            return property;
        }
        throw new MissingPropertyException("Cannot find property `" + name + "`");
    }

    private String getPropertyThenClearSearchHistory(final String name) {
        try {
            return getProperty(name);
        } finally {
            currentSearchHistory.clear();
        }
    }

    private String getProperty(final String name) {
        final var property = findProperty(name);
        return (property != null)
            ? resolveReferences(property)
            : null;
    }

    private String findProperty(final String name) {
        final var property = includeSystemVariables
            ? systemEnvironment.get(name)
            : null;
        return ((property == null) || allowOverrideSystemVariables)
            ? fileContent.getProperty(name)
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
        if (!currentSearchHistory.add(refName)) {
            throw new CyclicReferenceException(currentSearchPath(), SEARCH_PATH_DELIMITER, refName);
        }

        var result = resolvedRefs.get(refName);

        if (result == null) {
            result = getProperty(refName);
            if (result == null) {
                if (strictMode) {
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
