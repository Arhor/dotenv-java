package dev.arhor.dotenv;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Properties;

final class DotenvImpl implements Dotenv {

    private static final String REF_START = "${";
    private static final String REF_END = "}";
    private static final int MAX_SUBST_DEPTH = 5;

    private final boolean caseSensitive;
    private final boolean includeSystemVariables;
    private final boolean allowOverrideSystemVariables;

    private final Properties fileContent;

    DotenvImpl(final @Nonnull DotenvConfigurer config, final Properties fileContent) {
        this.caseSensitive = config.isCaseSensitive();
        this.includeSystemVariables = config.isIncludeSystemVariables();
        this.allowOverrideSystemVariables = config.isAllowOverrideSystemVariables();
        this.fileContent = fileContent;
    }

    @Nonnull
    @Override
    public String get(final @Nonnull String name) {
        return getProperty(name);
    }

    @Nullable
    @Override
    public String get(final @Nonnull String name, final @Nullable String defaultValue) {
        return getProperty(name);
    }

    @Override
    public boolean contains(final @Nonnull String name) {
        return fileContent.containsKey(name);
    }

    // =================================================================================================================

    private String getProperty(String key) {
        return getProperty(key, 0);
    }

    private String getProperty(String key, int level) {
        String value = fileContent.getProperty(key);

        if (value != null) {
            int beginIndex = 0;
            int startName = value.indexOf(REF_START, beginIndex);

            if (startName > 0 && value.charAt(startName - 1) == '\\') {
                return value;
            }

            while (startName != -1) {
                if (level + 1 > MAX_SUBST_DEPTH) {
                    return value;
                }

                int endName = value.indexOf(REF_END, startName);
                if (endName == -1) {
                    return value;
                }

                String constName = value.substring(startName + REF_START.length(), endName);
                String constValue = getProperty(constName, level + 1);

                if (constValue == null) {
                    return value;
                }


                String newValue = startName > 0 ? value.substring(0, startName) : "";
                newValue += constValue;

                beginIndex = newValue.length();

                newValue += value.substring(endName + 1);

                value = newValue;

                startName = value.indexOf(REF_START, beginIndex);
            }
        }

        // Return the value as is
        return value;
    }
}
