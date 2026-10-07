package com.icms.shared.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Structural contract of the i18n message bundles.
 * <p>
 * Loads {@code /i18n/messages.properties} and {@code /i18n/messages_es.properties}
 * as raw UTF-8 text and verifies the standard layout: mirror between locales,
 * standard section comments, business codes {@code Prefix-NNN} numbered from
 * {@code 001} without gaps, validation keys {@code domain.field.rule}, and a
 * business block that always precedes the validation block.
 * </p>
 */
@SuppressWarnings("null")
class MessagesBundleStructureTest {

    private static final String EN_BUNDLE = "/i18n/messages.properties";
    private static final String ES_BUNDLE = "/i18n/messages_es.properties";

    private static final Pattern BUSINESS_CODE = Pattern.compile("^[A-Z][A-Za-z]*-\\d{3}$");
    private static final Pattern VALIDATION_KEY = Pattern.compile("^[a-z][a-z0-9]*(\\.[a-z][a-z0-9]*)+$");
    private static final Pattern SECTION_COMMENT = Pattern.compile("^# [A-Za-z]+( [A-Za-z]+)* (Errors|Messages|Validation)$");
    private static final Pattern PLACEHOLDER = Pattern.compile("\\{\\d+}");

    /** The 16 section comments of the standard, in bundle order. */
    private static final List<String> STANDARD_SECTIONS = List.of(
        "# Common Errors",
        "# Resource Errors",
        "# Success Messages",
        "# Entity Errors",
        "# Catalog Errors",
        "# Language Errors",
        "# User Errors",
        "# User Profile Errors",
        "# Language Validation",
        "# Permission Validation",
        "# User Type Validation",
        "# User Type Translation Validation",
        "# User Status Validation",
        "# User Status Translation Validation",
        "# Create User Validation",
        "# User Profile Validation"
    );

    @Test
    @DisplayName("Both bundles have the same line count")
    void bothFilesHaveSameLineCount() {
        assertThat(lines(EN_BUNDLE)).hasSameSizeAs(lines(ES_BUNDLE));
    }

    @Test
    @DisplayName("Section comments are identical in both bundles, line by line")
    void sectionCommentsMatchLineByLine() {
        List<String> english = lines(EN_BUNDLE);
        List<String> spanish = lines(ES_BUNDLE);
        assertThat(english).hasSameSizeAs(spanish);
        for (int i = 0; i < english.size(); i++) {
            boolean englishIsComment = isComment(english.get(i));
            assertThat(isComment(spanish.get(i)))
                .as("comment parity at line %d", i + 1)
                .isEqualTo(englishIsComment);
            if (englishIsComment) {
                assertThat(spanish.get(i)).as("comment at line %d", i + 1).isEqualTo(english.get(i));
            }
        }
    }

    @Test
    @DisplayName("Section comments are exactly the 16 sections of the standard, in order")
    void sectionsFollowTheStandardOrder() {
        assertThat(sectionComments(lines(EN_BUNDLE))).isEqualTo(STANDARD_SECTIONS);
        assertThat(sectionComments(lines(ES_BUNDLE))).isEqualTo(STANDARD_SECTIONS);
    }

    @Test
    @DisplayName("Every section comment matches '# <Title> Errors|Messages|Validation'")
    void sectionCommentsFollowStandardFormat() {
        for (String bundle : List.of(EN_BUNDLE, ES_BUNDLE)) {
            for (String comment : sectionComments(lines(bundle))) {
                assertThat(SECTION_COMMENT.matcher(comment).matches())
                    .as("section comment '%s' in %s follows the standard format", comment, bundle)
                    .isTrue();
            }
        }
    }

    @Test
    @DisplayName("Keys appear in the same order in both bundles")
    void keysAppearInSameOrder() {
        assertThat(keys(lines(ES_BUNDLE))).isEqualTo(keys(lines(EN_BUNDLE)));
    }

    @Test
    @DisplayName("No bundle defines the same key twice")
    void noDuplicateKeys() {
        assertThat(keys(lines(EN_BUNDLE))).doesNotHaveDuplicates();
        assertThat(keys(lines(ES_BUNDLE))).doesNotHaveDuplicates();
    }

    @Test
    @DisplayName("Business keys match ^[A-Z][A-Za-z]*-\\d{3}$")
    void businessCodesMatchPattern() {
        for (String key : keys(lines(EN_BUNDLE))) {
            if (isBusinessKey(key)) {
                assertThat(key).as("business code").matches(BUSINESS_CODE.pattern());
            }
        }
        for (String key : keys(lines(ES_BUNDLE))) {
            if (isBusinessKey(key)) {
                assertThat(key).as("business code").matches(BUSINESS_CODE.pattern());
            }
        }
    }

    @Test
    @DisplayName("Business numbering is contiguous per prefix: 001..NNN without gaps")
    void businessNumberingIsSequentialPerPrefix() {
        Map<String, List<Integer>> numbersByPrefix = new TreeMap<>();
        for (String key : keys(lines(EN_BUNDLE))) {
            if (!isBusinessKey(key)) {
                continue;
            }
            int separator = key.lastIndexOf('-');
            String prefix = key.substring(0, separator);
            int number = Integer.parseInt(key.substring(separator + 1));
            numbersByPrefix.computeIfAbsent(prefix, ignored -> new ArrayList<>()).add(number);
        }
        assertThat(numbersByPrefix).isNotEmpty();
        for (Map.Entry<String, List<Integer>> entry : numbersByPrefix.entrySet()) {
            List<Integer> numbers = entry.getValue().stream().sorted().toList();
            List<Integer> expected = java.util.stream.IntStream.rangeClosed(1, numbers.size()).boxed().toList();
            assertThat(numbers)
                .as("numbers of prefix '%s'", entry.getKey())
                .isEqualTo(expected);
        }
    }

