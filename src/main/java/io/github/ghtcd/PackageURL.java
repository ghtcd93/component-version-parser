package io.github.ghtcd;

import java.util.Map;
import java.util.Optional;

/**
 * Represents a parsed Package URL (PURL).
 * See <a href="https://github.com/package-url/purl-spec">PURL Specification</a>
 *
 * @param type       The package "type" or "protocol" (e.g., "maven", "npm", "pypi").
 * @param namespace  The name prefix, such as a Maven group ID or a GitHub user.
 * @param name       The name of the package.
 * @param version    The version of the package.
 * @param qualifiers A map of key-value pairs for additional qualifying data.
 * @param subpath    Extra sub-path within a package, relative to the package root.
 */
public record PackageURL (
    String type,
    Optional<String> namespace,
    String name,
    Optional<String> version,
    Map<String, String> qualifiers,
    Optional<String> subpath
) {}
