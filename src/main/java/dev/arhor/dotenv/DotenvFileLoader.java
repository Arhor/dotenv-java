package dev.arhor.dotenv;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

final class DotenvFileLoader {

    private static final String SOURCE_FILE = "file:";
    private static final String SOURCE_ANDROID_RESOURCE = "android.resource:";
    private static final String SOURCE_CLASSPATH = "classpath:";

    private DotenvFileLoader() { /* no-op */ }

    public static Properties readDotenvFileAsProperties(final String location, final String filename) {
        try (final InputStream dotenvFileInputStream = getDotenvFileInputStream(location, filename)) {
            final Properties properties = new Properties();
            properties.load(dotenvFileInputStream);
            return properties;
        } catch (Exception e) {
            throw new RuntimeException(
                "Could not find " + filename + " within location " + location + " on the file system ",
                e
            );
        }
    }

    private static InputStream getDotenvFileInputStream(final String location, final String filename)
        throws IOException {

        final String fileLocation = getDotenvFileLocation(location, filename);
        final Path path = getDotenvFilePath(fileLocation);

        if (Files.exists(path)) {
            return Files.newInputStream(path);
        } else {
            return getDotenvFileInputStreamFromClasspath(fileLocation);
        }
    }

    private static InputStream getDotenvFileInputStreamFromClasspath(final String fileLocation) {
        final Class<?> currentClass = DotenvFileLoader.class;

        InputStream inputStream = currentClass.getResourceAsStream(fileLocation);
        if (inputStream == null) {
            inputStream = currentClass.getClassLoader().getResourceAsStream(fileLocation);
        }
        if (inputStream == null) {
            inputStream = ClassLoader.getSystemResourceAsStream(fileLocation);
        }
        if (inputStream == null) {
            throw new RuntimeException("Could not find " + fileLocation + " on the classpath");
        }
        return inputStream;
    }

    private static String getDotenvFileLocation(final String location, final String filename) {
        final String dir = location
            .replaceAll("\\\\", "/")
            .replaceFirst("\\.env$", "")
            .replaceFirst("/$", "");

        return dir + File.separator + filename;
    }

    private static Path getDotenvFilePath(final String fileLocation) {
        return shouldUseURI(fileLocation.toLowerCase())
            ? Paths.get(URI.create(fileLocation))
            : Paths.get(fileLocation);
    }

    private static boolean shouldUseURI(final String fileLocation) {
        return fileLocation.startsWith(SOURCE_FILE)
            || fileLocation.startsWith(SOURCE_ANDROID_RESOURCE);
    }
}