    @Test
    @DisplayName("Validation keys match <domain>.<field>.<rule>")
    void validationKeysMatchPattern() {
        for (String key : keys(lines(EN_BUNDLE))) {
            if (!isBusinessKey(key)) {
                assertThat(key).as("validation key").matches(VALIDATION_KEY.pattern());
            }
        }
        for (String key : keys(lines(ES_BUNDLE))) {
            if (!isBusinessKey(key)) {
                assertThat(key).as("validation key").matches(VALIDATION_KEY.pattern());
            }
        }
    }

    @Test
    @DisplayName("The business block always precedes the validation block")
    void validationBlockFollowsBusinessBlock() {
        assertThat(lastBusinessKeyIndex(lines(EN_BUNDLE)))
            .isLessThan(firstValidationKeyIndex(lines(EN_BUNDLE)));
        assertThat(lastBusinessKeyIndex(lines(ES_BUNDLE)))
            .isLessThan(firstValidationKeyIndex(lines(ES_BUNDLE)));
    }

    @Test
    @DisplayName("Placeholders {0}, {1}, ... match between locales")
    void placeholdersMatchBetweenLocales() {
        Map<String, String> english = valuesByKey(lines(EN_BUNDLE));
        Map<String, String> spanish = valuesByKey(lines(ES_BUNDLE));
        for (Map.Entry<String, String> entry : english.entrySet()) {
            assertThat(placeholders(spanish.get(entry.getKey())))
                .as("placeholders of key '%s'", entry.getKey())
                .isEqualTo(placeholders(entry.getValue()));
        }
    }

    @Test
    @DisplayName("Section comments have no trailing spaces")
    void noTrailingSpacesInSectionComments() {
        for (String bundle : List.of(EN_BUNDLE, ES_BUNDLE)) {
            for (String comment : sectionComments(lines(bundle))) {
                assertThat(comment).as("comment '%s' in %s", comment, bundle).isEqualTo(comment.stripTrailing());
            }
        }
    }

    @Test
    @DisplayName("No key has a blank value")
    void allValuesAreNonBlank() {
        for (String bundle : List.of(EN_BUNDLE, ES_BUNDLE)) {
            for (String line : lines(bundle)) {
                if (isKeyLine(line)) {
                    assertThat(valueOf(line)).as("value of '%s' in %s", keyOf(line), bundle).isNotBlank();
                }
            }
        }
    }

    /* Reads a bundle from the classpath as UTF-8 text and splits it into lines. */
    private static List<String> lines(String bundlePath) {
        try (InputStream stream = MessagesBundleStructureTest.class.getResourceAsStream(bundlePath)) {
            assertThat(stream).as("bundle %s must exist on the test classpath", bundlePath).isNotNull();
            String content = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            List<String> result = new ArrayList<>();
            for (String line : content.split("\n", -1)) {
                result.add(line.endsWith("\r") ? line.substring(0, line.length() - 1) : line);
            }
            return result;
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot read bundle " + bundlePath, ex);
        }
    }

    /* Comment line: first non blank character is '#'. */
    private static boolean isComment(String line) {
        return line.trim().startsWith("#");
    }

    /* Key line: not blank, not a comment and contains the key/value separator. */
    private static boolean isKeyLine(String line) {
        String trimmed = line.trim();
        return !trimmed.isEmpty() && !trimmed.startsWith("#") && trimmed.contains("=");
    }

    /* Business keys start with an uppercase letter, validation keys with a lowercase one. */
    private static boolean isBusinessKey(String key) {
        return Character.isUpperCase(key.charAt(0));
    }

    /* All key lines of a bundle, in file order. */
    private static List<String> keys(List<String> lines) {
        List<String> keys = new ArrayList<>();
        for (String line : lines) {
            if (isKeyLine(line)) {
                keys.add(keyOf(line));
            }
        }
        return keys;
    }

    /* All section comments of a bundle, in file order. */
    private static List<String> sectionComments(List<String> lines) {
        List<String> comments = new ArrayList<>();
        for (String line : lines) {
            if (isComment(line)) {
                comments.add(line);
            }
        }
        return comments;
    }

    /* Key of a key line: everything before the first '='. */
    private static String keyOf(String line) {
        return line.substring(0, line.indexOf('=')).trim();
    }

    /* Value of a key line: everything after the first '='. */
    private static String valueOf(String line) {
        return line.substring(line.indexOf('=') + 1);
    }

    /* Key to value map of a bundle, preserving file order. */
    private static Map<String, String> valuesByKey(List<String> lines) {
        Map<String, String> values = new LinkedHashMap<>();
        for (String line : lines) {
            if (isKeyLine(line)) {
                values.put(keyOf(line), valueOf(line));
            }
        }
        return values;
    }

    /* Zero based index of the last business key line, or -1 when there is none. */
    private static int lastBusinessKeyIndex(List<String> lines) {
        int index = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (isKeyLine(lines.get(i)) && isBusinessKey(keyOf(lines.get(i)))) {
                index = i;
            }
        }
        return index;
    }

    /* Zero based index of the first validation key line, or lines.size() when there is none. */
    private static int firstValidationKeyIndex(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            if (isKeyLine(lines.get(i)) && !isBusinessKey(keyOf(lines.get(i)))) {
                return i;
            }
        }
        return lines.size();
    }

    /* All {n} placeholders of a value, in appearance order. */
    private static List<String> placeholders(String value) {
        List<String> placeholders = new ArrayList<>();
        if (value == null) {
            return placeholders;
        }
        Matcher matcher = PLACEHOLDER.matcher(value);
        while (matcher.find()) {
            placeholders.add(matcher.group());
        }
        return placeholders;
    }
}
