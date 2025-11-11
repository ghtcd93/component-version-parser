package io.github.ghtcd;

/**
 * A record to hold the parsed component name and version.
 * @param name The name of the component.
 * @param version The version of the component.
 */
public record ComponentVersion(String name, String version) {
}