package io.github.ghtcd;

import java.util.Optional;

/**
 * A generic result object that holds the outcome of a parsing operation.
 * It can contain either a {@link PackageURL} or a {@link ComponentVersion}.
 */
public final class ParseResult {

    private final PackageURL packageURL;
    private final ComponentVersion componentVersion;

    private ParseResult(PackageURL packageURL, ComponentVersion componentVersion) {
        this.packageURL = packageURL;
        this.componentVersion = componentVersion;
    }

    static ParseResult from(PackageURL packageURL) {
        return new ParseResult(packageURL, null);
    }

    static ParseResult from(ComponentVersion componentVersion) {
        return new ParseResult(null, componentVersion);
    }

    /**
     * @return An {@link Optional} containing the {@link PackageURL} if the input was a PURL.
     */
    public Optional<PackageURL> getPackageURL() {
        return Optional.ofNullable(packageURL);
    }

    /**
     * @return An {@link Optional} containing the {@link ComponentVersion} if the input was a simple component string.
     */
    public Optional<ComponentVersion> getComponentVersion() {
        return Optional.ofNullable(componentVersion);
    }
}