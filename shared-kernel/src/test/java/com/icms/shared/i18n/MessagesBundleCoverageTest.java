package com.icms.shared.i18n;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Static coverage contract between the i18n bundles and {@code src/main/java}.
 * <p>
 * Every business code and validation key referenced from production code must
 * exist in both bundles, and every bundle key must be referenced from
 * production code or explicitly reserved. This is the safety net that catches
 * forgotten renames, because {@code setUseCodeAsDefaultMessage(true)} would
 * otherwise return the code itself as message instead of failing.
 * </p>
 */
@SuppressWarnings("null")
class MessagesBundleCoverageTest {

    /** Bundle keys that are intentionally defined without any code reference. */
    private static final Set<String> RESERVED_MESSAGE_KEYS = Set.of();

    private static final String EN_BUNDLE = "/i18n/messages.properties";
    private static final String ES_BUNDLE = "/i18n/messages_es.properties";

    private static final Pattern BUSINESS_CODE_LITERAL = Pattern.compile("\"([A-Z][A-Za-z]*-\\d{3})\"");
    private static final Pattern VALIDATION_KEY_LITERAL = Pattern.compile("message\\s*=\\s*\"\\{([^}]+)\\}\"");

    @Test
    @DisplayName("Every code referenced in src/main/java exists in both bundles")
    void everyReferencedCodeExistsInBundles() {
        Set<String> businessCodes = referencedBusinessCodes();
        Set<String> validationKeys = referencedValidationKeys();

        assertThat(businessCodes).as("business codes in production code").isNotEmpty();
        assertThat(validationKeys).as("validation keys in production code").isNotEmpty();

        assertThat(businessCodes).isSubsetOf(bundleKeys(EN_BUNDLE));
        assertThat(businessCodes).isSubsetOf(bundleKeys(ES_BUNDLE));
        assertThat(validationKeys).isSubsetOf(bundleKeys(EN_BUNDLE));
        assertThat(validationKeys).isSubsetOf(bundleKeys(ES_BUNDLE));
    }

    @Test
    @DisplayName("Every bundle key is referenced from production code or reserved")
    void everyBundleKeyIsReferencedOrReserved() {
        Set<String> referenced = new TreeSet<>(RESERVED_MESSAGE_KEYS);
        referenced.addAll(referencedBusinessCodes());
        referenced.addAll(referencedValidationKeys());

        assertThat(bundleKeys(EN_BUNDLE))
            .as("keys of %s without any reference in src/main/java", EN_BUNDLE)
            .isSubsetOf(referenced);
        assertThat(bundleKeys(ES_BUNDLE))
            .as("keys of %s without any reference in src/main/java", ES_BUNDLE)
            .isSubsetOf(referenced);
    }

    /* Reads a bundle from the classpath and returns its keys as a sorted set. */
    @SuppressWarnings("resource")
    //TODO: check this warning
    private static Set<String> bundleKeys(String bundlePath) {
        java.io.InputStream stream = MessagesBundleCoverageTest.class.getResourceAsStream(bundlePath);
        assertThat(stream).as("bundle %s must exist on the test classpath", bundlePath).isNotNull();
        try (Stream<String> lines = new java.io.BufferedReader(
                new java.io.InputStreamReader(stream, StandardCharsets.UTF_8)).lines()) {
            Set<String> keys = new TreeSet<>();
            lines.forEach(line -> {
                String trimmed = line.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("#") && trimmed.contains("=")) {
                    keys.add(trimmed.substring(0, trimmed.indexOf('=')).trim());
                }
            });
            return keys;
        }
    }

    /* All business codes quoted in production sources, e.g. "Ent-001". */
    private static Set<String> referencedBusinessCodes() {
        Set<String> codes = new TreeSet<>();
        for (String source : productionSources()) {
            Matcher matcher = BUSINESS_CODE_LITERAL.matcher(source);
            while (matcher.find()) {
                codes.add(matcher.group(1));
            }
        }
        return codes;
    }

    /* All Bean Validation keys bound with message = "{...}" in production sources. */
    private static Set<String> referencedValidationKeys() {
        Set<String> keys = new TreeSet<>();
        for (String source : productionSources()) {
            Matcher matcher = VALIDATION_KEY_LITERAL.matcher(source);
            while (matcher.find()) {
                keys.add(matcher.group(1));
            }
        }
        return keys;
    }

    /* Content of every .java file under any src/main/java of the repository. */
    private static List<String> productionSources() {
        List<String> sources = new ArrayList<>();
        for (Path file : mainJavaFiles(repositoryRoot())) {
            try {
                sources.add(Files.readString(file, StandardCharsets.UTF_8));
            } catch (IOException ex) {
                throw new IllegalStateException("Cannot read source file " + file, ex);
            }
        }
        return sources;
    }

    /*
     * Walks up from the working directory until the Gradle settings file of the
     * repository is found. Fails loudly when the repository root is unknown,
     * instead of passing silently against an empty file set.
     */
    private static Path repositoryRoot() {
        Path current = Path.of(System.getProperty("user.dir")).toAbsolutePath().normalize();
        while (current != null) {
            if (Files.isRegularFile(current.resolve("settings.gradle"))
                    || Files.isRegularFile(current.resolve("settings.gradle.kts"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException(
            "Repository root not found: no settings.gradle nor settings.gradle.kts above "
                + System.getProperty("user.dir"));
    }

    /* Recursively collects .java files under src/main/java, skipping build output. */
    private static List<Path> mainJavaFiles(Path directory) {
        List<Path> files = new ArrayList<>();
        try (Stream<Path> children = Files.list(directory)) {
            for (Path child : (Iterable<Path>) children::iterator) {
                String name = child.getFileName().toString();
                if (Files.isDirectory(child)) {
                    if (name.equals(".git") || name.equals(".gradle") || name.equals("build")) {
                        continue;
                    }
                    files.addAll(mainJavaFiles(child));
                } else if (name.endsWith(".java") && isUnderMainJava(child)) {
                    files.add(child);
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot list directory " + directory, ex);
        }
        return files;
    }

    /* True when the file lives below a {@code src/main/java} directory. */
    private static boolean isUnderMainJava(Path file) {
        Path current = file.getParent();
        while (current != null) {
            Path name = current.getFileName();
            Path parent = current.getParent();
            if (name != null && name.toString().equals("java") && parent != null
                    && parent.getFileName() != null && parent.getFileName().toString().equals("main")) {
                return true;
            }
            current = parent;
        }
        return false;
    }
}
