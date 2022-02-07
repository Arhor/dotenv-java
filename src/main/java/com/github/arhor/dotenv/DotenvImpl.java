package com.github.arhor.dotenv;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@NotThreadSafe
final class DotenvImpl implements Dotenv {

    private static final String REFERENCE_START = "${";
    private static final String REFERENCE_END = "}";

    private final boolean strictMode;
    private final boolean includeSystemVariables;
    private final boolean allowOverrideSystemVariables;

    private final Map<String, String> systemEnvironment;
    private final Properties fileContent;

    private final Map<String, String> resolvedRefs = new HashMap<>();
    private final List<String> currentSearchHistory = new ArrayList<>();

    DotenvImpl(
        final @Nonnull DotenvConfigurer config,
        final @Nonnull Map<String, String> systemEnvironment,
        final @Nonnull Properties fileContent
    ) {
        this.strictMode = config.isStrictMode();
        this.includeSystemVariables = config.isIncludeSystemVariables();
        this.allowOverrideSystemVariables = config.isAllowOverrideSystemVariables();
        this.systemEnvironment = systemEnvironment;
        this.fileContent = fileContent;
    }

    @Nullable
    @Override
    public String get(final @Nonnull String name) {
        return getPropertyThenClearSearchHistory(name);
    }

    @Nullable
    @Override
    public String get(final @Nonnull String name, final @Nullable String defaultValue) {
        final String property = getPropertyThenClearSearchHistory(name);
        return (property != null)
            ? property
            : defaultValue;
    }

    @Nonnull
    @Override
    public String getRequired(final @Nonnull String name) throws MissingPropertyException {
        final String property = getPropertyThenClearSearchHistory(name);
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
        String property = findProperty(name);
        return (property != null)
            ? resolveReferences(property)
            : null;
    }

    private String findProperty(final String name) {
        final String property = includeSystemVariables
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
        final int refStartIndex = input.indexOf(REFERENCE_START, startIndex);

        if (refStartIndex == -1) {
            return input;
        }

        final int refEndIndex = input.indexOf(REFERENCE_END, refStartIndex);

        if (refEndIndex == -1) {
            return input;
        }

        final String refName = input.substring(refStartIndex + REFERENCE_START.length(), refEndIndex);
        final String refValue = findRefValue(refName);

        final String prefix = (refStartIndex > 0) ? input.substring(0, refStartIndex) : "";
        final String suffix = input.substring(refEndIndex + 1);

        return resolveReferences(prefix + refValue + suffix, prefix.length() + refValue.length());
    }

    private String findRefValue(final String refName) {
        if (currentSearchHistory.contains(refName)) {
            currentSearchHistory.add(refName);
            throw new CyclicReferenceException(
                "Cyclic references found, path: " + currentSearchPath()
            );
        } else {
            currentSearchHistory.add(refName);
        }

        String result = resolvedRefs.get(refName);
        if (result == null) {
            result = getProperty(refName);
            if (result == null) {
                if (strictMode) {
                    throw new UnresolvedReferenceException(
                        "Cannot resolve reference with name '" + refName + "', path: " + currentSearchPath()
                    );
                }
                result = REFERENCE_START + refName + REFERENCE_END;
            }
            resolvedRefs.put(refName, result);
        }
        return result;
    }

    private String currentSearchPath() {
        return String.join(" -> ", currentSearchHistory);
    }
}
