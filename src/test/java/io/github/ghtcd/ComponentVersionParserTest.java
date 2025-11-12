package io.github.ghtcd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for {@link ComponentVersionParser}.
 */
class ComponentVersionParserTest {

    @ParameterizedTest
    @CsvSource({
            // Standard SemVer
            "spring-boot-starter-web-2.7.5,             spring-boot-starter-web,      2.7.5",
            // With qualifier
            "spring-security-core-5.7.3-SNAPSHOT,       spring-security-core,         5.7.3-SNAPSHOT",
            // With 'v' prefix
            "my-component-v1.2.3,                       my-component,                 v1.2.3",
            // Single number version
            "guava-31,                                  guava,                        31",
            // Version with extra text qualifier
            "guava-31.1-jre,                            guava,                        31.1-jre",
            // Date-based version
            "another-lib-20220101,                      another-lib,                  20220101",
            // Complex SemVer with build metadata
            "complex-parser-1.0.0-alpha.1+build.123,    complex-parser,               1.0.0-alpha.1+build.123", // Name with numbers
            "log4j-api-2.17.1,                          log4j-api,                    2.17.1",
            // With '@' separator
            "ST4@4.0.7,                                 ST4,                          4.0.7",
            // With '@' separator and 'v' prefix
            "my-lib@v2.1,                               my-lib,                       v2.1"
    })
    @DisplayName("Should parse various component strings into name and version")
    void shouldParseVariousComponentVersions(String input, String expectedName, String expectedVersion) {
        // given
        // when
        Optional<ComponentVersion> result = ComponentVersionParser.parse(input);

        // then
        assertEquals(Optional.of(new ComponentVersion(expectedName, expectedVersion)), result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "no-version-here", "component-"})
    @DisplayName("Should return an empty Optional for invalid or unparseable input")
    void shouldReturnEmptyForInvalidInput(String input) {
        // when
        Optional<ComponentVersion> result = ComponentVersionParser.parse(input);

        // then
        assertEquals(Optional.empty(), result);
    }
}
