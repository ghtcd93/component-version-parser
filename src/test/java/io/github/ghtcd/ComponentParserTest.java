package io.github.ghtcd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComponentParserTest {

    @Test
    @DisplayName("Should delegate to ComponentVersionParser for simple strings")
    void shouldDelegateToComponentVersionParser() {
        // given
        String simpleString = "spring-boot-2.7.5";

        // when
        Optional<ParseResult> result = ComponentParser.parse(simpleString);

        // then
        assertTrue(result.isPresent());
        assertTrue(result.get().getComponentVersion().isPresent());
        assertEquals("spring-boot", result.get().getComponentVersion().get().name());
    }

    @Test
    @DisplayName("Should delegate to PackageURLParser for PURL strings")
    void shouldDelegateToPackageURLParser() {
        // given
        String purlString = "pkg:maven/org.antlr/ST4@4.0.7";

        // when
        Optional<ParseResult> result = ComponentParser.parse(purlString);

        // then
        assertTrue(result.isPresent());
        assertTrue(result.get().getPackageURL().isPresent());
        assertEquals("maven", result.get().getPackageURL().get().type());
        assertEquals("ST4", result.get().getPackageURL().get().name());
    }
}