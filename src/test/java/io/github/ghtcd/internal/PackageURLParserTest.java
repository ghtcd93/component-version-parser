package io.github.ghtcd.internal;

import io.github.ghtcd.PackageURL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PackageURLParserTest {

    @ParameterizedTest
    @CsvSource({
            // Standard Maven
            "pkg:maven/org.antlr/ST4@4.0.7,                     maven,  org.antlr,            ST4,                      4.0.7",
            "pkg:maven/com.sun/activation@1.1,                  maven,  com.sun,              activation,               1.1",
            "pkg:maven/org.apache.commons/commons-lang3@3.3.2,  maven,  org.apache.commons,   commons-lang3,            3.3.2",
            // Multi-level namespace
            "pkg:maven/org.springframework.boot/spring-boot-starter-web@2.7.5, maven, org.springframework.boot, spring-boot-starter-web, 2.7.5",
            // No namespace
            "pkg:npm/foobar@1.2.3,                              npm,    ,                     foobar,                   1.2.3",
            // npm scoped package (namespace starts with @)
            "pkg:npm/@angular/core@12.0.0,                      npm,    @angular,             core,                     12.0.0",
            // No version
            "pkg:gem/ruby-advisory-db-check,                    gem,    ,                     ruby-advisory-db-check,   ",
            // No version and no namespace
            "pkg:npm/foobar,                                    npm,    ,                     foobar,                   ",
            // Golang with 'v' prefix in version
            "pkg:golang/github.com/gorilla/mux@v1.8.0,          golang, github.com/gorilla,   mux,                      v1.8.0",
    })
    @DisplayName("Should parse various valid PURL strings correctly")
    void shouldParseValidPurls(String purl, String type, String namespace, String name, String version) {
        // when
        Optional<PackageURL> result = PackageURLParser.parse(purl);

        // then
        assertTrue(result.isPresent(), "Parsing should be successful for: " + purl);
        PackageURL parsed = result.get();

        assertEquals(type, parsed.type());
        assertEquals(Optional.ofNullable(namespace), parsed.namespace());
        assertEquals(name, parsed.name());
        assertEquals(Optional.ofNullable(version), parsed.version());
    }

    @Test
    @DisplayName("Should parse PURL with qualifiers correctly")
    void shouldParsePurlWithQualifiers() {
        // given
        String purl = "pkg:maven/org.apache.logging.log4j/log4j-api@2.12.1?classifier=tests&type=test-jar";
        Map<String, String> expectedQualifiers = Map.of("classifier", "tests", "type", "test-jar");

        // when
        Optional<PackageURL> result = PackageURLParser.parse(purl);

        // then
        assertTrue(result.isPresent());
        assertEquals(expectedQualifiers, result.get().qualifiers());
    }

    @Test
    @DisplayName("Should parse PURL with subpath correctly")
    void shouldParsePurlWithSubpath() {
        // given
        String purl = "pkg:golang/google.golang.org/genproto#googleapis/api/annotations";

        // when
        Optional<PackageURL> result = PackageURLParser.parse(purl);

        // then
        assertTrue(result.isPresent());
        assertEquals(Optional.of("googleapis/api/annotations"), result.get().subpath());
    }

    @Test
    @DisplayName("Should parse PURL with URL encoded characters")
    void shouldParsePurlWithUrlEncodedChars() {
        // given
        String purl = "pkg:maven/a/b@1.0?repository_url=http%3A%2F%2Fexample.com";

        // when
        Optional<PackageURL> result = PackageURLParser.parse(purl);

        // then
        assertTrue(result.isPresent());
        assertEquals("http://example.com", result.get().qualifiers().get("repository_url"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "  ", // Whitespace
            "not-a-purl", // Missing scheme
            "pkg", // Missing colon
            "pkg:", // Missing type
            "pkg:type", // Missing name part
            "pkg:type/", // Missing name after type and slash
    })
    @DisplayName("Should return an empty Optional for invalid or unparseable PURL input")
    void shouldReturnEmptyForInvalidPurlInput(String input) {
        // when
        Optional<PackageURL> result = PackageURLParser.parse(input);

        // then
        assertEquals(Optional.empty(), result);
    }
}
