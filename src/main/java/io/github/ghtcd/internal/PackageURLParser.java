package io.github.ghtcd.internal;

import io.github.ghtcd.PackageURL;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A parser for Package URLs (PURLs).
 */
public class PackageURLParser {

    private static final String SCHEME = "pkg";

    /**
     * Parses a Package URL string into its components.
     *
     * @param purlString The PURL string to parse.
     * @return An {@link Optional} containing the {@link PackageURL} if parsing is successful,
     *         otherwise an empty {@link Optional}.
     */
    public static Optional<PackageURL> parse(String purlString) {

        // if purlString is null or is empty, return an empty
        if (purlString == null || purlString.trim().isEmpty()) {
            return Optional.empty();
        }

        // if purlString does not start with pkg:, return an empty (Ignore case)
        if (!purlString.toLowerCase().startsWith(SCHEME + ":")) {
            return Optional.empty();
        }

        // 1. Handle scheme (e.g., "pkg:maven/org.apache.commons/commons-compress@1.15" -> "maven/org.apache.commons/commons-compress@1.15")
        String rest = purlString.substring(SCHEME.length() + 1);

        // 2. Handle subpath (e.g., "golang/google.golang.org/genproto#googleapis/api/annotations" -> subpath="googleapis/api/annotations", rest="golang/google.golang.org/genproto")
        String subpath = null;
        int subpathIndex = rest.indexOf('#');
        if (subpathIndex != -1) {
            subpath = decode(rest.substring(subpathIndex + 1));
            rest = rest.substring(0, subpathIndex);
        }

        // 3. Handle qualifiers (e.g., "maven/log4j-api@2.12.1?classifier=tests&type=test-jar" -> qualifiers={classifier:"tests", type:"test-jar"}, rest="maven/log4j-api@2.12.1")
        Map<String, String> qualifiers = Collections.emptyMap();
        int qualifiersIndex = rest.indexOf('?');
        if (qualifiersIndex != -1) {
            String qualifiersString = rest.substring(qualifiersIndex + 1);
            qualifiers = Arrays.stream(qualifiersString.split("&"))
                .map(pair -> pair.split("=", 2))
                .collect(Collectors.toMap(
                    p -> decode(p[0]),
                    p -> p.length > 1 ? decode(p[1]) : "",
                    (v1, v2) -> v2, // In case of duplicate keys, the last one wins
                    LinkedHashMap::new
                ));
            rest = rest.substring(0, qualifiersIndex);
        }

        // 4. Handle version (e.g., "maven/org.apache.commons/commons-lang3@3.3.2" -> version="3.3.2", rest="maven/org.apache.commons/commons-lang3")
        String version = null;
        int versionIndex = rest.lastIndexOf('@');
        if (versionIndex != -1) {
            int firstSlash = rest.indexOf('/');
            if (versionIndex > firstSlash) {
                version = decode(rest.substring(versionIndex + 1));
                rest = rest.substring(0, versionIndex);
            }
        }

        // 5. Handle type and components (e.g., "maven/org.apache.commons/commons-lang3" -> type="maven", namespace="org.apache.commons", name="commons-lang3")
        int firstSlashIndex = rest.indexOf('/');
        if (firstSlashIndex == -1 || firstSlashIndex == rest.length() - 1) {
            return Optional.empty();
        }
        String type = rest.substring(0, firstSlashIndex).toLowerCase();
        String[] components = rest.substring(firstSlashIndex + 1).split("/");

        String name = decode(components[components.length - 1]);
        String namespace = components.length > 1
            ? Arrays.stream(components, 0, components.length - 1).map(PackageURLParser::decode).collect(Collectors.joining("/"))
            : null;

        return Optional.of(new PackageURL(type, Optional.ofNullable(namespace), name, Optional.ofNullable(version), qualifiers, Optional.ofNullable(subpath)));
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

}
